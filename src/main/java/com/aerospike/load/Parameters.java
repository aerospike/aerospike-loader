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

import com.aerospike.client.policy.WritePolicy;

/**
 * Configuration data.
 */
public class Parameters {
	String host;
	int port;
	String namespace;
	String set;
	int ttl;
	String fileType;
	char delimiter;
	long timeZoneOffset;
	boolean ignoreFirstLine;
	int abortErrorCount;
	WritePolicy writePolicy;
	
	/**
	 * Set parameters from commandline argument. 
	 * @param host
	 * @param port
	 * @param namespace
	 * @param set
	 * @param ttl
	 * @param fileType
	 * @param delimiter
	 * @param timeZoneOffset
	 * @param ignoreFirstLine
	 * @param abortAtError
	 */
	protected Parameters(
			String host,
			int port,
			String namespace,
			String set,
			int ttl,
			String fileType,
			char delimiter,
			long timeZoneOffset,
			boolean ignoreFirstLine,
			int abortErrorCount,
			WritePolicy writePolicy
			) {
		this.host = host;
		this.port = port;
		this.namespace = namespace;
		this.set = set;
		this.ttl = ttl;
		this.fileType = fileType;
		this.delimiter = delimiter;
		this.timeZoneOffset = timeZoneOffset;
		this.ignoreFirstLine = ignoreFirstLine;
		this.abortErrorCount = abortErrorCount;
		this.writePolicy = writePolicy;
	}

	@Override
	public String toString() {
		return "Parameters:[ host=" + host + 
				", port=" + port + 
				", ns=" + namespace + 
				", set=" + set +
				", ttl=" + ttl +
				", timeout=" + writePolicy.timeout +
				", write-action=" + writePolicy.recordExistsAction.toString() +
				", fileType=" + fileType +
				", delimiter=" + delimiter +
				", timeZoneOffset=" + timeZoneOffset +
				", ignoreFirstLine=" + ignoreFirstLine +
				", abortErrorCount=" + abortErrorCount + "]";
	}
}
