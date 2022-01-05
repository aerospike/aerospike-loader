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

/**
 * 
 * @author Aerospike
 * 
 * MetaDefinition (KeyDefinition/SetDefinition) populated by parsing config file.
 * 
 */
public class MetaDefinition {
	String staticName;
	ColumnDefinition nameDef;
	
	public MetaDefinition(String staticName, ColumnDefinition nameDef) {
		this.staticName = staticName;
		this.nameDef = nameDef;
	}

	public String getBinStaticName() {
		return this.staticName;
	}

	public ColumnDefinition getBinNameDef() {
		return this.nameDef;
	}

	@Override
	public String toString() {
		return "ColumnDefinition [staticName=" + this.staticName + ", nameDef=" + this.nameDef + "]";
	}
}
