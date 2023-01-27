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

import com.aerospike.client.policy.WritePolicy;
import com.aerospike.client.Host;

/**
 * Configuration data.
 */
public class Parameters {
	Host[] hosts;
	String namespace;
	WritePolicy writePolicy;
	long maxThroughput;
	long timeZoneOffset;
	int abortErrorCount;
	boolean verbose;
	boolean unorderdMaps;
	
	/**
	 * Set parameters from commandline argument. 
	 * @param hosts
	 * @param namespace
	 * @param writePolicy
	 * @param maxThroughput
	 * @param timeZoneOffset
	 * @param abortAtError
	 * @param verbose
	 * @param unorderdMaps
	 */
	protected Parameters(
			Host[] hosts,
			String namespace,
			WritePolicy writePolicy,
			long maxThroughput,
			long timeZoneOffset,
			int abortErrorCount,
			boolean verbose,
			boolean unorderdMaps
			) {
		this.hosts = hosts;
		this.namespace = namespace;
		this.writePolicy = writePolicy;
		this.maxThroughput = maxThroughput;
		this.timeZoneOffset = timeZoneOffset;
		this.abortErrorCount = abortErrorCount;
		this.verbose = verbose;
		this.unorderdMaps = unorderdMaps;
	}

	@Override
	public String toString() {
		return "Parameters:[ hosts=" + this.hosts + 
				", ns=" + this.namespace + 
				", maxThroughput=" + this.maxThroughput +
				", write-action=" + this.writePolicy.recordExistsAction.toString() +
				", timeZoneOffset=" + this.timeZoneOffset +
				", abortErrorCount=" + this.abortErrorCount + 
				", verbose=" + this.verbose + 
				", unorderdMaps=" + this.unorderdMaps + "]";
	}
}
