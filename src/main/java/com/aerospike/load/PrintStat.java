/*******************************************************************************
 * Copyright 2022 by Aerospike.
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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



/**
 * Prints Progress of Aerospike-Loader
 *
 */
public class PrintStat implements Runnable{
	private static Logger	log = LogManager.getLogger(PrintStat.class);
	private static final SimpleDateFormat SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private Counter counters;

	PrintStat(Counter counters) {
		this.counters = counters;
	}

	/**
	 * Print write_count, TPS, errors(timeouts, keyExists, otherWrites, readErrors, processing),
	 * skipped, noBins, progress of loading. 
	 */
	@Override
	public void run() {

		int tps = 0;
		int rtps = 0;
		int nWrites = 0;
		int nErrors = 0;
		int nReader = 0;
		int progress = 0;
		while (!Thread.currentThread().isInterrupted()) {

			if (log.isDebugEnabled()) {
				Runtime runtime = Runtime.getRuntime();
				int mb = 1024 * 1024;
				log.debug("Used Memory: " + (runtime.totalMemory() - runtime.freeMemory()) / mb + " Free Memory: "
						+ runtime.freeMemory() / mb + " Total Memory: " + runtime.totalMemory() / mb + " Max Memory: "
						+ runtime.maxMemory() / mb);
			}
			// Get current time
			long time = System.currentTimeMillis();
			String date = SimpleDateFormat.format(new Date(time));

			// Calculate progress
			if (counters.write.bytesTotal != 0)
				progress = (int) ((counters.write.bytesProcessed.get() * 100) / counters.write.bytesTotal);

			// Calculate transaction per second and store current count
			tps = (	counters.write.writeCount.get() + 
					counters.write.writeErrors.get() + 
					counters.write.readErrors.get() + 
					counters.write.processingErrors.get()) - 
					(nWrites + nErrors);
			
			nWrites = counters.write.writeCount.get();
			nErrors = (counters.write.writeErrors.get() +
					counters.write.readErrors.get() +
					counters.write.processingErrors.get());
			
			rtps = counters.write.readCount.get() - nReader;
			nReader = counters.write.readCount.get();

			log.debug(date.toString() + ": Read/process tps:" + rtps);
			// Print stats
			log.info(date.toString() + " load(Write count=" + counters.write.writeCount.get() + 
					" tps=" + tps +
					" Errors=" + nErrors +
					" (Timeout:"+counters.write.writeTimeouts.get()+" KeyExists:" + counters.write.writeKeyExists.get() + 
					" othersWrites:" + (counters.write.writeErrors.get() - counters.write.writeTimeouts.get()-counters.write.writeKeyExists.get() ) +  
					" ReadErrors:" + counters.write.readErrors.get() +
					" Processing:" + counters.write.processingErrors.get() + ")" +
					" Skiped (NullKey:" + counters.write.keyNullSkipped.get() + " NoBins:" + counters.write.noBinsSkipped + ")" +
					" Progress:" + progress + "%");
			
			// Wait for 1 second
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

}
