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

/**
 * Constants used for this tool and keywords for Json config file.
 * @author Aerospike
 *
 */
public class Constants {

	// Config keywords
	public static final String VERSION				= "version";
	public static final String DSV_CONFIG			= "dsv_config";
	public static final String DELIMITER			= "delimiter";
	public static final String N_COLUMN				= "n_columns_datafile";
	public static final String HEADER_EXIST	        = "header_exist";
	
	public static final String KEY					= "key";
	public static final String SET					= "set";
	public static final String BIN					= "bin";
	public static final String MAPPINGS				= "mappings";
	public static final String SECONDARY_MAPPING	= "secondary_mapping";
	
	public static final String BINLIST				= "bin_list";
	public static final String NAME					= "name";
	public static final String VALUE				= "value";
	public static final String COLUMN_POSITION		= "column_position";
	public static final String COLUMN_NAME			= "column_name";
	public static final String TYPE					= "type";
	public static final String DST_TYPE				= "dst_type";
	public static final String ENCODING				= "encoding";
	public static final String HEX_ENCODING			= "hex";
	public static final String REMOVE_PREFIX		= "remove_prefix";
	public static final String JSON_PATH			= "json_path";

	// Constants
	public static final int BIN_NAME_LENGTH			= 14;
	public static final int SET_NAME_LENGTH			= 63;

	public static final int MAX_THREADS				= 128;
	
	public static final int RW_THROTTLE_LIMIT		= 10000;
	public static final char DOUBLE_QUOTE_DELEMITER	= '"';

	// keywords to insert extra information specified by user. Reserved
	public static final String SYSTEM_TIME			= "system_time";

	public static final String ABORT_AT_ERROR		= "abort_at_error";
	public static final int MajorV					= 2;
	public static final int MinorV					= 0;
	
	
}
