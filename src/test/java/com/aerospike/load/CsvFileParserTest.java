package com.aerospike.load;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.AerospikeException;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.policy.Policy;

enum BinType {
	INTEGER, STRING, BLOB, LIST, MAP, JSON, TIMESTAMP, FLOAT;
}


/**
 * @author jyoti
 *
 */
public class CsvFileParserTest {
	
	String host = "127.0.0.1";
	String port = "3000";
	String ns = "test";
	String set = null;
	String config = "src/test/resources/config.json";
	String error_count = "0";
	String write_action = "update";
	String timeout = "10";
	
	String dataFile = "src/test/resources/data.csv";
	String log = "aerospike-load.log";
	AerospikeClient client;
	
	public CsvFileParserTest(){
		 try {
			client = new AerospikeClient(host, Integer.parseInt(port));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (AerospikeException e) {
			e.printStackTrace();
		}

	}
	
	@Override
	protected void finalize() throws Throwable {
		client.close();
	}
	 @Before
	 public void setUp() {

	 }
	 
	 @After
	 public void tearDown() {
		 
	 }

	//String type data validation
	@Test
	public void testValidateString() throws Exception {
		System.out.println("TestValidateString: start");
		if(!client.isConnected()) {
			System.out.println("Client is not able to connect:" + host + ":" + port);
			return;
		}
		
		HashMap<String, String> binMap = new HashMap<String, String>();
		binMap.put("key", "string");
		binMap.put("set", "String");
		binMap.put("loc", "string");
		String dstType = null;
		//set%5, range=10, seed= 20	, nrecords= 100
		int setMod = 5, range = 100, seed = 10, nrecords = 10;
		String filename = dataFile;
		writeDataMap(filename, nrecords, setMod, range, seed, binMap);
		
		AerospikeLoad.main(new String[]{"-h", host,"-p", port,"-n", ns, "-ec", error_count,"-wa", write_action,"-c", "src/test/resources/stringValidation.json", dataFile});
		
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
		if(!client.isConnected()) {
			System.out.println("Client is not able to connect:" + host + ":" + port);
			return;
		}
		
		HashMap<String, String> binMap = new HashMap<String, String>();
		binMap.put("key", "string");
		binMap.put("set", "String");
		binMap.put("age", "integer");
		String dstType = null;
		//set%5, range=10, seed= 20	, nrecords= 100
		int setMod = 5, range = 100, seed = 10, nrecords = 10;
		String filename = dataFile;
		writeDataMap(filename, nrecords, setMod, range, seed, binMap);
		
		AerospikeLoad.main(new String[]{"-h", host,"-p", port,"-n", ns, "-ec", error_count,"-wa", write_action,"-c", "src/test/resources/integerValidation.json", dataFile});
		
		boolean dataValid = validateMap(client, filename, nrecords, setMod, range, seed, binMap, dstType);
		boolean error = getError(log);
		
		assertTrue(dataValid);
		assertTrue(!error);
		
		System.out.println("TestValidateInteger: Complete");
	}

	//Blob type data validation
	@Test
	public void testValidateDouble() throws Exception {
		System.out.println("TestValidateDouble: start");
		if(!client.isConnected()) {
			System.out.println("Client is not able to connect:" + host + ":" + port);
			return;
		}
		
		HashMap<String, String> binMap = new HashMap<String, String>();
		binMap.put("key", "string");
		binMap.put("set", "String");
		binMap.put("percentage", "float");
		String dstType = null;

		int setMod = 5, range = 100, seed = 10, nrecords = 10;
		String filename = dataFile;
		writeDataMap(filename, nrecords, setMod, range, seed, binMap);
		
		AerospikeLoad.main(new String[]{"-h", host,"-p", port,"-n", ns, "-ec", error_count,"-wa", write_action,"-c", "src/test/resources/doubleValidation.json", dataFile});
		
		boolean dataValid = validateMap(client, filename, nrecords, setMod, range, seed, binMap, dstType);
		boolean error = getError(log);
		
		assertTrue(dataValid);
		assertTrue(!error);
		
		System.out.println("TestValidateDouble: Complete");
	}

	//Utf8 string type data validation
	@Test
	public void testValidateStringUtf8() throws Exception {
		System.out.println("TestValidateStringUtf8: start");
		if(!client.isConnected()) {
			System.out.println("Client is not able to connect:" + host + ":" + port);
			return;
		}
		
		HashMap<String, String> binMap = new HashMap<String, String>();
		binMap.put("key", "string");
		binMap.put("set", "String");
		binMap.put("UTF8", "string");
		String dstType = null;
		
		//set%5, range=10, seed= 20	, nrecords= 100
		int setMod = 5, range = 100, seed = 10, nrecords = 10;
		String filename = dataFile;
		writeDataMap(filename, nrecords, setMod, range, seed, binMap);
		
		AerospikeLoad.main(new String[]{"-h", host,"-p", port,"-n", ns, "-ec", error_count,"-wa", write_action,"-c", "src/test/resources/stringUtf8Validation.json", dataFile});
		
		boolean dataValid = validateMap(client, filename, nrecords, setMod, range, seed, binMap, dstType);
		boolean error = getError(log);
		
		assertTrue(dataValid);
		assertTrue(!error);
			
		System.out.println("TestValidateStringutf8: Complete");
	}

	//timestamp type data validation
	@Test
	public void testValidateTimestampInteger() throws Exception {
		System.out.println("TestValidateTimestampInteger: start");
		if(!client.isConnected()) {
			System.out.println("Client is not able to connect:" + host + ":" + port);
			return;
		}
		
		HashMap<String, String> binMap = new HashMap<String, String>();
		binMap.put("key", "string");
		binMap.put("set", "String");
		binMap.put("dob", "timestamp");
		String dst_type = "integer";
		//set%5, range=10, seed= 20	, nrecords= 100
		int setMod = 5, range = 100, seed = 10, nrecords = 10;
		String filename = dataFile;
		writeDataMap(filename, nrecords, setMod, range, seed, binMap);
		
		AerospikeLoad.main(new String[]{"-h", host,"-p", port,"-n", ns, "-ec", error_count,"-wa", write_action,"-c", "src/test/resources/timestampIntegerValidation.json", dataFile});
			
		boolean dataValid = validateMap(client, filename, nrecords, setMod, range, seed, binMap, dst_type);
	
		boolean error = getError(log);
			
		assertTrue(dataValid);
		assertTrue(!error);
				
		System.out.println("TestValidateTimestampInteger: Complete");
	}

	//Blob type data validation
	@Test
	public void testValidateBlob() throws Exception {
		System.out.println("TestValidateBlob: start");
		if(!client.isConnected()) {
			System.out.println("Client is not able to connect:" + host + ":" + port);
			return;
		}
		
		HashMap<String, String> binMap = new HashMap<String, String>();
		binMap.put("key", "string");
		binMap.put("set", "String");
		binMap.put("loc", "blob");
		String dstType = "blob";
		//set%5, range=10, seed= 20	, nrecords= 100
		int setMod = 5, range = 100, seed = 10, nrecords = 10;
		String filename = dataFile;
		writeDataMap(filename, nrecords, setMod, range, seed, binMap);
		
		AerospikeLoad.main(new String[]{"-h", host,"-p", port,"-n", ns, "-ec", error_count,"-wa", write_action,"-c", "src/test/resources/blobValidation.json", dataFile});
		
		boolean dataValid = validateMap(client, filename, nrecords, setMod, range, seed, binMap, dstType);
		boolean error = getError(log);
		
		assertTrue(dataValid);
		assertTrue(!error);
			
		System.out.println("TestValidateBlob: Complete");
	}

	//List type data validation
	@Test
	public void testValidateList() throws Exception {
		System.out.println("TestValidateList: start");
		if(!client.isConnected()) {
			Assert.fail("Client is not able to connect:" + host + ":" + port);
		}


		String filename = "data/list/list-data.csv";
		
		this.client.delete(null, new Key("test", "list", "user-1"));
		this.client.delete(null, new Key("test", "list", "user-2"));
		this.client.delete(null, new Key("test", "list", "user-3"));
		this.client.delete(null, new Key("test", "list", "user-4"));

		AerospikeLoad.main(new String[]{"-h", host,"-p", port,"-n", ns, "-ec", error_count,"-wa", write_action,"-c", "src/test/resources/listValidation.json", filename});
		
		Key key = new Key("test", "list", "user-1");
		Record record = this.client.get(null, key);
		Assert.assertEquals("bob", record.getString("name"));
		Assert.assertTrue(record.getValue("segments") instanceof List);
		Assert.assertNotNull("Bin is not a Large List", this.client.getLargeList(null, key, "l-segments"));
		System.out.println("TestValidateList: Complete");
	}

	//Map type data validation
	//@Test
	public void testValidateMap() throws Exception {
		System.out.println("TestValidateMap: start");
		if(!client.isConnected()) {
			System.out.println("Client is not able to connect:" + host + ":" + port);
			return;
		}
		HashMap<String, String> binMap = new HashMap<String, String>();
		binMap.put("key", "string");
		binMap.put("set", "String");
		binMap.put("mapprog", "map");
		String dstType = "map";
		//set%5, range=10, seed= 20	, nrecords= 100
		int setMod = 5, range = 100, seed = 10, nrecords = 10;
		String filename = dataFile;
		writeDataMap(filename, nrecords, setMod, range, seed, binMap);
		AerospikeLoad.main(new String[]{"-h", host,"-p", port,"-n", ns, "-ec", error_count,"-wa", write_action,"-c", "src/test/resources/mapValidation.json", dataFile});
		boolean dataValid = validateMap(client, filename, nrecords, setMod, range, seed, binMap, dstType);
		boolean error = getError(log);

		assertTrue(dataValid);
		assertTrue(!error);

		System.out.println("TestValidateMap: Complete");
	}

	//JSON type data validation
	//@Test
	public void testValidateJSON() throws Exception {
		System.out.println("TestValidateJSON: start");
		if(!client.isConnected()) {
			System.out.println("Client is not able to connect:" + host + ":" + port);
			return;
		}
		HashMap<String, String> binMap = new HashMap<String, String>();
		binMap.put("key", "string");
		binMap.put("set", "String");
		binMap.put("JSONprog", "JSON");
		String dstType = "JSON";
		//set%5, range=10, seed= 20	, nrecords= 100
		int setMod = 5, range = 100, seed = 10, nrecords = 10;
		String filename = dataFile;
		writeDataMap(filename, nrecords, setMod, range, seed, binMap);
		AerospikeLoad.main(new String[]{"-h", host,"-p", port,"-n", ns, "-v", "-ec", error_count,"-wa", write_action,"-c", "src/test/resources/jsonValidation.json", dataFile});
		boolean dataValid = validateMap(client, filename, nrecords, setMod, range, seed, binMap, dstType);
		boolean error = getError(log);

		assertTrue(dataValid);
		assertTrue(!error);

		System.out.println("TestValidateJSON: Complete");
	}

	//Multiple data type insert
	@Test
	public void testAllDatatype() throws Exception {
		System.out.println("TestAllDatatype: start");
		if(!client.isConnected()) {
			System.out.println("Client is not able to connect:" + host + ":" + port);
			return;
		}
		
		HashMap<String, String> binMap = new HashMap<String, String>();
		binMap.put("key", "integer");
		binMap.put("set", "String");
		binMap.put("loc", "string");
		binMap.put("dob", "timestamp");
		binMap.put("locblob", "blob");
		binMap.put("age", "integer");
		//set%5, range=10, seed= 20	, nrecords= 100
		int setMod = 5, range = 100, seed = 10, nrecords = 100;
		String filename = dataFile;
		writeDataMap(filename, nrecords, setMod, range, seed, binMap);
		
		AerospikeLoad.main(new String[]{"-h", host,"-p", port,"-n", ns, "-ec", error_count,"-wa", write_action,"-c", "src/test/resources/allDatatype.json", dataFile});
		
		boolean error = getError(log);

		assertTrue(!error);
			
		System.out.println("TestAllDatatype: Complete");
	}

	//Dynamic bin name
	@Test
	public void testDynamicBinName() throws Exception {
		System.out.println("Test Dynamic BinName: start");
		if(!client.isConnected()) {
			System.out.println("Client is not able to connect:" + host + ":" + port);
			return;
		}
			
		AerospikeLoad.main(new String[]{"-h", host,"-p", port,"-n", ns, "-ec", error_count,"-wa", write_action,"-c", "src/test/resources/dynamicBinName.json", "src/test/resources/dynamicBinData.csv"});

		boolean error = getError(log);

		assertTrue(!error);
				
		System.out.println("Test Dynamic BinName: Complete");
	}

	//Static binName
	@Test
	public void testStaticBinName() throws Exception {
		System.out.println("Test static BinName: start");
		if(!client.isConnected()) {
			System.out.println("Client is not able to connect:" + host + ":" + port);
			return;
		}
	
		AerospikeLoad.main(new String[]{"-h", host,"-p", port,"-n", ns, "-ec", error_count,"-wa", write_action,"-c", "src/test/resources/staticBinName.json", "src/test/resources/staticBinData.csv"});

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
			
				Iterator<Entry<String, String>> iterator = binMap.entrySet().iterator();
				String binName;
				String binType;
				Map.Entry<String, String> mapEntry;
				while (iterator.hasNext()) {
					mapEntry = iterator.next();
					binName = mapEntry.getKey();
					binType = mapEntry.getValue();
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
		
			Iterator<Entry<String, String>> iterator = binMap.entrySet().iterator();
			String binName;
			String binType = null;
			Map.Entry<String, String> mapEntry;
			while (iterator.hasNext()) {
				mapEntry = iterator.next();
				
				if ( (binName = mapEntry.getKey()) != null){
					if(i == 0) {
						//skip 1st row data
					} else {
						binType = mapEntry.getValue();
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
		
		if(binType == "timestamp" && dstType != null && dstType.equalsIgnoreCase("integer")){
			DateFormat format = new SimpleDateFormat("MM/dd/yy");
			try {							
				Date formatDate = format.parse(bin.value.toString());
				long miliSecondForDate = formatDate.getTime()/1000 ;
				expected = String.format("%d", miliSecondForDate);
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
		} else if (dstType != null && dstType.equalsIgnoreCase("blob")){
			expected = convertHexToString(bin.value.toString());
			received = new String((byte[]) received);
		} else if (dstType != null && dstType.equalsIgnoreCase("list")){
			received = received.toString().replace('[', '"').replace(']', '"');
			expected = bin.value.toString();
		} else if (dstType != null && dstType.equalsIgnoreCase("map")){
			Map<String, Integer> map = (Map<String, Integer>) received;
			String temp = String.format("\"%s%d=%s, %s%d=%s, %s%d=%s, %s%d=%s, %s%d=%s\"",
					bin.name,1,map.get(bin.name+"1"),
					bin.name,2,map.get(bin.name+"2"),
					bin.name,3,map.get(bin.name+"3"),
					bin.name,4,map.get(bin.name+"4"),
					bin.name,5,map.get(bin.name+"5"));
			received = temp;
			expected = bin.value.toString();
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
			value = convertStringToHex(String.format("%s%d",binName, i));
			break;
		case INTEGER:
			value = String.format("%d", i);
			break;
		case FLOAT:
			value = String.format("%.1f", (double) i);
			break;
		case JSON:
			JSONParser jsonParser = new JSONParser();
			// Read the json config file
			Object obj = null;
			try {
				obj =  jsonParser.parse(new FileReader("src/test/resources/jsonValidation.json"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			value = String.format( "\"%s\"",obj.toString().replace(':', '='));
			break;
		case LIST:
			value = String.format("\"%s%d, %s%d, %s%d, %s%d, %s%d\"", binName,1,binName,2,binName,3,binName,4,binName,5);
			break;
		case MAP:
			value = String.format("\"%s%d=%d, %s%d=%d, %s%d=%d, %s%d=%d, %s%d=%d\"", binName,1,1,binName,2,2,binName,3,3,binName,4,4,binName,5,5);
			break;
		case STRING:
			if(binName.equalsIgnoreCase("utf8")){
				value = String.format("%s%d","Ûtf8", i);
			} else
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
		}else if ("float".equalsIgnoreCase(type)){
			return BinType.FLOAT;
		}
		return null;
	}
	
	public String convertStringToHex(String str){

		char[] chars = str.toCharArray();
		StringBuffer hex = new StringBuffer();
		for(int i = 0; i < chars.length; i++){
			hex.append(Integer.toHexString((int)chars[i]));
		}
		return hex.toString();
	}

	public String convertHexToString(String hex){

		StringBuilder sb = new StringBuilder();
		StringBuilder temp = new StringBuilder();
		
		for( int i=0; i<hex.length()-1; i+=2 ){
			//get the hex in pairs
			String pair = hex.substring(i, (i + 2));
			//convert hex to decimal
			int numpair = Integer.parseInt(pair, 16);
			//convert the decimal to character
			sb.append((char)numpair);

			temp.append(numpair);
		}
		return sb.toString();
	}
}
