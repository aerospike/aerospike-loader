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

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Counter class to keep track of statistic of Loader.
 * @author jyoti
 *
 */
public class Counter {
	
	public static class Current {
		AtomicInteger readCount = new AtomicInteger();
		AtomicInteger readErrors = new AtomicInteger();
		AtomicInteger processingErrors = new AtomicInteger();
		AtomicInteger writeCount = new AtomicInteger();
		AtomicInteger writeErrors = new AtomicInteger();
		AtomicInteger writeTimeouts = new AtomicInteger();
		AtomicInteger writeKeyExists = new AtomicInteger();
		
		AtomicInteger readerProcessed = new AtomicInteger();
		int colTotal;
		long recordTotal;
		AtomicLong recordProcessed = new AtomicLong();
	}
		
	Current write = new Current();
	static int numCol;
}
