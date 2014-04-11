#Data file format
> User can provide "list of file names" or "directory name", which contains data dump. Currently data dump in CSV formatted is supported by aerospike-loader.

##Sample data file:
``` c
user_location, user_id,last_visited, community
India, 1, 08/16/2011, facebook
India, 2, 08/17/2011, Tweeter
USA, 3, 08/16/2011, Tweeter
```

Above one is a sample of a data file. First row is the header information of each column. We can use this header information  for column mapping in configuration file.
