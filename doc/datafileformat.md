#Data file format
> User can provide "list of file names" or "directory name", which contains data dump. Currently data dump in CSV formatted is supported by aerospike-loader.

##Sample data file:
``` c
user_location, user_id,last_visited, community
India, 1, 08/16/2011, facebook
India, 2, 08/17/2011, Tweeter
USA, 3, 08/16/2011, Tweeter
```

Above one is a sample of a data file. First row is the header information of each column. We can use this header information  for column mapping in configuration file. Next three rows contain data with ',' separated.

##Supported Data Types:

- Integer : Integer type data including numbers. E.g. 123456
- Float   : Floating type data is stored in aerospike as 8 byte byte array. E.g. 0.345
- String  : String type data. E.g. "Aerospike"
- Blob    : Binary fields which is hex ecoded is stored as blobs. E.g. hex encoded "abc" as 616263.
- Timestamp: Timestamp type data stored as string or integer. E.g. "1-1-1970" as string stored as "1-1-1970" but as integer stored as -19800 seconds(negative because its calculated reference to UTC timezone).

> Timestamp type data should have some format, and always be in double quotes. For best practices in timestamp formatting refer [SimpleDateFormat](http://docs.oracle.com/javase/6/docs/api/java/text/SimpleDateFormat.html)
