# Configuration file
>The Aerospike-loader configuration file specifies the schema mapping of the source data set to the Aerospike database, as well as specifies attributes to help parse the source data files.
Format of this file is in JSON.

- [Sample Config](#config)
- [Keywords Supported](#keyword)
  - [CSV_STYLE attributes](#csv_style)
  - [Key/set attribute](#key/set)
  - [Binlist attributes](#binlist)

<a name="config"></a>
## Sample configuration file:
``` c
{
  "version" : "3.0",
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
      "name": "bin1",
      "value": {
        "column_position": 3,
        "type": "timestamp",
        "dst_type": "integer",
        "encoding" : "MM/dd/yyyy"
      }
    },
    {
      "name": "bin2",
      "value": {
        "column_name": "last_visited",
        "type": "blob",
        "encoding" : "hex"
      }
    }
  ]
}

```

<a name="keyword"></a>
## Keywords Supported in Config file:

| Keywords   | Description                                                                                                  | Required/ Optional                     | Value                    | Attributes                                                                |
|------------|--------------------------------------------------------------------------------------------------------------|----------------------------------------|--------------------------|---------------------------------------------------------------------------|
| VERSION    | Version of Aerospike loader. Current version is 1.0                                                          | Required                               | "1.0"                    | No attributes                                                             |
| INPUT_TYPE | input_type is to specify the format of data file. Currently only "csv" is supported.                         | Required                               | "csv"                    | No attributes                                                             |
| CSV_STYLE  | csv_style is used for csv formatted data.                                                                    | Required ( only if input_style is csv) | list of attribute values | delimiter, n_columns_datafile, ignore_first_line                          |
| KEY        | Key mapping from data file.                                                                                  | Required                               | list of attribute values | choice( column_position, column_name), type                               |
| SET        | Set name mapping from data file. Set name can be provided from command line. Set name is always string type. | Optional                               | list of attribute values | choice( column_position, column_name)                                     |
| BINLIST    |  List of bin mapping  from data file.                                                                        | Required                               | Array of lists           | No direct attibutes. Each list in array has two attributes: NAME, VALUE . |

<a name="csv_style"></a>
### CSV_STYLE Attributes:

| Keywords          	| Description                                                                                  	| Required/ Optional        	| Value                                                                        	|
|-------------------	|----------------------------------------------------------------------------------------------	|---------------------------	|------------------------------------------------------------------------------	|
| DELIMITER         	| delimiter is used to separate data in each row of data file.                                 	| Optional (default is ',') 	| any single character. Data part should not contain this delimiter character. 	|
| COLUMNS           	| Number of columns in data file.                                                              	| Required                  	| Integer                                                                      	|
| IGNORE_FIRST_LINE 	| This attribute is used to skip first line of data file where header information is present.  	| Required                  	| "true","false".                                                              	|

<a name="key/set"></a>
### Key/Set Attributes:

| Keywords                     | Description                                                                | Required/ Optional                             | Value           |
|------------------------------|----------------------------------------------------------------------------|------------------------------------------------|-----------------|
| COLUMN POSITION/ COLUMN_NAME | Column position number in data file or column name in header of data file. | Reaquire one of COLUMN_POSITION / COLUMN NAME. | integer/ string |
| TYPE                         | Type of key/set. Set name data should be string.                           | Require                                        | string          |
<a name="binlist"></a>
### Binlist Attributes:
BINLIST contains array of lists. So there is no direct attributes. Each list in binlist has two attributes one is NAME(name mapping for each bin) and other one is VALUE(value mapping for each bin). In following table some subattributes for NAME/ VALUE described. NAME attribute doesn't have dst_type and encoding attribute, and type is string. NAME/VALUE can have static/fixed values or we can pick name/value from data file. Leanth of each bin name should be less than or equal to 14.

| Keywords                     | Description                                                                                                                                                                                    | Required/ Optional                                            | Value            |
|------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------|------------------|
| COLUMN POSITION/ COLUMN_NAME | Column name in header of data file or column position.                                                                                                                                         | Require any one of COLUMN POSITION/ COLUMN_NAME               | integer / string |
| TYPE                         | Data type of source data. Supported data types are: integer, float, string, blob, timestamp.                                                                                                   | Require                                                       | string           |
| DST_TYPE                     | Source type data to aerospike type conversion. Supported data types are: integer, string, blob. Timestamp can be stored as integer/string, float is stored as 8 byte encoded byte array(blob). | Require if source type to destination type conversion needed. | string           |
| ENCODING                     | Encoding formatt for data conversion from source to destination type. Blob type data  should be hex encoded. Timestamp type data can be encoded as "MM/DD/YYYY" if dst_type is integer.        | Require if DST_TYPE is given                                  | string           |
