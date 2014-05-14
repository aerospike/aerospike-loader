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

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.cli.CommandLine;
import org.apache.log4j.Logger;

import com.aerospike.client.policy.RecordExistsAction;
import com.aerospike.client.policy.WritePolicy;

public class Utils {
	private static Logger	log = Logger.getLogger(Utils.class);
	
	/**
	 * Get list of data file names 
	 * @param files 
	 * @return
	 */
	protected static List<String> getFileNames(String[] files) {
		List<String> allFileNames = new ArrayList<String>();
		// Expand directories
		for (String fileName : files){
			File file = new File(fileName);
			if (!file.exists()){
				log.error("File " + fileName + " does not exist");
				continue;
			}
			if (file.isDirectory()){
				File[] subFiles = file.listFiles(new FileFilter() {
						public boolean accept(File file) {
							return !file.getName().endsWith(".");
						}
				});
				for (File subFile : subFiles){
					allFileNames.add(subFile.getAbsolutePath());
				}
			} else if (!file.getName().endsWith(".")) {
				allFileNames.add(file.getAbsolutePath());
			}
		}
		
		return allFileNames;
	}
	
	/**
	 * Get file name from file path
	 * @param filePath
	 * @return
	 */
	protected static String getFileName(String filePath) {
		return (new File(filePath).getName());
	}
	
	/**
	 * Parse command line parameters.
	 * @param cl Commandline arguments
	 * @return Parameters
	 * @throws Exception
	 */
	protected static Parameters parseParameters(CommandLine cl) {

		String host = cl.getOptionValue("h", "127.0.0.1");
		String portString = cl.getOptionValue("p", "3000");
		int port = Integer.parseInt(portString);
		String namespace = cl.getOptionValue("n","test");
		String set = cl.getOptionValue("s", null);

		String timeToLive = cl.getOptionValue("et", "-1");
		int ttl = (Integer.parseInt(timeToLive));
		
		String timeout = cl.getOptionValue("tt", "0");
		int to = (Integer.parseInt(timeout));

		//Get timezone offset
		//get user input for timeZone and check for valid ID
		if (cl.hasOption("T")) {
			if(!Utils.checkTimeZoneID(cl))
				log.error("TimeZone given is not a valid ID");
		}
		String timeZone = cl.getOptionValue("T", TimeZone.getDefault().getID());
		TimeZone source = TimeZone.getTimeZone(timeZone);
		TimeZone local = TimeZone.getDefault();

		long timeZoneOffset = local.getRawOffset() - source.getRawOffset();

		String errorCount = cl.getOptionValue("ec", "0");
		int abortErrorCount = Integer.parseInt(errorCount);

		String writeAction = cl.getOptionValue("wa", "UPDATE");
		WritePolicy writePolicy = new WritePolicy();
		writePolicy.recordExistsAction = RecordExistsAction.valueOf(writeAction.toUpperCase());
		writePolicy.timeout = to;

		char delimiter = ',';
		boolean ignoreFirstLine = true;
		
		boolean verbose = false;
		if (cl.hasOption("v")) {
			verbose = true;
		}

		return new Parameters(host, port, namespace, set, ttl, Constants.CSV_FILE, delimiter, timeZoneOffset, ignoreFirstLine, verbose, abortErrorCount, writePolicy);
	}
	
	/**
	 * Check existence of user provided timezone
	 * @param cl
	 * @return
	 */
	protected static boolean checkTimeZoneID(CommandLine cl) {
		String timeZone = cl.getOptionValue("tz");
		boolean sourceTZ = false;
		for (String timezone : TimeZone.getAvailableIDs()){ 
			if( timezone.equalsIgnoreCase(timeZone)){
				sourceTZ = true;
			}
		}		
		return sourceTZ;
	}	
}
