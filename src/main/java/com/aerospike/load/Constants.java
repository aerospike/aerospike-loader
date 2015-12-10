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
 * Constants used for this tool and keywords for Json config file.
 * @author jyoti
 *
 */
public class Constants {

	public static final String KEY					= "key";
	public static final String SET					= "set";
	public static final String CSV_FILE				= "csv";
	
	public static final int BIN_NAME_LENGTH			= 14;
	public static final int MAX_THREADS				= 128;
	
	public static final int READLOAD				= 10000;
	
	public static final char COMMA_DELEMITER		= ',';
	public static final char DOUBLE_QOUTE_DELEMITER	= '"';
	public static final String LIST_DELEMITER		= "::";
	public static final String MAP_DELEMITER		= "::";
	public static final String MAPKEY_DELEMITER		= "=";
	//config keywords
	public static final String VERSION				= "version";
	public static final String INPUT_TYPE			= "input_type";
	public static final String CSV_STYLE			= "csv_style";
	public static final String DELIMITER			= "delimiter";
	public static final String COLUMNS				= "n_columns_datafile";
	public static final String IGNORE_FIRST_LINE	= "ignore_first_line";
	
	public static final String ABORT_AT_ERROR		= "abort_at_error";
	public static final String BINLIST				= "binlist";
	public static final String NAME					= "name";
	public static final String VALUE				= "value";
	public static final String COLUMN_POSITION		= "column_position";
	public static final String COLUMN_NAME			= "column_name";
	public static final String JSON_PATH			= "json_path";
	public static final String TYPE					= "type";
	public static final String DST_TYPE				= "dst_type";
	public static final String ENCODING				= "encoding";
	public static final String HEX_ENCODING			= "hex";
	
	//keywords to insert extra information specified by user
	public static final String SYSTEM_TIME			= "system_time";

	public static final int MajorV					= 1;
	public static final int MinorV					= 1;
}
