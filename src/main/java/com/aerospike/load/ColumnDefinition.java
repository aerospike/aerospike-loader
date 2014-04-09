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


/**
 * List of source data types
 *
 */
enum SrcColumnType {
	INTEGER, STRING, BLOB, LIST, MAP, JSON, TIMESTAMP, FLOAT;
}
/**
 * List of datatypes supported by Aerospike
 *
 */
enum DstColumnType {
	INTEGER, STRING, BLOB, LIST, MAP;
}
/**
 * Column Definition class for data file.
 *
 */
public class ColumnDefinition {
	String binNameHeader;
	String binValueHeader;
	boolean staticName;
	boolean staticValue;
	SrcColumnType srcType;
	DstColumnType dstType;
	String encoding;
	int binNamePos;
	int binValuePos;
	String columnName;
	String jsonPath;
	
	public ColumnDefinition(
			String binNameHeader,
			String binValueHeader,
			boolean staticName,
			boolean staticValue,
			String srcType,
			String dstType,
			String encoding,
			int binNamePos,
			int binValuePos,
			String columnName,
			String jsonPath
	) {
		this.binNameHeader = binNameHeader;
		this.binValueHeader = binValueHeader;
		this.staticName = staticName;
		this.staticValue = staticValue;
		setSrcType(srcType);
		setDstType(dstType);
		setEncoding(encoding);
		this.binNamePos = binNamePos;
		this.binValuePos = binValuePos;
		this.columnName = columnName;
		this.jsonPath = jsonPath;
	}
	public String getBinNameHeader() {
		return binNameHeader;
	}
	public String getBinValueHeader() {
		return binValueHeader;
	}
	public SrcColumnType getSrcType() {
		return srcType;
	}
	public DstColumnType getDstType() {
		return dstType;
	}
	public String getEncoding() {
		return encoding;
	}
	public int getBinNamePos() {
		return binNamePos;
	}
	public int getBinValuePos() {
		return binValuePos;
	}
	public String getColumnName() {
		return columnName;
	}
	public String getJsonPath() {
		return jsonPath;
	}
	public void setEncoding(String type) {
		this.encoding = type;
	}
	public void setSrcType(String type) {
		if ("string".equalsIgnoreCase(type)){
			this.srcType = SrcColumnType.STRING;
		} else if ("integer".equalsIgnoreCase(type)){
			this.srcType = SrcColumnType.INTEGER;
		} else if ("blob".equalsIgnoreCase(type)){
			this.srcType = SrcColumnType.BLOB;
		} else if ("list".equalsIgnoreCase(type)){
			this.srcType = SrcColumnType.LIST;
		} else if ("map".equalsIgnoreCase(type)){
			this.srcType = SrcColumnType.MAP;
		} else if ("json".equalsIgnoreCase(type)){
			this.srcType = SrcColumnType.JSON;
		} else if ("timestamp".equalsIgnoreCase(type)){
			this.srcType = SrcColumnType.TIMESTAMP;
		} else if ("float".equalsIgnoreCase(type)){
			this.srcType = SrcColumnType.FLOAT;
		}
	}

	public void setDstType(String type) {
		if ("string".equalsIgnoreCase(type)){
			this.dstType = DstColumnType.STRING;
		} else if ("integer".equalsIgnoreCase(type)){
			this.dstType = DstColumnType.INTEGER;
		} else if ("blob".equalsIgnoreCase(type)){
			this.dstType = DstColumnType.BLOB;
		} else if ("list".equalsIgnoreCase(type)){
			this.dstType = DstColumnType.LIST;
		} else if ("map".equalsIgnoreCase(type)){
			this.dstType = DstColumnType.MAP;
		}
	}
	

	@Override
	public String toString() {
		return "ColumnDefinition: [ BinNameHeader:"+this.binNameHeader + 
				", BinValueHeader:" + this.binValueHeader +
				", srcType:"+this.srcType+
				", dstType:"+this.dstType+
				", Encoding:"+this.encoding+
				", binNamePos:"+this.binNamePos+
				", binValuePos:" + this.binValuePos + " ]";
	}
}
