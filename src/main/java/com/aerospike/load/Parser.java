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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Parser class to parse different formated files.
 *
 */
public class Parser {
	private static Logger log = Logger.getLogger(Parser.class);
	
	
	/**
	 * Process column definitions in JSON formated file and create two lists for metadata and bindata and one list for metadataLoader
	 * @param file Config file name
	 * @param metadataLoader Map of metadata for loader to use, given in config file
	 * @param metadataColumnDefs List of column definitions for metadata of bins like key,set 
	 * @param binColumnDefs List of column definitions for bins
	 * @param params User given parameters
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public static boolean processJSONColumnDefinitions(File file, HashMap<String, String> metadataConfigs, List<ColumnDefinition> metadataColumnDefs, List<ColumnDefinition> binColumnDefs, Parameters params ) {
		boolean processConfig = false;
		if (params.verbose) {
			log.setLevel(Level.DEBUG);				
		}
		try {
			// Create parser object
			JSONParser jsonParser = new JSONParser();
			
			// Read the json config file
			Object obj;
			obj =  jsonParser.parse(new FileReader(file));
			
			JSONObject jobj;
			// Check config file contains metadata for datafile
			if(obj == null) {
				log.error("Empty config File");
				return processConfig;
			} else {
				jobj = (JSONObject) obj;
				log.debug("Config file contents:" + jobj.toJSONString());
			}			
			
			// Get meta data of loader
			// Search for input_type
			if((obj = getJsonObject(jobj,Constants.VERSION)) != null){
				metadataConfigs.put(Constants.VERSION, obj.toString());
			} else {
				log.error("\"" + Constants.VERSION + "\"  Key is missing in config file");
				return processConfig;
			}
			
			if((obj = getJsonObject(jobj,Constants.INPUT_TYPE)) != null) {
				
				// Found input_type, check for csv 
				if(obj instanceof String && obj.toString().equals(Constants.CSV_FILE)) {
					// Found csv format
					metadataConfigs.put(Constants.INPUT_TYPE, obj.toString());
					
					// Search for csv_style
					if ((obj = getJsonObject(jobj,Constants.CSV_STYLE)) != null){
						// Found csv_style
						JSONObject cobj = (JSONObject) obj;
						
						// Number_Of_Columns in data file
						if((obj = getJsonObject(cobj,Constants.COLUMNS)) != null){
							metadataConfigs.put(Constants.COLUMNS, obj.toString());
						} else {
							log.error("\"" + Constants.COLUMNS + "\"  Key is missing in config file");
							return processConfig;
						}
						
						// Delimiter for parsing data file
						if((obj = getJsonObject(cobj,Constants.DELIMITER)) != null)
							metadataConfigs.put(Constants.DELIMITER, obj.toString());
						
						// Skip first row of data file if it contains column names
						if((obj = getJsonObject(cobj,Constants.IGNORE_FIRST_LINE)) != null)
							metadataConfigs.put(Constants.IGNORE_FIRST_LINE, obj.toString());
						
					} else {
						log.error("\"" + Constants.CSV_STYLE + "\"  Key is missing in config file");
						return processConfig;
					}
				} else {
					log.error("\"" + obj.toString() + "\"  file format is not supported in config file");
					return processConfig;
				}
				
			} else {
				log.error("\"" + Constants.INPUT_TYPE + "\"  Key is missing in config file");
				return processConfig;
			}
			
			
			// Get metadata of records
			// Get key definition of records
			if((obj = getJsonObject(jobj,Constants.KEY)) != null) {				
				metadataColumnDefs.add(getColumnDefs( (JSONObject) obj, Constants.KEY));
			} else {
				log.error("\"" + Constants.KEY + "\"  Key is missing in config file");
				return processConfig;
			}
			
			// Get set definition of records. Optional because we can get "set" name from user.
			if((obj =  getJsonObject(jobj,Constants.SET)) != null) {
				if (obj instanceof String) {
					metadataColumnDefs.add(new ColumnDefinition(Constants.SET, obj.toString() , true, true, "string", "string", null, -1, -1, null, null));
				} else {
					metadataColumnDefs.add(getColumnDefs( (JSONObject) obj, Constants.SET));
				}
			} 
			
			// Get bin column definitions 
			JSONArray binList;
			if((obj = getJsonObject(jobj,Constants.BINLIST)) != null) {
				// iterator for bins
				binList = (JSONArray) obj;
				Iterator<?> i = binList.iterator();
				// take each bin from the JSON array separately
				while (i.hasNext()) {
					JSONObject binObj = (JSONObject) i.next();
					binColumnDefs.add(getColumnDefs(binObj, Constants.BINLIST));
				}
				
			} else {
				return processConfig;
			}
			log.info(String.format("Number of columns: %d(metadata) + %d(bins)", (metadataColumnDefs.size()), binColumnDefs.size()));
			processConfig = true;
		} catch (IOException ie) {
			log.error("File:"+ Utils.getFileName(file.getName()) + " Config i/o Error: " + ie.toString());
			if(log.isDebugEnabled()){
				ie.printStackTrace();
			}
		} catch (ParseException pe) {
			log.error("File:"+ Utils.getFileName(file.getName()) + " Config parsing Error: " + pe.toString());
			if(log.isDebugEnabled()){
				pe.printStackTrace();
			}
		
		} catch (Exception e) {
			log.error("File:"+ Utils.getFileName(file.getName()) + " Config unknown Error: " + e.toString());
			if(log.isDebugEnabled()){
				e.printStackTrace();
			}
		}
		
		return processConfig;
	}
	
	public static Object getJsonObject(JSONObject jobj, String key) {
		Object obj = null;
		if ((obj = jobj.get(key)) == null){
			log.warn("\"" + key + "\"  Key is missing in config file");
		}
		return obj;
	}
	
	/**
	 * 
	 * @param jobj JSON object which contains json formatted data
	 * @param jobjName JSON object name for which it asks for
	 * @return ColumnDefinition object
	 */
	public static ColumnDefinition getColumnDefs(JSONObject jobj, String jobjName) throws Exception{
		String binNameHeader = null;
		String binValueHeader = null;
		boolean staticName = true;
		boolean staticValue = true;
		String srcType = null;
		String dstType = null;
		String encoding = null;
		int binNamePos = -1;
		int binValuePos = -1 ;
		String columnName = null;
		String jsonPath = null;

		if(Constants.KEY.equalsIgnoreCase(jobjName)  || Constants.SET.equalsIgnoreCase(jobjName)) {
			// Get key/set location
			if ((jobj.get(Constants.COLUMN_POSITION)) == null) {
				if ((columnName = (String)(jobj.get(Constants.COLUMN_NAME))) == null) {
					if ((jsonPath = (String)(jobj.get(Constants.JSON_PATH))) == null) {
						log.error("Specify proper key/set mapping in config file for " + jobjName + ":" + jobj.toString());
					} else {
						//TODO for json format
					}
				} else {
					binNameHeader = jobjName;
					binValueHeader = columnName;
				}
			} else {
				binNameHeader = jobjName;
				binValuePos = (Integer.parseInt(jobj.get(Constants.COLUMN_POSITION).toString())-1);
			}
			
			// Get key type
			if (Constants.KEY.equalsIgnoreCase(jobjName)) {
				srcType = (String) jobj.get(Constants.TYPE);				
			}
			
			// Default set type is string
			if (Constants.SET.equalsIgnoreCase(jobjName)) {
				srcType = (String) jobj.get(Constants.TYPE);
				if (srcType == null)
					srcType = "string";
			}
			
			//key and set value are always dynamic
			staticValue = false;
			
		} else if (Constants.BINLIST.equalsIgnoreCase(jobjName)){
			Object obj;			
			// Get name information of bin
			if ((obj = jobj.get(Constants.NAME)) != null) {
				if (obj instanceof JSONObject) {
					staticName = false;
					JSONObject nameObj = (JSONObject) obj;
					if ((nameObj.get(Constants.COLUMN_POSITION)) == null) {
						if ((columnName = (String)(nameObj.get(Constants.COLUMN_NAME))) == null) {
							if ((jsonPath = (String)(nameObj.get(Constants.JSON_PATH))) == null) {
								log.error("Specify proper bin name mapping in config file for " + jobjName + ":" + jobj.toString());
							} else {
								//TODO for json format
							}
						} else 
							binNameHeader = columnName;
					} else {
						binNamePos = (Integer.parseInt(nameObj.get(Constants.COLUMN_POSITION).toString())-1);
					}
				} else {
					binNameHeader = (String) obj;//default name
				}
			} else {
				log.error(Constants.NAME + " key is missing object:"+ jobj.toString());
			}
		
			// Get value information of bin
			JSONObject valueObj;
			if ((obj = jobj.get(Constants.VALUE)) != null){
				if(obj instanceof JSONObject) {
					staticValue = false;
					valueObj = (JSONObject) obj;
					
					if ((valueObj.get(Constants.COLUMN_POSITION)) == null) {
						if ((columnName = (String)(valueObj.get(Constants.COLUMN_NAME))) == null) {
							if ((jsonPath = (String)(valueObj.get(Constants.JSON_PATH))) == null) {
								log.error("Specify proper bin value mapping in config file for " + jobjName);
							} else {
								//TODO for json
							}
						} else {
							binValueHeader = columnName;
						}
					} else {
						
						binValuePos = (Integer.parseInt(valueObj.get(Constants.COLUMN_POSITION).toString())-1);
						
					}
					
					// Get src data type
					if ((srcType = (String)(valueObj.get(Constants.TYPE))) == null) {
						log.error(Constants.TYPE + " key is missing in bin object:"+ jobj.toString());
					}
					// Get destination type conversion for src data format
					if ((dstType = (String)(valueObj.get(Constants.DST_TYPE))) != null) {
						//log.error(Constants.DST_TYPE + " key is missing in bin object:"+ jobj.toString());
					}
					// Get encoding format 
					if ((encoding = (String)(valueObj.get(Constants.ENCODING))) == null) {
						//log.error(Constants.ENCODING + " key is missing in bin object:"+ jobj.toString());
					}				
				} else {
					binValueHeader = (String) obj;// default value
				} 
			} else {
				log.error(Constants.VALUE + " key is missing in bin object:"+ jobj.toString());
			}
			
		}
		ColumnDefinition colDef = new ColumnDefinition(binNameHeader, binValueHeader , staticName, staticValue, srcType, dstType, encoding, binNamePos, binValuePos, columnName, jsonPath);
		
		return colDef;
	}
	
