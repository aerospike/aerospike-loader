/*******************************************************************************
 * Copyright 2014 by Aerospike.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 ******************************************************************************/

package com.aerospike.load;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.AerospikeException;
/**
 * This is the main class for the Aerospike import. 
 * 
 * It will import multiple Data Dump files concurrently
 * 
 * To run: java -jar aerospike-import-<version> <options> <file names>
 * The options are:
 * -c,--config <arg>   				Column definition file in JSON format
 * -ec,--abort-Error-Count<arg>		Abort when error occurs more than this value(default: 0(don't abort))
 * -h,--host <arg>      			Server hostname (default: localhost)
 * -n,--namespace <arg> 			Namespace (default: test)
 * -p,--port <arg>      			Server port (default: 3000)
 * -s,--set <arg>       			Set name. (default: null)
 * -tt,--transaction-timeout <arg>	Transaction timeout in milliseconds for write (default: no timeout)
 * -et,--expiration-time <arg>		Time to expire of a record in seconds(default: never expire)
 * -T,--timezone <arg>				TimeZone of source where datadump is taken (default: local timeZone)
 * -u,--usage           			Print usage.
 * -v,--verbose						Verbose mode for debug logging (default: INFO)
 * -wa,--write-Action <arg>			Write action if key already exists (default: update)
 * -wt,--writerThreads<arg>			Number of writer threads (default: 5 * number of cores)
 * -rt,--readerThreads<arg>			Number of reader threads (default: 1 * number of cores)
 * -uk --send-user-key                    	Send user defined key in addition to hash digest to store on the server. (default: userKey is not sent to reduce meta-data overhead)
 * 
 * The file names can be a series of file names or directories. 
 *
 * @author jyoti
 *
 */
public class AerospikeLoad implements Runnable {

	private AerospikeClient 	client;
	private String				fileName;

	private static ExecutorService			writerPool;
	private static int						nWriterThreads;
	private static int						nReaderThreads;
	private static int						rwThrottle;
	private static final int				scaleFactor = 5;
	private static List<ColumnDefinition>	metadataColumnDefs;
	private static List<ColumnDefinition>	binColumnDefs;
	private static HashMap<String, String>	metadataConfigs;
	private static Parameters 				params;
	private static Counter 	counters = new Counter();
	private static Logger	log = Logger.getLogger(AerospikeLoad.class);
	
