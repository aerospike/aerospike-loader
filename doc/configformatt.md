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

