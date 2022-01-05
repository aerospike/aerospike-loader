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
 * List of source data types
 *
 */
enum SrcColumnType {
	INTEGER, STRING, BLOB, GEOJSON, JSON, TIMESTAMP, FLOAT;
}
/**
 * List of datatypes supported by Aerospike
 *
 */
enum DstColumnType {
	INTEGER, STRING, BLOB, LIST, MAP, GEOJSON;
}

/**
 * Column Definition class for data file.
 * It has same params as column_def(key_def, set_def, Bin.name_def, Bin.Value_def)
 * section in config file.
 *
 */
public class ColumnDefinition {

	int columnPos;
	String columnName;
	SrcColumnType srcType;
	DstColumnType dstType;
	String encoding;
	String removePrefix;
	String jsonPath;
	
	public ColumnDefinition(
			int columnPos,
			String columnName,
			String srcType,
			String dstType,
			String encoding,
			String jsonPath,
			String removePrefix
	) {

		this.columnPos = columnPos;
		this.columnName = columnName;
		this.jsonPath = jsonPath;
		this.removePrefix = removePrefix;
		this.encoding = encoding;
		
		setSrcType(srcType);
		setDstType(dstType);
	}

	public void setSrcType(String type) {
		if ("string".equalsIgnoreCase(type)) {
			this.srcType = SrcColumnType.STRING;
		} else if ("integer".equalsIgnoreCase(type)) {
			this.srcType = SrcColumnType.INTEGER;
		} else if ("blob".equalsIgnoreCase(type)) {
			this.srcType = SrcColumnType.BLOB;
		} else if ("geojson".equalsIgnoreCase(type)) {
			this.srcType = SrcColumnType.GEOJSON;
		} else if ("json".equalsIgnoreCase(type)) {
			this.srcType = SrcColumnType.JSON;
		} else if ("timestamp".equalsIgnoreCase(type)) {
			this.srcType = SrcColumnType.TIMESTAMP;
		} else if ("float".equalsIgnoreCase(type)) {
			this.srcType = SrcColumnType.FLOAT;
		}
	}

	public void setDstType(String type) {
		if ("string".equalsIgnoreCase(type)) {
			this.dstType = DstColumnType.STRING;
		} else if ("integer".equalsIgnoreCase(type)) {
			this.dstType = DstColumnType.INTEGER;
		} else if ("blob".equalsIgnoreCase(type)) {
			this.dstType = DstColumnType.BLOB;
		} else if ("list".equalsIgnoreCase(type)) {
			this.dstType = DstColumnType.LIST;
		} else if ("map".equalsIgnoreCase(type)) {
			this.dstType = DstColumnType.MAP;
		} else if ("geojson".equalsIgnoreCase(type)) {
			this.dstType = DstColumnType.GEOJSON;
		}
	}
	@Override

	public String toString() {
		return "ColumnDefinition [columnPos=" + columnPos + ", columnName=" + columnName
				+ ", srcType=" + srcType + ", dstType=" + dstType + ", encoding=" + encoding + ", removePrefix=" + removePrefix
				+ ", jsonPath=" + jsonPath + "]";
	}
}
