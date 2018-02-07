#Data file format
> User can provide "list of filenames" or "directory name", which contains data dump. Currently data dump in DSV formatted is supported by aerospike-loader.

##Sample data file:
``` c
user_location##user_id##last_visited##community
India##1##08/16/2011##facebook
India##2##08/17/2011##Twitter
USA##3##08/16/2011##Twitter
```

In the above sample data file first row is the header information for each column. This column name can be used for column mapping in configuration file. Next three rows contain data with delimiter('##') separated.

##Supported Data Types:

- Integer : Integer type data including numbers. E.g. 123456
- Float   : Floating type data is stored as float earlier it was stored as 8 byte byte array. E.g. 0.345
- String  : String type data. E.g. "Aerospike"
- Blob    : Binary fields which is hex encoded is stored as blobs. E.g. hex encoded "abc" as 616263.
- Timestamp: Timestamp type data stored as string or integer. E.g. "1-1-1970" as string stored as "1-1-1970" but as integer stored as -19800 seconds(negative because its calculated reference to UTC timezone). 
- Json    : Any standard JSON doc. (List, map will also go as JSON.). E.g. List: ["a", "b", ["c", "d"]], Map: {"a": "b", "c": {"d", "e"}}. 
 
> **Note**: Data file should not contain ':' and ',' as delimiter if file consist any JSON data. Only data inside double quotes (" ") will not be searched for delimiter. Its DSV supported so user can use any good delimiter.
 
> **Note**: Timestamp type data should have some format, and always be in double quotes. For best practices in timestamp formatting refer [SimpleDateFormat](http://docs.oracle.com/javase/6/docs/api/java/text/SimpleDateFormat.html).
