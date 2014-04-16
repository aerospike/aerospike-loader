# Configuration file
>The Aerospike-loader configuration file specifies the schema mapping of the source data set to the Aerospike database, as well as specifies attributes to help parse the source data files. Format of this file is in JSON. To write configuration file we need preliminary information from data file like column header and type of data.

- [Sample Config](#config)
- [Keywords Supported](#keyword)
  - [csv_style attributes](#csv_style)
  - [key/set attribute](#key/set)
  - [binlist attributes](#binlist)

<a name="config"></a>
## Sample configuration file:
``` c
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
| version    | Version of Aerospike loader. Current version is 1.0                                                          | Required                               | "1.0"                    | No attributes                                                             |
| input_type | input_type is to specify the format of data file. Currently only "csv" is supported.                         | Required                               | "csv"                    | No attributes                                                             |
| csv_style  | csv_style is used for csv formatted data.                                                                    | Required ( only if input_style is csv) | list of attribute values | delimiter, n_columns_datafile, ignore_first_line                          |
| key        | Key mapping from data file.                                                                                  | Required                               | list of attribute values | choice( column_position, column_name), type                               |
| set        | Set name mapping from data file. Set name can be provided from command line. Set name is always string type. | Optional                               | list of attribute values | choice( column_position, column_name)                                     |
| binlist    |  List of bin mapping  from data file.                                                                        | Required                               | Array of lists           | No direct attributes. Each list in array has two attributes: NAME, VALUE . |

<a name="csv_style"></a>
### csv_style Attributes:

| Keywords          	| Description                                                                                  	| Required/ Optional        	| Value                                                                        	|
|-------------------	|----------------------------------------------------------------------------------------------	|---------------------------	|------------------------------------------------------------------------------	|
| delimiter         	| delimiter is used to separate data in each row of data file.                                 	| Optional (default is ',') 	| any single character. Data part should not contain this delimiter character. 	|
| columns           	| Number of columns in data file.                                                              	| Required                  	| Integer                                                                      	|
| ignore_first_line 	| This attribute is used to skip first line of data file where header information is present.  	| Required                  	| "true","false".                                                              	|

<a name="key/set"></a>
### key/Set Attributes:

| Keywords                     | Description                                                                | Required/ Optional                             | Value           |
|------------------------------|----------------------------------------------------------------------------|------------------------------------------------|-----------------|
| column_position/ column_name | Column position number in data file or column name in header of data file. | Require one of column_position/ column_name. | integer/ string |
| type                        | Type of key/set. Set name data should be string.                           | Require                                  | string          |
<a name="binlist"></a>
### Binlist Attributes:
"binlist" contains array of lists. So there is no direct attributes. Each list in binlist has two attributes one is "name"(name mapping for each bin) and other one is "value"(value mapping for each bin). In following table some sub attributes for "name/value" is described. "name" attribute doesn't have dst_type and encoding attribute, and type is string. "name/value" can have static/fixed values or we can pick name/value from data file. Length of each bin name should be less than or equal to 14.

| Keywords                     | Description                                                                                                                                                                                    | Required/ Optional                                            | Value            |
|------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------|------------------|
| column_position/ column_name | Column name in header of data file or column position.                                                                                                                                         | Require any one of column_position/ column_name               | integer / string |
| type                         | Data type of source data. Supported data types are: integer, float, string, blob, timestamp.                                                                                                   | Require                                                       | string           |
| dst_type                     | Source type data to aerospike type conversion. Supported data types are: integer, string, blob. Timestamp can be stored as integer/string, float is stored as 8 byte encoded byte array(blob). | Require if source type to destination type conversion needed. | string           |
| encoding                     | Encoding format for data conversion from source to destination type. Blob type data  should be hex encoded. Timestamp type data can be encoded as "MM/DD/YYYY" if dst_type is integer.        | Require if dst_type is given                                  | string           |
