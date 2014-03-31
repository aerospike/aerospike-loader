package com.aerospike.load;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.AerospikeException;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.policy.Policy;
import com.aerospike.load.AerospikeLoad;

enum BinType {
	INTEGER, STRING, BLOB, LIST, MAP, JSON, TIMESTAMP;
}


/**
 * @author jyoti
 *
 */
public class CsvFileParserTest {
	
	String host = "192.168.64.138";
	String port = "3000";
	String ns = "test";
	String set = "demo";
	String config = "src/test/resources/config.json";
	String error_count = "0";
	String write_action = "update";
	String timeout = "10";
	
	String dataFile = "src/test/resources/data.csv";
	String log = "aerospike-load.log";
	AerospikeClient client;
	
	
	 @Before
	 public void setUp() {

		 try {
			client = new AerospikeClient(host, Integer.parseInt(port));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (AerospikeException e) {
			e.printStackTrace();
		}
		 if(!client.isConnected()) {
			System.out.println("Client is not able to connect:" + host + ":" + port);
			return;
		}
	 }
	 
	 @After
	 public void tearDown() {
		 client.close();
	     System.out.println("@After - tearDown");
	 }

	//String type data validation
	@Test
	public void testValidateString() throws Exception {
		System.out.println("TestValidateString: start");
		
		HashMap<String, String> binMap = new HashMap<String, String>();
		binMap.put("key", "string");
		binMap.put("set", "String");
		binMap.put("loc", "string");
		String dstType = null;
		//set%5, range=10, seed= 20	, nrecords= 100
		int setMod = 5, range = 100, seed = 10, nrecords = 10;
		String filename = dataFile;		
		writeDataMap(filename, nrecords, setMod, range, seed, binMap);
		
		AerospikeLoad.main(new String[]{"-h", host,"-p", port,"-n", ns,"-s", set, "-v", "-ec", error_count,"-wa", write_action,"-c", "src/test/resources/stringValidation.json", dataFile});
		
		boolean dataValid = validateMap(client, filename, nrecords, setMod, range, seed, binMap, dstType);
		boolean error = getError(log);
		
		assertTrue(dataValid);
		assertTrue(!error);
		
		System.out.println("TestValidateString: Complete");
	}
	
	//Integer type data validation
	@Test
	public void testValidateInteger() throws Exception {
		System.out.println("TestValidateInteger: start");
		
		HashMap<String, String> binMap = new HashMap<String, String>();
		binMap.put("key", "string");
		binMap.put("set", "String");
		binMap.put("age", "integer");
		String dstType = null;
		//set%5, range=10, seed= 20	, nrecords= 100
		int setMod = 5, range = 100, seed = 10, nrecords = 10;
		String filename = dataFile;		
		writeDataMap(filename, nrecords, setMod, range, seed, binMap);
		
		AerospikeLoad.main(new String[]{"-h", host,"-p", port,"-n", ns,"-s", set, "-v", "-ec", error_count,"-wa", write_action,"-c", "src/test/resources/integerValidation.json", dataFile});
		
		boolean dataValid = validateMap(client, filename, nrecords, setMod, range, seed, binMap, dstType);
		boolean error = getError(log);
		
		assertTrue(dataValid);
		assertTrue(!error);
		
		System.out.println("TestValidateInteger: Complete");
	}
	
	//Utf8 string type data validation
	//@Test
	public void testValidateStringUtf8() throws Exception {
		System.out.println("TestValidateStringUtf8: start");
		
		HashMap<String, String> binMap = new HashMap<String, String>();
		binMap.put("key", "string");
		binMap.put("set", "String");
		binMap.put("age", "string");
		String dstType = null;
		
		//set%5, range=10, seed= 20	, nrecords= 100
		int setMod = 5, range = 100, seed = 10, nrecords = 10;
		String filename = dataFile;		
		writeDataMap(filename, nrecords, setMod, range, seed, binMap);
		
		AerospikeLoad.main(new String[]{"-h", host,"-p", port,"-n", ns,"-s", set, "-v", "-ec", error_count,"-wa", write_action,"-c", "src/test/resources/stringUtf8Validation.json", dataFile});
		
		boolean dataValid = validateMap(client, filename, nrecords, setMod, range, seed, binMap, dstType);
		boolean error = getError(log);
		
		assertTrue(dataValid);
		//assertTrue(error);
			
		System.out.println("TestValidateStringutf8: Complete");
	}
		
	//timestamp type data validation
	@Test
	public void testValidateTimestampInteger() throws Exception {
		System.out.println("TestValidateTimestampInteger: start");
		
		HashMap<String, String> binMap = new HashMap<String, String>();
		binMap.put("key", "string");
		binMap.put("set", "String");
		binMap.put("dob", "timestamp");
		String dst_type = "integer";
		//set%5, range=10, seed= 20	, nrecords= 100
		int setMod = 5, range = 100, seed = 10, nrecords = 10;
		String filename = dataFile;		
		writeDataMap(filename, nrecords, setMod, range, seed, binMap);
		
		AerospikeLoad.main(new String[]{"-h", host,"-p", port,"-n", ns,"-s", set, "-v", "-ec", error_count,"-wa", write_action,"-c", "src/test/resources/timestampIntegerValidation.json", dataFile});
			
		boolean dataValid = validateMap(client, filename, nrecords, setMod, range, seed, binMap, dst_type);
	
		boolean error = getError(log);
			
		assertTrue(dataValid);
		//assertTrue(error);
				
		System.out.println("TestValidateTimestampInteger: Complete");
	}
	
	//String type data validation
	//@Test
	public void testValidateBlob() throws Exception {
		System.out.println("TestValidateBlob: start");
		
		HashMap<String, String> binMap = new HashMap<String, String>();
		binMap.put("key", "string");
		binMap.put("set", "String");
		binMap.put("loc", "blob");
		String dstType = null;
		//set%5, range=10, seed= 20	, nrecords= 100
		int setMod = 5, range = 100, seed = 10, nrecords = 10;
		String filename = dataFile;		
		writeDataMap(filename, nrecords, setMod, range, seed, binMap);
		
		AerospikeLoad.main(new String[]{"-h", host,"-p", port,"-n", ns,"-s", set, "-v", "-ec", error_count,"-wa", write_action,"-c", "src/test/resources/blobValidation.json", dataFile});
		
		boolean dataValid = validateMap(client, filename, nrecords, setMod, range, seed, binMap, dstType);
		boolean error = getError(log);
		
		assertTrue(dataValid);
		assertTrue(!error);
			
		System.out.println("TestValidateBlob: Complete");
	}
	
	//Multiple data type insert
	@Test
	public void testAllDatatype() throws Exception {
		System.out.println("TestAllDatatype: start");
		
		HashMap<String, String> binMap = new HashMap<String, String>();
		binMap.put("key", "integer");
		binMap.put("set", "String");
		binMap.put("loc", "string");
		binMap.put("dob", "timestamp");
		binMap.put("age", "integer");
		String dstType = "integer";
		//set%5, range=10, seed= 20	, nrecords= 100
		int setMod = 5, range = 100, seed = 10, nrecords = 100;
		String filename = dataFile;		
		writeDataMap(filename, nrecords, setMod, range, seed, binMap);
		
		AerospikeLoad.main(new String[]{"-h", host,"-p", port,"-n", ns,"-s", set, "-ec", error_count,"-wa", write_action,"-c", "src/test/resources/allDatatype.json", dataFile});
		
		boolean error = getError(log);

		assertTrue(!error);
			
		System.out.println("TestAllDatatype: Complete");
	}

	
	//Dynamic bin name
	@Test
	public void testDynamicBinName() throws Exception {
		System.out.println("Test Dynamic BinName: start");
			
		AerospikeLoad.main(new String[]{"-h", host,"-p", port,"-n", ns,"-s", set, "-v", "-ec", error_count,"-wa", write_action,"-c", "src/test/resources/dynamicBinName.json", "src/test/resources/dynamicBinData.csv"});

		boolean error = getError(log);

		assertTrue(!error);
				
		System.out.println("Test Dynamic BinName: Complete");
	}
	
	//Static binName
	@Test
	public void testStaticBinName() throws Exception {
			System.out.println("Test static BinName: start");
				
			AerospikeLoad.main(new String[]{"-h", host,"-p", port,"-n", ns,"-s", set, "-v", "-ec", error_count,"-wa", write_action,"-c", "src/test/resources/staticBinName.json", "src/test/resources/staticBinData.csv"});

			boolean error = getError(log);

			assertTrue(!error);
					
			System.out.println("Test static BinName: Complete");
	}
	
	// Helper functions
	public void writeDataMap(String fileName, int nrecords, int setMod, int range, int seed, HashMap<String, String> binMap){
		File file = new File(fileName);
		// if file doesnt exists, then create it
		
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			Writer bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file), "UTF8"));
			Random r = new Random(seed);	
			int rint;
			for (int i = 0; i <= nrecords; i++) {
				int comma = 0;
				
				rint = r.nextInt(range);
			
				Iterator iterator = binMap.entrySet().iterator();
				String binName;
				String binType;
				Map.Entry mapEntry;
				while (iterator.hasNext()) {
					mapEntry = (Map.Entry) iterator.next();
					binName = (String)mapEntry.getKey();
					binType = (String)mapEntry.getValue();
					if ( binName != null){
						if(i == 0) {
							bw.write(binName);
						} else {
							if(binName.equalsIgnoreCase("key")) {
								bw.write(getValue(binName, binType, i));					
							} else if(binName.equalsIgnoreCase("set")) {
								bw.write(getValue(binName, "string", i%setMod));	
							} else {
								bw.write(getValue(binName, binType, rint));
							}
					    }
					}
					if( binMap.size() > ++comma) {
						bw.write(String.format(","));
					}						
				}
				bw.write("\n");
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean validateMap(AerospikeClient client, String filename,int nrecords,int setMod,int range,int seed, HashMap<String, String>  binMap, String dstType) {
		boolean valid = false;
		
		Random r = new Random(seed);	
		int rint;
		String key = null;
		String set = null;
		Key key1 = null;
		Bin bin1 = null;
		String bin1Type = null;
		Record record = null;
		
		for (int i = 0; i <= nrecords; i++) {			
			rint = r.nextInt(range);
		
			Iterator iterator = binMap.entrySet().iterator();
			String binName;
			String binType = null;
			Map.Entry mapEntry;
			while (iterator.hasNext()) {
				mapEntry = (Map.Entry) iterator.next();
				
				if ( (binName = (String)mapEntry.getKey()) != null){
					if(i == 0) {
						//skip 1st row data
					} else {
						binType = (String)mapEntry.getValue();
						if(binName.equalsIgnoreCase("key")) {
							key = (String.format(getValue(binName, binType , i)));
						} else if(binName.equalsIgnoreCase("set")) {
							set = String.format(getValue(binName, "string", i%setMod));	
						} else {
							String value = (String.format(getValue(binName,binType, rint)));
							bin1 = new Bin(binName, value);
							bin1Type = value;
						}	
					}
				}					
			}

			if (i!=0) {
				try {
					key1 = new Key(ns, set, key);
					record = client.get(new Policy(), key1);
				} catch (AerospikeException e) {
					e.printStackTrace();
				}
			
			if (validateBin(key1, bin1, bin1Type, dstType, record))
				valid = true;
			}
		}
		
		return valid;
	}

	/**
	 * @param key key to validate
	 * @param bin bin to validate
	 * @param binType type of bin
	 * @param dstType dst bin type
	 * @param record received record
	 * @return
	 */
	private boolean validateBin(Key key, Bin bin, String binType,String dstType, Record record) {
		boolean valid = false;
		String expected = null;
		
		Object received = record.getValue(bin.name);
		
		if(dstType != null && dstType.equalsIgnoreCase("integer")){
			DateFormat format = new SimpleDateFormat("MM/dd/yy");
			try {							
				Date formatDate = format.parse(bin.value.toString());
				long miliSecondForDate = formatDate.getTime()/1000 ;
				expected = String.format("%d", miliSecondForDate);
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else{
			expected = bin.value.toString();
		}
				
		if (received != null && received.toString().equals(expected)) {
			System.out.println(String.format("Bin matched: namespace=%s set=%s key=%s bin=%s value=%s generation=%d expiration=%d", 
				key.namespace, key.setName, key.userKey, bin.name, received, record.generation, record.expiration));
			valid = true;
		}
		else {
			System.out.println(String.format("Put/Get mismatch: Expected %s. Received %s.", expected, received));
		}
		
		return valid;
	}
	
	/**
	 * @param log log file name
	 * @return return true if get any error
	 */
	public boolean getError(String log) {
		boolean error = false;
		
		BufferedReader br = null;
		try {
			String line;
			br = new BufferedReader(new FileReader(log));
			try {
				while ((line = br.readLine()) != null) {
					if((line.substring(0,5)).contains("ERROR"))
					{
						error = true;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return error;
	}
	
	/**
	 * @param binName binName prefix
	 * @param binType type of binValue
	 * @param i index of insert
	 * @return
	 */
	public String getValue(String binName, String binType, int i) {

		String value = null;
		
		switch(getBinType(binType.toLowerCase())){
		case BLOB:
			//value = byte[] blob = new byte[] {3, 52, 125};
			
			break;
		case INTEGER:
			value = String.format("%d", i);
			break;
		case JSON:
			break;
		case LIST:
			break;
		case MAP:
			break;
		case STRING:
			value = String.format("%s%d",binName, i);
			break;
		case TIMESTAMP:
			value = String.format("%d/%d/%d", i%12,i%30,i%100);
			break;
		default:
			break;
		}
		
		return value;
	}

	public BinType getBinType(String type) {
		if ("string".equalsIgnoreCase(type)){
			return BinType.STRING;
		} else if ("integer".equalsIgnoreCase(type)){
			return BinType.INTEGER;
		} else if ("blob".equalsIgnoreCase(type)){
			return BinType.BLOB;
		} else if ("list".equalsIgnoreCase(type)){
			return BinType.LIST;
		} else if ("map".equalsIgnoreCase(type)){
			return BinType.MAP;
		} else if ("json".equalsIgnoreCase(type)){
			return BinType.JSON;
		} else if ("timestamp".equalsIgnoreCase(type)){
			return BinType.TIMESTAMP;
		}
		return null;
	}
	
}