	/**
	 * Parses the line into raw column string values
	 * Format of data should follow one rule: No delimeter contain same character as data part
	 * @param line One line in data file with CSV formated
	 * @return List List of column entries from line
	 */
	public static List<String> getCSVRawColumns(String line, char delimiter){
		
		List<String> store = new ArrayList<String>();
		StringBuilder curVal = new StringBuilder();
		boolean inquotes = false;
		boolean prevDelimiter = false;
		for (int i=0; i<line.length(); i++) {
			
			char ch = line.charAt(i);
			
			// trim white space and tabs after delimiter
			if (prevDelimiter) {
				if (Character.isWhitespace(ch)) {
					continue;
				} else {
					prevDelimiter = false;
				}
			}
			
			
			if (inquotes) {
				if (ch== Constants.DOUBLE_QOUTE_DELEMITER) {
					inquotes = false;
				}
				else {
					curVal.append(ch);
				}
			}
			else {
				
				if (ch == Constants.DOUBLE_QOUTE_DELEMITER) {
					inquotes = true;
					if (curVal.length()>0) {
						inquotes = false;
						//if this is the second quote in a value, add a quote
						//this is for the double quote in the middle of a value
						curVal.append('\"');
					}
				}
				
				else if (ch == delimiter) {
					prevDelimiter = true;
					// trim will remove all whitespace character from end of string or
					// after ending double quotes 
					store.add(curVal.toString());
					curVal = new StringBuilder();
				}
				else {
					curVal.append(ch);
				}
			}
		}
		store.add(curVal.toString());
		return store;
	}
}
