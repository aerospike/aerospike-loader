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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.AerospikeException;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.ResultCode;
import com.aerospike.client.policy.WritePolicy;

public class AsWriterTask implements Callable<Integer> {

	private AerospikeClient client;
	private WritePolicy writePolicy;
	private String nameSpace;
	private String set;
	private long timeZoneOffset; 
	private String fileName;
	private int abortErrorCount;
	
	private Key key;
	private Bin[] bins;
	private List<ColumnDefinition> metadataMapping = new ArrayList<ColumnDefinition>();
	private List<ColumnDefinition> binMapping = new ArrayList<ColumnDefinition>();
	private List<String> columns;
	private Counter counters;
	private int lineNumber;
	private int lineSize;
	private JSONParser jsonParser;

	private static Logger log = Logger.getLogger(AsWriterTask.class);

	/**
	 * AsWriterTask writes data into Aerospike server. In processLine() function using
	 * column definition and each record in list format of bins it maps the bin name to
	 * its value. After processing it writes the data to Aerospike server using writeToAS().
	 * 
	 * @param fileName Name of the data file
	 * @param lineNumber Line number in the file fileName
	 * @param lineSize Size of the line to keep track of record processed 
	 * @param columns List of column separated entries in this lineNumber
	 * @param metadataColumnDefs Column Definitions for special columns like key,set
	 * @param binColumnDefs Column Definitions for bin columns
	 * @param client Aerospike client
	 * @param params User given parameters
	 * @param counters Counter for stats
	 */
	public AsWriterTask( String fileName, int lineNumber, int lineSize, List<String> columns, List<ColumnDefinition> metadataColumnDefs, List<ColumnDefinition> binColumnDefs, AerospikeClient client, Parameters params, Counter counters){
		this.columns = columns;
		this.client = client;
		this.writePolicy = params.writePolicy;
		this.writePolicy.expiration = params.ttl;
		this.timeZoneOffset = params.timeZoneOffset;
		this.nameSpace = params.namespace;
		this.set = params.set;
		this.metadataMapping = metadataColumnDefs;
		this.binMapping = binColumnDefs;
		this.counters = counters;
		this.fileName = fileName;
		this.abortErrorCount = params.abortErrorCount;
		this.lineNumber = lineNumber;
		this.lineSize = lineSize;
	}

	/**
	 * writes a record to the Aerospike Cluster
	 * @throws AerospikeException
	 */
	private int writeToAs() {
		int value = 0;
		long errorTotal = 0;
		if (this.client != null){
			try {
				this.client.put(this.writePolicy, this.key, this.bins);
				counters.write.recordProcessed.addAndGet(this.lineSize);
				counters.write.writeCount.getAndIncrement();	
				log.trace("Wrote line " + lineNumber + " Key: " + this.key.userKey + " to Aerospike");
				value = 1;
			} catch (AerospikeException ae) {
				log.error("File:" + Utils.getFileName(this.fileName) + " Line:" + lineNumber + " Write Error:" + ae.getResultCode());
				if(log.isDebugEnabled()){
					ae.printStackTrace();
				}
				counters.write.recordProcessed.addAndGet(this.lineSize);
				counters.write.writeErrors.getAndIncrement();
				errorTotal = (counters.write.readErrors.get() + counters.write.writeErrors.get() + counters.write.processingErrors.get());
				
				switch(ae.getResultCode()) {				
				case ResultCode.TIMEOUT:
					counters.write.writeTimeouts.getAndIncrement();
					break ;				
				case ResultCode.KEY_EXISTS_ERROR:
					counters.write.writeKeyExists.getAndIncrement();
					break ;					
				default:					
				}
				if((this.abortErrorCount != 0 && this.abortErrorCount < errorTotal) ) {
					System.exit(-1);
				}
			} catch (Exception e) {
				log.error("File:" + Utils.getFileName(this.fileName) + " Line:" + lineNumber + " Write Error:" + e.getMessage());
				if(log.isDebugEnabled()){
					e.printStackTrace();
				}
				counters.write.recordProcessed.addAndGet(this.lineSize);
				counters.write.writeErrors.getAndIncrement();
				errorTotal = (counters.write.readErrors.get() + counters.write.writeErrors.get() + counters.write.processingErrors.get());
				if(this.abortErrorCount != 0 && this.abortErrorCount < errorTotal) {
					System.exit(-1);
				}
			}
		} 
		return value;
	}

