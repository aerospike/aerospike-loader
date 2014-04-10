# Configuration file
  > The Aerospike-loader configuration file specifies the schema mapping of the source data set to the Aerospike database, as well as specifies attributes to help parse the source data files.
Format of this file is in JSON.

## Keywords Supported in Config file:

| Keywords   | Description                                                                                                  | Required/ Optional                     | Value                    | Attributes                                                                |
|------------|--------------------------------------------------------------------------------------------------------------|----------------------------------------|--------------------------|---------------------------------------------------------------------------|
| VERSION    | Version of Aerospike loader. Current version is 1.0                                                          | Required                               | "1.0"                    | No attributes                                                             |
| INPUT_TYPE | input_type is to specify the format of data file. Currently only "csv" is supported.                         | Required                               | "csv"                    | No attributes                                                             |
| CSV_STYLE  | csv_style is used for csv formatted data.                                                                    | Required ( only if input_style is csv) | list of attribute values | delimiter, n_columns_datafile, ignore_first_line                          |
| KEY        | Key mapping from data file.                                                                                  | Required                               | list of attribute values | choice( column_position, column_name), type                               |
| SET        | Set name mapping from data file. Set name can be provided from command line. Set name is always string type. | Optional                               | list of attribute values | choice( column_position, column_name)                                     |
| BINLIST    |  List of bin mapping  from data file.                                                                        | Required                               | Array of lists           | No direct attibutes. Each list in array has two attributes: NAME, VALUE . |

### CSV_STYLE Attributes:

| Keywords          	| Description                                                                                  	| Required/ Optional        	| Value                                                                        	|
|-------------------	|----------------------------------------------------------------------------------------------	|---------------------------	|------------------------------------------------------------------------------	|
| DELIMITER         	| delimiter is used to separate data in each row of data file.                                 	| Optional (default is ',') 	| any single character. Data part should not contain this delimiter character. 	|
| COLUMNS           	| Number of columns in data file.                                                              	| Required                  	| Integer                                                                      	|
| IGNORE_FIRST_LINE 	| This attribute is used to skip first line of data file where header information is present.  	| Required                  	| "true","false".                                                              	|

### Key/Set Attributes:

| Keywords        	| Description                                      	| Required/ Optional                            	| Value   	|
|-----------------	|--------------------------------------------------	|-----------------------------------------------	|---------	|
| COLUMN_POSITION 	| Column position number in data file.             	| Required if COLUMN_NAME is not specified.     	| integer 	|
| COLUMN_NAME     	| Column name in header of data file.              	| Required if COLUMN_POSITION is not specified. 	| string  	|
| TYPE            	| Type of key/set. Set name data should be string. 	| Require                                       	|   string      	|

### Binlist Attributes:
