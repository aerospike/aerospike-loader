#Example

- [ Datafile example](#data sample)
- [ Config file example] (#config sample)
- [ Option usage example] (#option usage)


<a name="data sample"></a>
## Datafile example
> Datafile is any CSV formatted data, dumped into a file. Sample data of a CSV file is given below. First row contains the header information(column names) and next three row contains data. Each row contains 4 columns. 

```c
user_location,userid,last_visited, set
India, userid1, 08/16/2011, facebook
India, userid2, 08/17/2011, Tweeter
USA, userid3, 08/16/2011, Tweeter
```


<a name="config sample"></a>
## Config file example
Configuration file is used to map data in datafile to store in aerospike server. Below are quick guide lines to write config file.

**1. Sample config file for above data file:**

```json
{
  "version" : "1.0",
  "input_type": "csv",
  "csv_style": {
    "delimiter": ",",
    "n_columns_datafile": 4,
    "ignore_first_line": true
  },
  "key": {
    "column_position": 2,
    "type": "string"
  },
  "set": {
    "column_position": 4
  },
  "binlist": [
    {
      "name": "location",
      "value": {
        "column_position": 1,
        "type": "string"
      }
    },
    {
      "name": "recent_visit",
      "value": {
        "column_position": 3,
        "type": "string"
      }
    }
  ]
}
```

**2. We can give static values for a record.**
E.g. we can add extra information for a record. To do that add following to binlist.

```json
    {
      "name": "load_from",
      "value": "xyz database"
    }
```

**3. To use header information from data file use column_name. It will use column name for mapping.**

```json
	"value": {
            "column_name": "last_visited",
            "type": "string"
      	}
```
**4. To load timestamp type data we have to specify dst_type(native aerospike type), and encoding format.**
>  **Note: Timestamp can be stored as integer of seconds.**

**Timestamp-Integer**

```json
	"value": {
            "column_position": 3,
            "type": "timestamp",
            "dst_type": "integer",
            "encoding" : "MM/dd/yyyy"
        }
```

**Timestamp-String**

```json
	"value": {
            "column_position": 3,
            "type": "timestamp",
            "dst_type": "string",
            "encoding" : "MM/dd/yyyy"
        }
```

**5. To load blob type data we have to specify dst_type(native aerospike type), and encoding format.**

```json
	"value": {
            "column_position": 3,
            "type": "blob",
            "dst_type": "blob",
            "encoding" : "hex"
        }
```

<a name="option usage"></a>
## Option usage example


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
* Specify timezone of the location from where data dump is taken. Its optional, if source and destination are same.

```java

	./run_loader -c ~/pathto/config.csv -T PST data/

```

* Specify write action for existing records:

```java

	./run_loader -c ~/pathto/config.csv -wa update data/

```