	public static void main(String[] args) throws IOException{

		Thread statPrinter = new Thread(new PrintStat(counters));
		try {
			log.info("Aerospike loader started");
			Options options = new Options();
			options.addOption("h", "host", true, "Server hostname (default: localhost)");
			options.addOption("p", "port", true, "Server port (default: 3000)");
			options.addOption("n", "namespace", true, "Namespace (default: test)");
			options.addOption("s", "set", true, "Set name. (default: null)");
			options.addOption("c", "config", true, "Column definition file name");
			options.addOption("wt", "write-threads", true, "Number of writer threads (default: Number of cores * 5)");
			options.addOption("rt", "read-threads", true, "Number of reader threads (default: Number of cores * 1)");
			options.addOption("l", "rw-throttle", true, "Throttling of reader to writer(default: 10k) ");
			options.addOption("tt", "transaction-timeout", true, "write transaction timeout in miliseconds(default: No timeout)");
			options.addOption("et", "expiration-time", true, "Expiration time of records in seconds (default: never expire)");
			options.addOption("T", "timezone", true, "Timezone of source where data dump is taken (default: local timezone)");
			options.addOption("ec", "abort-error-count", true, "Error count to abort (default: 0)");
			options.addOption("wa", "write-action", true, "Write action if key already exists (default: update)");
			options.addOption("v", "verbose", false, "Logging all");
			options.addOption("u", "usage", false, "Print usage.");
			options.addOption("uk","send-user-key",false,"Send user defined key in addition to hash digest to store on the server. (default: userKey is not sent to reduce meta-data overhead)");

			CommandLineParser parser = new PosixParser();
			CommandLine cl = parser.parse(options, args, false);

			if (args.length == 0 || cl.hasOption("u")) {
				logUsage(options);
				return;
			}

			if (cl.hasOption("l")){
				rwThrottle = Integer.parseInt(cl.getOptionValue("l"));
			} else {
				rwThrottle = Constants.READLOAD;
			}
			// Get all command line options
			params = Utils.parseParameters(cl);

			//Get client instance
			AerospikeClient client = new AerospikeClient(params.host, params.port);
			if(!client.isConnected()) {
				log.error("Client is not able to connect:" + params.host + ":" + params.port);
				return;
			}

			if (params.verbose) {
				log.setLevel(Level.DEBUG);				
			}

			// Get available processors to calculate default number of threads
			int cpus = Runtime.getRuntime().availableProcessors();
			nWriterThreads = cpus * scaleFactor;
			nReaderThreads = cpus;
			
			// Get writer thread count
			if (cl.hasOption("wt")){
				nWriterThreads = Integer.parseInt(cl.getOptionValue("wt"));
				nWriterThreads = (nWriterThreads > 0 ? (nWriterThreads > Constants.MAX_THREADS ? Constants.MAX_THREADS : nWriterThreads) : 1);
				log.debug("Using writer Threads: " + nWriterThreads);
			}
			writerPool = Executors.newFixedThreadPool(nWriterThreads);
			
			// Get reader thread count
			if (cl.hasOption("rt")){
				nReaderThreads = Integer.parseInt(cl.getOptionValue("rt"));
				nReaderThreads = (nReaderThreads > 0 ? (nReaderThreads > Constants.MAX_THREADS ? Constants.MAX_THREADS :nReaderThreads) : 1);
				log.debug("Using reader Threads: " + nReaderThreads);
			}
			
			String columnDefinitionFileName = cl.getOptionValue("c", null);

			log.debug("Column definition files/directory: " + columnDefinitionFileName);
			if (columnDefinitionFileName == null){
				log.error("Column definition files/directory not specified. use -c <file name>");
				return;
			}

			File columnDefinitionFile = new File(columnDefinitionFileName);
			if (!columnDefinitionFile.exists()){
				log.error("Column definition files/directory does not exist: " + Utils.getFileName(columnDefinitionFileName));
				return;
			}
						
			// Get data file list
			String[] files = cl.getArgs();
			if(files.length == 0) {
				log.error("No data file Specified: add <file/dir name> to end of the command ");
				return;
			}
			List<String> allFileNames = new ArrayList<String>();
			allFileNames = Utils.getFileNames(files);
			if (allFileNames.size() == 0){
				log.error("Given datafiles/directory does not exist");
				return;
			}
			for (int i = 0; i < allFileNames.size(); i++) {
				log.debug("File names:" + Utils.getFileName(allFileNames.get(i)));
				File file = new File(allFileNames.get(i));
				counters.write.recordTotal = counters.write.recordTotal + file.length();
			}
						
			//remove column definition file from list
			allFileNames.remove(columnDefinitionFileName);

			log.info("Number of data files:" + allFileNames.size());

			/**
			 * Process column definition file to get meta data and bin mapping.
			 */
			metadataColumnDefs = new ArrayList<ColumnDefinition>();
			binColumnDefs = new ArrayList<ColumnDefinition>();
			metadataConfigs = new HashMap<String, String>();
			
			
			if (Parser.processJSONColumnDefinitions(columnDefinitionFile, metadataConfigs, metadataColumnDefs, binColumnDefs, params)){
				log.info("Config file processed.");
			} else {
				throw new Exception("Config file parsing Error");
			}
			
			// Add metadata of config to parameters
			String metadata;
			if((metadata = metadataConfigs.get(Constants.INPUT_TYPE)) != null){
				params.fileType = metadata;
				if (params.fileType.equals(Constants.CSV_FILE)) {
					
					// Version check
					metadata = metadataConfigs.get(Constants.VERSION);
					String[] vNumber = metadata.split("\\.");
					int v1 = Integer.parseInt(vNumber[0]);
					int v2 = Integer.parseInt(vNumber[1]);
					if((v1 <= Constants.MajorV) && (v2 <= Constants.MinorV)){
						log.debug("Config version used:" + metadata);
					} else 
						throw new Exception("\"" + Constants.VERSION + ":" + metadata + "\" is not Supported");
					
					// Set delimiter 
					if((metadata = metadataConfigs.get(Constants.DELIMITER)) != null && metadata.length() == 1){
						params.delimiter = metadata.charAt(0);
					} else {
						log.warn("\"" + Constants.DELIMITER + ":" + metadata + "\" is not properly specified in config file. Default is ','");
					}
					
					if((metadata = metadataConfigs.get(Constants.IGNORE_FIRST_LINE)) != null){
						params.ignoreFirstLine = metadata.equals("true");
					} else {
						log.warn("\"" + Constants.IGNORE_FIRST_LINE + ":" + metadata + "\" is not properly specified in config file. Default is false");
					}
					
					if((metadata = metadataConfigs.get(Constants.COLUMNS)) != null){
						counters.write.colTotal = Integer.parseInt(metadata);
					} else {
						throw new Exception("\"" + Constants.COLUMNS + ":" + metadata + "\" is not properly specified in config file");
					}
				} else {
					throw new Exception("\"" + params.fileType + "\" is not supported in config file");
				}				
			} else {
				throw new Exception("\"" + Constants.INPUT_TYPE + "\" is not specified in config file");
			}
			
			// add config input to column definitions
			if(params.fileType.equals(Constants.CSV_FILE)){
				List<String> binName = null;
				if (params.ignoreFirstLine) {
					String line;
					BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(allFileNames.get(0)), "UTF8"));
					if(( line = br.readLine()) != null) {
						binName = Parser.getCSVRawColumns(line, params.delimiter);
						br.close();	
						if(binName.size() != counters.write.colTotal) {
							throw new Exception("Number of column in config file and datafile are mismatch."
								+ " Datafile: " + Utils.getFileName(allFileNames.get(0))
								+ " Configfile: " + Utils.getFileName(columnDefinitionFileName));
						}
					}										
				}

				//update columndefs for metadata
				for (int i = 0; i < metadataColumnDefs.size(); i++){
					if(metadataColumnDefs.get(i).staticValue) {

					}else {
						if(metadataColumnDefs.get(i).binValuePos < 0) {
							if(metadataColumnDefs.get(i).columnName == null){
								if(metadataColumnDefs.get(i).jsonPath == null){
									log.error("dynamic metadata having improper info" + metadataColumnDefs.toString()); //TODO
								} else {
									//TODO check for json_path	
								}
							} else {
								if(params.ignoreFirstLine) {
									if(binName.indexOf(metadataColumnDefs.get(i).binValueHeader) != -1){ 
										metadataColumnDefs.get(i).binValuePos = binName.indexOf(metadataColumnDefs.get(i).binValueHeader);
									} else {
										throw new Exception("binName missing in data file:" + metadataColumnDefs.get(i).binValueHeader);
									}
								}
							}
						} else {
							if(params.ignoreFirstLine)
								metadataColumnDefs.get(i).binValueHeader = binName.get(metadataColumnDefs.get(i).binValuePos);
						}
					}
					if((!metadataColumnDefs.get(i).staticValue) && (metadataColumnDefs.get(i).binValuePos < 0)) {
						throw new Exception("Information for bin mapping is missing in config file:" + metadataColumnDefs.get(i));
					}

					if(metadataColumnDefs.get(i).srcType == null ) {
						throw new Exception("Source data type is not properly mentioned:" + metadataColumnDefs.get(i));
					}

					if(metadataColumnDefs.get(i).binNameHeader == Constants.SET && !metadataColumnDefs.get(i).srcType.equals(SrcColumnType.STRING)){
						throw new Exception("Set name should be string type:" + metadataColumnDefs.get(i));
					}

					if(metadataColumnDefs.get(i).binNameHeader.equalsIgnoreCase(Constants.SET) && params.set != null) {
						throw new Exception("Set name is given both in config file and commandline. Provide only once.");
					}
				}

				//update columndefs for bins
				for (int i = 0; i < binColumnDefs.size(); i++){
					if(binColumnDefs.get(i).staticName) {
						
					}else {
						if(binColumnDefs.get(i).binNamePos < 0) {
							if(binColumnDefs.get(i).columnName == null){
								if(binColumnDefs.get(i).jsonPath == null){
									log.error("dynamic bin having improper info"); //TODO
								} else {
									//TODO check for json_path
								}
							} else {
								if(params.ignoreFirstLine) {
									if(binName.indexOf(binColumnDefs.get(i).binNameHeader) != -1) {
										binColumnDefs.get(i).binNamePos = binName.indexOf(binColumnDefs.get(i).binNameHeader);
									} else {
										throw new Exception("binName missing in data file:" + binColumnDefs.get(i).binNameHeader);
									}
								}
							}
						}else {
							if(params.ignoreFirstLine)
								binColumnDefs.get(i).binNameHeader = binName.get(binColumnDefs.get(i).binNamePos);
						}
					}
					
					
					if(binColumnDefs.get(i).staticValue) {
				
					} else {
						if(binColumnDefs.get(i).binValuePos < 0) {
							if(binColumnDefs.get(i).columnName == null){
								if(binColumnDefs.get(i).jsonPath == null){
									log.error("dynamic bin having improper info"); //TODO
								} else {
									//TODO check for json_path
								}
							} else {
								if(params.ignoreFirstLine) {
									if(binName.contains(binColumnDefs.get(i).binValueHeader)){
										binColumnDefs.get(i).binValuePos = binName.indexOf(binColumnDefs.get(i).binValueHeader);
									} else if(!binColumnDefs.get(i).binValueHeader.toLowerCase().equals(Constants.SYSTEM_TIME)) {
										throw new Exception("Wrong column name mentioned in config file:" + binColumnDefs.get(i).binValueHeader);
									}
								}
							}
						}else {
							if(params.ignoreFirstLine)
								binColumnDefs.get(i).binValueHeader = binName.get(binColumnDefs.get(i).binValuePos);
						}
						
						//check for missing entries in config file
						if(binColumnDefs.get(i).binValuePos < 0 && binColumnDefs.get(i).binValueHeader == null) {
							throw new Exception("Information missing(Value header or bin mapping) in config file:" + binColumnDefs.get(i));
						}
						
						//check for proper data type in config file.
						if(binColumnDefs.get(i).srcType == null ) {
							throw new Exception("Source data type is not properly mentioned:" + binColumnDefs.get(i));
						}
						
						//check for valid destination type
						if((binColumnDefs.get(i).srcType.equals(SrcColumnType.TIMESTAMP) || binColumnDefs.get(i).srcType.equals(SrcColumnType.BLOB)) && binColumnDefs.get(i).dstType == null ) {
							throw new Exception("Destination type is not mentioned: " + binColumnDefs.get(i));
						}
						
						//check for encoding
						if(binColumnDefs.get(i).dstType != null && binColumnDefs.get(i).encoding == null ) {
							throw new Exception("Encoding is not given for src-dst type conversion:" + binColumnDefs.get(i));
						}
						
						//check for valid encoding
						if(binColumnDefs.get(i).srcType.equals(SrcColumnType.BLOB) && !binColumnDefs.get(i).encoding.equals(Constants.HEX_ENCODING)) {
							throw new Exception("Wrong encoding for blob data:" + binColumnDefs.get(i));
						}
					}
					
					//Check static bin name mapped to dynamic bin value
					if((binColumnDefs.get(i).binNamePos == binColumnDefs.get(i).binValuePos) && (binColumnDefs.get(i).binNamePos != -1)) {
						throw new Exception("Static bin name mapped to dynamic bin value:" + binColumnDefs.get(i));
					}
					
					//check for missing entries in config file
					if(binColumnDefs.get(i).binNameHeader == null && binColumnDefs.get(i).binNameHeader.length() > Constants.BIN_NAME_LENGTH) {
						throw new Exception("Information missing binName or large binName in config file:" + binColumnDefs.get(i));
					}						
				}
			}
			
			log.info(params.toString());
			log.debug("MetadataConfig:" + metadataColumnDefs);
			log.debug("BinColumnDefs:" + binColumnDefs);
			
			// Start PrintStat thread
			statPrinter.start();
			
			// Reader pool size
			ExecutorService readerPool = Executors.newFixedThreadPool(nReaderThreads > allFileNames.size() ? allFileNames.size() : nReaderThreads);
			log.info("Reader pool size : " + nReaderThreads);
			
			// Submit all tasks to writer threadpool.
			for (String aFile : allFileNames){
				log.debug("Submitting task for: " + aFile);
				readerPool.submit(new AerospikeLoad(aFile, client, params));
			}
			
			// Wait for reader pool to complete
			readerPool.shutdown();
			log.info("Shutdown down reader thread pool");
			
			while(!readerPool.isTerminated());
			//readerPool.awaitTermination(20, TimeUnit.MINUTES);
			log.info("Reader thread pool terminated");
			
			// Wait for writer pool to complete after getting all tasks from reader pool
			writerPool.shutdown();
			log.info("Shutdown down writer thread pool");
			
			while(!writerPool.isTerminated());
			log.info("Writer thread pool terminated");

			// Print final statistic of aerospike-loader.
			log.info("Final Statistics of importer: (Succesfull Writes = " + counters.write.writeCount.get() + ", "
					+ "Errors=" + (counters.write.writeErrors.get() + counters.write.readErrors.get() + counters.write.processingErrors.get()) 
					+ "("+(counters.write.writeErrors.get()) + "-Write,"+ counters.write.readErrors.get()+ "-Read," + counters.write.processingErrors.get() +"-Processing)");
		}
		catch (Exception e) {
			log.error(e);
			if(log.isDebugEnabled()){
				e.printStackTrace();
			}
		} finally {
			// Stop statistic printer thread.
			statPrinter.interrupt();
			log.info("Aerospike loader completed");
		}
	}

	/*
	 * Constructor
	 */
	public AerospikeLoad(String fileName, AerospikeClient client, Parameters params) throws AerospikeException{
		this.client = client;
		this.fileName = fileName; 
	}

	
	/**
	 * Write usage to console.
	 */
	private static void logUsage(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		String syntax = AerospikeLoad.class.getName() + " [<options>]";
		formatter.printHelp(pw, 100, syntax, "options:", options, 0, 2, null);
		log.info(sw.toString());
	}

	/**
	 * Process a single file
	 */
	private void processFile() {
		int lineNumber = 0;
		
		log.trace("Hosts: " + this.client.getNodeNames());
		long start = System.currentTimeMillis();
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(this.fileName), "UTF8"));
			log.debug("Reading file:  " + Utils.getFileName(fileName));
			while(br.ready())
			{
				String line;
				
				//skip reading 1st line of data file
				if (params.ignoreFirstLine){
					lineNumber++;	
					String metaColumn = br.readLine();
					counters.write.recordTotal = counters.write.recordTotal - metaColumn.length();
				}
				AsWriterTask task = null;
				while((line = br.readLine()) != null){
					lineNumber++;
					log.trace("Read line " + lineNumber + " from file " + Utils.getFileName(fileName));
					
					// Throttle the read to write ratio
					while((counters.write.readCount.get() - (counters.write.writeCount.get() + counters.write.writeErrors.get())) > rwThrottle) {
						Thread.sleep(20);
					}
					if(params.fileType.equalsIgnoreCase(Constants.CSV_FILE)){
						//add 2 to handle different file size in different platform.
						task = new AsWriterTask(fileName, lineNumber, (line.length() + 1), Parser.getCSVRawColumns(line, params.delimiter), metadataColumnDefs, binColumnDefs, this.client, params, counters);
						counters.write.readerProcessed.incrementAndGet();
					} else {
						log.error("File format not supported");
					}
					log.trace("Submitting line " + lineNumber + " in file " + Utils.getFileName(fileName));
					writerPool.submit(task);
					
					counters.write.readCount.incrementAndGet();
				}
			}
			
			br.close();
			
		} catch (IOException e) {
			counters.write.readErrors.incrementAndGet();
			log.error("Error processing file: " + Utils.getFileName(fileName)+": Line: " + lineNumber);
			if(log.isDebugEnabled()){
				e.printStackTrace();
			}
		} catch (Exception e) {
			counters.write.readErrors.incrementAndGet();
			log.error("Error processing file: " + Utils.getFileName(fileName)+": Line: "+ lineNumber);
			if(log.isDebugEnabled()){
				e.printStackTrace();
			}
		} 
		long stop = System.currentTimeMillis();
		log.info(String.format("Reader completed %d-lines in %.3fsec, From file: %s", 
				lineNumber, (float)(stop-start)/1000, Utils.getFileName(fileName)));
	}
	
	public void run() {
		log.info("Processing: " + Utils.getFileName(fileName));

		try {
			log.trace("Processing File " + Utils.getFileName(fileName));
			processFile();
		} catch (Exception e) {
			log.error("Cannot process file " + Utils.getFileName(fileName));
			if(log.isDebugEnabled()){
				e.printStackTrace();
			}
		}

	}
}
