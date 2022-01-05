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
 * BinDefinition (BinName/Def, BinValu/Def) populated by parsing config file.
 * 
 */
public class BinDefinition {

	String staticName;
	String staticValue;
	ColumnDefinition nameDef;
	ColumnDefinition valueDef;
	
	public BinDefinition(
			String staticName,
			String staticValue,
			ColumnDefinition nameDef,
			ColumnDefinition valueDef

	) {
		this.staticName = staticName;
		this.staticValue = staticValue;
		this.nameDef = nameDef;
		this.valueDef = valueDef;
	}

	public String getBinStaticName() {
		return this.staticName;
	}

	public String getBinStaticValue() {
		return this.staticValue;
	}

	public ColumnDefinition getBinNameDef() {
		return this.nameDef;
	}

	public ColumnDefinition getValueDef() {
		return this.valueDef;
	}

	@Override
	public String toString() {
		return "ColumnDefinition [staticName=" + this.staticName + ", staticValue=" + this.staticValue
				+ ", nameDef=" + this.nameDef + ", valueDef=" + this.valueDef + "]";
	}
}
