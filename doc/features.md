#Aerospike load functionality:

- From initial experiments we found the performance of the loader is loading 260-270k rows per second per machine on a CPU MHz: 1600.000, 12 core machine. 
- There is a config file that will specify the schema / data layout of the input strings, and the output into the Aerospike cluster. This data schema specification is in JSON.
- Input data can support arbitrary specifiers (specified in the schema description), and the data type of the input entry (string, integer, float, timestamp, blob). 
- Timestamps, which are specified on input, can be specified through the configuration file to either map to an integer or to a timestamp-formatted string.
- Binary fields should be specified on input as [ hex data. Hex data is a format where every byte is represented as two characters (digit-digit) ] 
- Columns in the CSV can be referenced by name (using the first line of the input CSV), or by position (in the case where no first line with names is available).
- Output to Aerospike must specify which input columns are mapped to which bins, in which type (integer and string). An input CSV column might be placed into a different bin name, or might not be written(skipped) as output to Aerospike.
- Along with loading data from data file we can add extra information for each record like when created or from which database loaded.
- Separate thread pools are working for reading records from data file and writing them to Aerospike.
- The input CSV filename, cluster destination, output set, output namespace, and timeout are specified as command-line input. 
- From command line you specify whether a new row should be written, or whether a read-modify-write updates existing rows (overwriting or adding new column values if the row already exists). This allows a particular schema / transform to be re-used, and the data to be inserted into different sets.
- Error handling. The tool runs in two modes. One halts when an error is encountered (like wrong formatted data in input, or a failure in writing like timeout or cluster unavailable). It writes diagnostic information to a log file including which row was not written and the error encountered, and continues.