	private boolean processLine() {
		log.debug("processing  File:line " + Utils.getFileName(fileName) + this.lineNumber );
		bins = new Bin[this.binMapping.size()];
		int binIndex = 0;
		boolean lineProcessed = false;
		long errorTotal = 0;
		try {
			if(columns.size() != counters.write.colTotal) {
				if (columns.size() < counters.write.colTotal)
				{
					log.error("Number of column in data file is less than number of column in config file. File:Line " + Utils.getFileName(this.fileName) + ":" + lineNumber);
					System.exit(-1);
				} else {
					throw new ParseException(lineNumber);
				}
			}
			
			//retrieve set name first
			for (ColumnDefinition metadataColumn : this.metadataMapping) {
				String metadataRawText = this.columns.get(metadataColumn.getBinValuePos());
				if(metadataColumn.getBinNameHeader().equalsIgnoreCase(Constants.SET))
					this.set = metadataRawText;				
			}
			
			// use set name to create key
			for(ColumnDefinition metadataColumn : this.metadataMapping){
				String metadataRawText = this.columns.get(metadataColumn.getBinValuePos());
				if(metadataColumn.getBinNameHeader().equalsIgnoreCase(Constants.KEY)){
					if (metadataColumn.getSrcType() == SrcColumnType.INTEGER){
						Integer integer = Integer.parseInt(metadataRawText); 
						this.key = new Key(this.nameSpace, this.set, integer);
					} else {
						this.key = new Key(this.nameSpace, this.set, metadataRawText);
					}					
				}	
			}
			
			for (ColumnDefinition binColumn : this.binMapping){
				Bin bin = null;
				
				if (!binColumn.staticName) {
					binColumn.binNameHeader = this.columns.get(binColumn.binNamePos);
				}

				if (!binColumn.staticValue) {
					String binRawText = this.columns.get(binColumn.getBinValuePos());
					switch (binColumn.getSrcType()) {
					case INTEGER:
						Integer integer = Integer.parseInt(binRawText);
						bin = new Bin(binColumn.getBinNameHeader(), integer);
						break;
					case STRING:
						bin = new Bin(binColumn.getBinNameHeader(), binRawText);
						break;
					case BLOB:
						if (binColumn.encoding.equalsIgnoreCase("hex"))
							bin = new Bin(binColumn.getBinNameHeader(),
									this.toByteArray(binRawText)); //TODO
						break;
					case LIST:
						/*
						 * Assumptions
						 * 1. Items are separated by a colon ':'
						 * 2. Item value will be a string
						 * 
						 * No support for nested maps or nested lists
						 * 
						 */
						List<String> list = new ArrayList<String>();
						String[] listValues = binRawText.split(":", -1);
						if (listValues.length > 0) {
							for (String value : listValues) {
								list.add(value);
							}
							bin = Bin
									.asList(binColumn.getBinNameHeader(), list);
						} else {
							bin = null;
							log.error("Error: Cannot parse to a list: "
									+ binRawText);
						}
						break;
					case MAP:
						/*
						 * Asumptions:
						 * 1. Items are separated by a colon ':'
						 * 2. Name value pairs are separated by equals '='
						 * 3. Map key is a string
						 * 4. Map value will be a string
						 * 
						 * No support for nested maps or nested lists
						 * 
						 */
						Map<String, Object> map = new HashMap<String, Object>();
						String[] mapValues = binRawText.split(":", -1);
						if (mapValues.length > 0) {
							for (String value : mapValues) {
								String[] kv = value.split("=");
								if (kv.length != 2)
									log.error("Error: Cannot parse map <k,v> using: "
											+ kv);
								else
									map.put(kv[0], kv[1]);
							}
							bin = Bin.asMap(binColumn.getBinNameHeader(), map);
						} else {
							bin = null;
							log.error("Error: Cannot parse to a map: "
									+ binRawText);
						}
						break;
					case JSON:
						try {
							if (jsonParser == null)
								jsonParser = new JSONParser();
							Object obj = jsonParser.parse(binRawText);
							if (obj instanceof JSONArray) {
								JSONArray jsonArray = (JSONArray) obj;
								bin = Bin.asList(binColumn.getBinNameHeader(),
										jsonArray);
							} else {
								JSONObject jsonObj = (JSONObject) obj;
								bin = Bin.asMap(binColumn.getBinNameHeader(),
										jsonObj);
							}
						} catch (ParseException e) {
							log.error("Failed to parse JSON", e);
						}
						break;
					case TIMESTAMP:
						DateFormat format = new SimpleDateFormat(
								binColumn.getEncoding());
						try {
							Date formatDate = format.parse(binRawText);
							long miliSecondForDate = formatDate.getTime()
									- timeZoneOffset;
							bin = new Bin(binColumn.getBinNameHeader(),
									miliSecondForDate / 1000);
							log.trace("Date format:" + binRawText
									+ " in seconds:" + miliSecondForDate / 1000);
						} catch (java.text.ParseException e) {
							e.printStackTrace();
						}
						break;

					default:

					}
				} else {
					bin = new Bin(binColumn.getBinNameHeader(), binColumn.getBinValueHeader());
				}
				bins[binIndex] = bin;
				binIndex++;
				}
			lineProcessed = true;
			log.trace("Formed key and bins for line " + lineNumber + " Key: " + this.key.userKey);
		} catch (AerospikeException ae) {
			log.error("File:" + Utils.getFileName(this.fileName) + " Line:" + lineNumber + " Aerospike Bin processing Error:" + ae.getResultCode());
			if(log.isDebugEnabled()){
				ae.printStackTrace();
			}
			counters.write.processingErrors.getAndIncrement();
			counters.write.recordProcessed.addAndGet(this.lineSize);
			errorTotal = (counters.write.readErrors.get() + counters.write.writeErrors.get() + counters.write.processingErrors.get());
			if(this.abortErrorCount != 0 && this.abortErrorCount < errorTotal) {
				System.exit(-1);
			}
		} catch (ParseException pe) {
			log.error("File:" + Utils.getFileName(this.fileName) + " Line:" + lineNumber + " Parsing Error:" + pe);
			if(log.isDebugEnabled()){
				pe.printStackTrace();
			}
			counters.write.processingErrors.getAndIncrement();
			counters.write.recordProcessed.addAndGet(this.lineSize);
			errorTotal = (counters.write.readErrors.get() + counters.write.writeErrors.get() + counters.write.processingErrors.get());
			if(this.abortErrorCount != 0 && this.abortErrorCount < errorTotal) {
				System.exit(-1);
			}
		} catch (Exception e) {
			log.error("File:" + Utils.getFileName(this.fileName) + " Line:" + lineNumber + " Unknown Error:" + e);
			if(log.isDebugEnabled()){
				e.printStackTrace();
			}			
			counters.write.processingErrors.getAndIncrement();
			counters.write.recordProcessed.addAndGet(this.lineSize);
			errorTotal = (counters.write.readErrors.get() + counters.write.writeErrors.get() + counters.write.processingErrors.get());
			if(this.abortErrorCount != 0 && this.abortErrorCount < errorTotal) {
				System.exit(-1);
			}
		}
		return lineProcessed;
	}

	public byte[] toByteArray(String s) {
		
		if ((s.length() % 2) != 0) {
			log.error("blob exception: " + s);
			throw new IllegalArgumentException("Input hex formated string must contain an even number of characters");
		}
	
		int len = s.length();
		byte[] data = new byte[len / 2];
		
		try {
			for (int i = 0; i < len; i += 2) {
				data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
			}
		} catch (Exception e) {
			log.info("blob exception:" + e.toString());
		}
		return data;
	}

	public Integer call() throws Exception {
		boolean processLine = false;
		
		try {			
			processLine = processLine();
		} catch (Exception e) {
			log.error("Error parsing File:line - " + Utils.getFileName(fileName) + ":" + (this.lineNumber+1));
			log.debug(e);
		}
		
		//If processLine() succeeds, write to aerospike
		if (processLine) {
			return writeToAs();
		} else {
			return 0;
		}
	}
}
