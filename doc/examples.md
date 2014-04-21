#Example

Each example given below is for different use cases. To learn the usage quickly, just copy the data file content to data.csv, config file content to config.json and run the loader using following run command.

    ./run_loader -h localhost -c config.json data.csv

- [With header in data file](#with header)
- [Without header in data file](#without header)
- [Static value](#static value)
- [Options usage](#options)

<a name="with header"></a>
##__1. With header in data file__ : 
Given below is an example of using datafile to write config file and usage of all supported data type formats. Referring data file we can create config file first. Data file may or may not contain column name as header information. If header information exists then it must present in first line of data file and we can use this header for column position mapping. Use of without header data file is given in next example.

### Data file content:
```CSV
user_location,user_id,last_visited,set_name,age,user_name,user_name_blob,user_rating
IND, userid1, 04/1/2014, facebook, 20, X20, 583230, 8.1
USA, userid2, 03/18/2014, twitter, 27, X2, 5832, 6.4
UK, userid3, 01/9/2014, twitter, 21, X3, 5833, 4.3
UK, userid4, 01/2/2014, facebook, 16, X9, 5839, 5.9
IND, userid5, 08/20/2014, twitter, 37, X10, 583130, 9.3
```

### Config file content:
```JSON
{
  "version" : "1.0",
  "input_type" : "csv",
  "csv_style": { "delimiter": "," , "n_columns_datafile": 8, "ignore_first_line": true},


  "key": {"column_name":"user_id", "type": "string"},

  "set": { "column_name":"set_name" , "type": "string"},

  "binlist": [
    {"name": "age",
     "value": {"column_name": "age", "type" : "integer"}
    },
    {"name": "location",
     "value": {"column_name": "user_location", "type" : "string"}
    },
    {"name": "name",
     "value": {"column_name": "user_name", "type" : "String"}
    },
    {"name": "name_blob",
     "value": {"column_name": "user_name_blob", "type" : "blob", "dst_type" : "blob" , "encoding":"hex"}
    },
    {"name": "recent_visit",
     "value": {"column_name": "last_visited", "type" : "timestamp", "encoding":"MM/dd/yy", "dst_type": "integer"}
    },
    {"name": "rating",
     "value": {"column_name": "user_rating", "type" : "float"}
    }
  ]
}
```
### Explanation:
- Specify delimiter(the way two bin values separated), n_columns_datafile(actual column count in data file), ignore_first_line(true if header or column names is mentioned in first line of datafile else false.)
- Binlist contains array of bin mapping. In each bin mapping two entries are there, one is name which is used as bin name in aerospike and other one is value, which is the bin content mapping. If one column mapping is absent in config file then that column will be skipped while loading.
- Instead of using column_name we can use column_position.
- Native data types integer and string are stored as it is.
    - add following line to binlist in config file for __integer__ type data.
       - {"name": "age", "value": {  "column_name": "age", "type" : "integer"}}
    - add following line to binlist in config file for __string__ type data.
       - {"name": "location", "value": { "column_name": "user_location", "type" : "string"}}
- Data types other than native types:
    - add following line to binlist in config file for __blob__ type data. Data in data file should be in hex format.
       - {"name": "name_blob", "value": {"column_name": "user_name_blob", "type" : "blob", "dst_type" : "blob" , "encoding":"hex"}
    }
    - add following line to binlist in config file for __float__ type data.
       -  {"name": "rating", "value": {"column_name": "user_rating", "type" : "float"}
    }
    - add following line to binlist in config file for __timestamp__ type data.
      - { "name": "recent_visit", "value": {"column_name": "last_visited", "type" : "timestamp", "encoding":"MM/dd/yy", "dst_type": "integer"}
    }


<a name="without header"></a>
##__2. Without header in data file__:
Given below is an example of using datafile in which there is no header information in first line. When header information is not present in data file always use column_position for column mapping.

### Data file content:
```CSV
IND, userid1, 04/1/2014, facebook, 20, X20, 583230, 8.1
USA, userid2, 03/18/2014, twitter, 27, X2, 5832, 6.4
UK, userid3, 01/9/2014, twitter, 21, X3, 5833, 4.3
UK, userid4, 01/2/2014, facebook, 16, X9, 5839, 5.9
IND, userid5, 08/20/2014, twitter, 37, X10, 583130, 9.3
```
### Config file content:
```JSON
{
  "version" : "1.0",
  "input_type" : "csv",
  "csv_style": { "delimiter": "," , "n_columns_datafile": 8, "ignore_first_line": false},


  "key": {"column_position":2, "type": "string"},

  "set": { "column_position":4 , "type": "string"},

  "binlist": [
    {"name": "age",
     "value": {"column_position": 5, "type" : "integer"}
    },
    {"name": "location",
     "value": {"column_position": 1, "type" : "string"}
    },
    {"name": "name",
     "value": {"column_position": 6, "type" : "String"}
    },
    {"name": "name_blob",
     "value": {"column_position": 7, "type" : "blob", "dst_type" : "blob" , "encoding":"hex"}
    },
    {"name": "recent_visit",
     "value": {"column_position": 3, "type" : "timestamp", "encoding":"MM/dd/yy", "dst_type": "integer"}
    },
    {"name": "rating",
     "value": {"column_position": 8, "type" : "float"}
    }
  ]
}
```
### Explanation:

-  As there is no header information in data file so "ignore_first_line" should be false.
-  Each column mapping is specified in column_position only.
-  Check [example-1](#with header) for other details.

<a name="static value"></a>
##__3. Static value__:
Apart from loading data from file, we can add extra information for each record. Example given below inserts user name and from which db this data is taken as extra information.

### Data file content:
```CSV
IND, userid1, 04/1/2014, facebook, 20, X20, 583230, 8.1
USA, userid2, 03/18/2014, twitter, 27, X2, 5832, 6.4
UK, userid3, 01/9/2014, twitter, 21, X3, 5833, 4.3
UK, userid4, 01/2/2014, facebook, 16, X9, 5839, 5.9
IND, userid5, 08/20/2014, twitter, 37, X10, 583130, 9.3
```
### Config file content:
```JSON
{
  "version" : "1.0",
  "input_type" : "csv",
  "csv_style": { "delimiter": "," , "n_columns_datafile": 8, "ignore_first_line": false},


  "key": {"column_position":2, "type": "string"},

  "set": { "column_position":4 , "type": "string"},

  "binlist": [
    
    {
     "name": "name",
     "value": {"column_position": 6, "type" : "String"}
    },
    {
      "name": "load_from",
      "value": "xyz database"
    }
    
  ]
}
```
### Explanation:
- Extra information load_from xyz database is added to each record. This is called as static bin mapping.

<a name="options"></a>
##__4. Option usage__


* With all default values, run aerospike loader as follows:

```java

	./run_loader -c ~/pathto/config.json ~/pathto/data.csv

```

* Use list of data files for loading:

```java

	./run_loader -c ~/pathto/config.json data1.csv data2.csv data3.csv

```

* Use directory name of data files for loading:

```java

	./run_loader -c ~/pathto/config.json data/

```
* Specify time zone of the location from where data dump is taken. Its optional, if source and destination are same.

```java

	./run_loader -c ~/pathto/config.csv -T PST data/

```

* Specify write action for existing records:

```java

	./run_loader -c ~/pathto/config.csv -wa update data/

```

