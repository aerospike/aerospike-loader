/*******************************************************************************
 * Copyright 2017 by Aerospike.
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

import com.aerospike.client.Host;
import com.aerospike.client.policy.RecordExistsAction;
import com.aerospike.client.policy.WritePolicy;

public class Utils {
	private static Logger log = Logger.getLogger(Utils.class);

	/**
	 * Get list of data file names from given files(filenames, directories).
	 * 
	 * @param  files This includes filenames and directory name.
	 * @return       Return list of absolute filenames.
	 */
	protected static List<String> getFileNames(String[] files) {
		List<String> dataFileNames = new ArrayList<String>();
		// Expand directories
		for (String fileName : files) {
			File file = new File(fileName);
			if (!file.exists()) {
				log.error("File: " + fileName + " does not exist.");
				continue;
			}
			if (file.isDirectory()) {
				File[] subFiles = file.listFiles(new FileFilter() {
										public boolean accept(File file) {
											return !file.getName().endsWith(".");
										}
									});
				for (File subFile : subFiles) {
					dataFileNames.add(subFile.getAbsolutePath());
				}
			} else if (!file.getName().endsWith(".")) {
				dataFileNames.add(file.getAbsolutePath());
			}
		}

		return dataFileNames;
	}
	
	/**
	 * Get absolute file name from file path.
	 */
	protected static String getFileName(String filePath) {
		return (new File(filePath).getName());
	}

	/**
	 * Parse command line parameters.
	 * 
	 * @param  cl          Commandline arguments
	 * @return Parameters  Group of abstract Params.
	 * @throws Exception
	 */
	protected static Parameters parseParameters(CommandLine cl) throws Exception {

		String portString = cl.getOptionValue("p", "3000");
		int port = Integer.parseInt(portString);

		Host[] hosts;
		if (cl.hasOption("hosts")) {
			hosts = Host.parseHosts(cl.getOptionValue("hosts"), port);
		} else {
			hosts = new Host[1];
			hosts[0] = new Host("127.0.0.1", port);
		}

		String namespace = cl.getOptionValue("n", "test");
		
		long maxThroughput = Long.parseLong(cl.getOptionValue("g", "0"));

		if (cl.hasOption("tz")) {
			if (!Utils.checkTimeZoneID(cl.getOptionValue("tz")))
				log.error("TimeZone given is not a valid ID");
		}
		String timeZone = cl.getOptionValue("TZ", TimeZone.getDefault().getID());
		TimeZone source = TimeZone.getTimeZone(timeZone);
		TimeZone local = TimeZone.getDefault();
		long timeZoneOffset = local.getRawOffset() - source.getRawOffset();

		String errorCount = cl.getOptionValue("ec", "0");
		int abortErrorCount = Integer.parseInt(errorCount);

		String timeToLive = cl.getOptionValue("e", "-1");
		int ttl = (Integer.parseInt(timeToLive));

		String timeout = cl.getOptionValue("T", "0");
		int timeout_int = (Integer.parseInt(timeout));

		boolean sendKey = false;
		if (cl.hasOption("uk")) {
			sendKey = true;
		}

		String writeAction = cl.getOptionValue("wa", "UPDATE");

		WritePolicy writePolicy = new WritePolicy();
		writePolicy.recordExistsAction = RecordExistsAction.valueOf(writeAction.toUpperCase());
		writePolicy.timeout = timeout_int;
		writePolicy.expiration = ttl;
		writePolicy.sendKey = sendKey;


		boolean verbose = false;
		if (cl.hasOption("v")) {
			verbose = true;
		}

		return new Parameters(hosts, namespace, writePolicy, maxThroughput, timeZoneOffset, abortErrorCount, verbose);
	}

	/**
	 * Check existence of user provided timezone.
	 * 
	 * @param  timeZone timezone_id.
	 * @return          true if timezone_id is a valid id else false.
	 */
	protected static boolean checkTimeZoneID(String timeZone) {
		boolean sourceTZ = false;
		for (String timezone : TimeZone.getAvailableIDs()) {
			if (timezone.equalsIgnoreCase(timeZone)) {
				sourceTZ = true;
			}
		}
		return sourceTZ;
	}
}
