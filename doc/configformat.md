# Configuration file
>The Aerospike-loader configuration file specifies the schema mapping of the source data set to the Aerospike database, as well as specifies attributes to help parse the source data files. Format of this file is in JSON. To write configuration file we need preliminary information from data file like column header and type of data.

- [Sample Config](#config)
- [Keywords Supported](#keyword)
  - [dsv_config attributes](#dsv_config)
  - [mappings attributes](#mappings)
  - [key/set attribute](#key/set)
  - [bin_list attributes](#bin_list)

<a name="config"></a>
## Sample configuration file:
Following config file maps data file having five columns. If first line of data file contains column_names than given column_names can be used for mapping. If column_positions is given than data position can be obtained while processing data file. This config file defines dsv_config and mappings. dsv_config would have delimiter, n_columns_datafile, header_exist info. There are two types of mapping (Primary_mapping, secondary_mapping used for any column to key reverse indexing). Each mapping should have three fields (key, set, bin_list). Here for eg. primary mapping has a key (having column_name 'key'), set (having column_name set) and bin_list having definition for three bins.
``` c
{
    "version" : "2.0",
    "dsv_config": {
        "delimiter": ",",
        "n_columns_datafile": 5,
        "header_exist": true
    },

    "mappings" [
        {
            "key": {
                "column_name": "key",
                "type": "string"
            },
            "set": {
                "column_name": "set"
                "type": "string"
            },
            "bin_list": [
                {
                    "name": "dob",
                    "value": {
                        "column_position": 3,
                        "type": "timestamp",
                        "dst_type": "integer",
                        "encoding" : "MM/dd/yyyy"
                    }
                },
                {
                    "name": "lstblob",
                    "value": {
                        "column_name": "lstblob",
                        "type": "blob",
                        "encoding" : "hex"
                    }
                },
                {
                    "name": "age",
                    "value": {
                        "column_name": "name",
                        "type": "name",
                        "dst_type": "string",
                    }
                }
            ]
        },
        {
            "secondary_mapping": "true",
            "key": {
                "column_name": "name",
                "type": "String"
            },
            "set": "name_map",
            "bin_list": [
                {
                    "name": "name_key",
                    "value": {
                        "column_name": "key",
                        "type": "integer",
                        "dst_type": "cdt_list"
                    }
                }
            ]
        }
    ]
}

```

<a name="keyword"></a>
## Keywords Supported in Config file:

| Keywords   | Description                                                                                                  | Required/ Optional                      | Value                    | Attributes                                                                |
|------------|--------------------------------------------------------------------------------------------------------------|-----------------------------------------|--------------------------|---------------------------------------------------------------------------|
| version    | Version of Aerospike loader. Current version is 2.0                                                          | Required                                | "2.0"                    | No attributes                                                             |
| dsv_config  | dsv_config is used for specifying configs.                                                                    | Required                                | Map of attribute values  | delimiter, n_columns_datafile, header_exist                          |
| mappings   | List of mapping primary and secondary (secondary mapping is used to create a reverse mapping from secondary key. If there are columns other than Primary_key on which user want to create index.) | Required | List of mapping_def map | No direct attributes. Each map in array has four attributes: secondary_mapping (optional boolean. used to specify secondary_mapping), key (as above), set (as above), bin_list (as above in bin_list)
| key (mappingDef attribute)       | Key mapping from data file.                                                                                  | Required                                | Map of attribute values  | choice( column_position/column_name), type                                |
| set (mappingDef attribute)       | Set name mapping from data file. Set name can be provided as static value or dynamic (defined by mapping) in config file. Set name is always string type. | Required | Map of attribute values  | choice( column_position/column_name), type                               |
| bin_list (mappingDef attribute)   | List of bin mapping  from data file.                                                                         | Required                                | List of bin_def map      | No direct attributes. Each map in array has two attributes: name, value (column_position/column_name, type, dst_type, encoding). |

<a name="dsv_config"></a>
### dsv_config Attributes:

| Keywords          	| Description                                                                                  	| Required/ Optional        	| Value                                                                        	|
|-------------------	|----------------------------------------------------------------------------------------------	|---------------------------	|------------------------------------------------------------------------------	|
| delimiter         	| delimiter is used to separate data in each row of data file.                                 	| Optional (default is ',') 	| any string Data part should not contain this delimiter character. 	    |
| n_columns_datafile           	| Number of columns in data file.                                                              	| Required                  	| Integer                                                                      	|
| header_exist 	| This attribute is used to skip first line of data file where header information is present.  	| Required                  	| "true","false".                                                              	|

<a name="mappings"></a>
### mappings Attributes:
mapping is list of primary or secondary mappingDefs. mapping has four attributes.
- secondary_mapping (boolean optional)
- key (map)
- set (string or map)
- bin_list (list of bindefs.)

__Note__: Definition is given below for all attributes.

<a name="key/set"></a>
### key/set Attributes: 
key is unique and always picked from data file. 

| Keywords                     | Description                                                                | Required/ Optional                             | Value           |
|------------------------------|----------------------------------------------------------------------------|------------------------------------------------|-----------------|
| column_position/ column_name | Column position number in data file or column name in header of data file. | Require one of column_position/ column_name.   | integer/ string |
| type                         | Type of key/set. Set name should be string.                                | Require                                        | string          |

<a name="bin_list"></a>
### bin_list Attributes:
"bin_list" contains array of lists. So there is no direct attributes. Each list in bin_list has two attributes one is "name"(name mapping for each bin) and other one is "value"(value mapping for each bin). In following table some sub attributes for "name/value" is described. "name" attribute doesn't have dst_type and encoding attribute, and type is always string. "name/value" can have static/fixed values or we can pick name/value from data file. Length of each bin name should be less than or equal to 14.

| Keywords                     | Description                                                                                                                                                                                    | Required/ Optional                                            | Value            |
|------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------|------------------|
| column_position/ column_name | Column name in header of data file or column position.                                                                                                                                         | Require any one of column_position/ column_name               | integer / string |
| type                         | Data type of source data. Supported data types are: integer, float, string, blob, timestamp.                                                                                                   | Require                                                       | string           |
| dst_type                     | Source type data to aerospike type conversion. Supported data types are: integer, string, blob. Timestamp can be stored as integer/string, float is stored as 8 byte encoded byte array(blob). json (nested list, map can be passed.)| Require if source type to destination type conversion needed and for timestamp and blob case. | string           |
| encoding                     | Encoding format for data conversion from source to destination type. Blob type data  should be hex encoded. Timestamp type data can be encoded as "MM/DD/YYYY" if dst_type is integer.         | Require if dst_type is given                                  | string           |

   __Note:__ Specify column_name:"system_time" in config file to insert extra bin in each record with system time at the time of writing stored in it.
