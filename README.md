# Aerospike Loader
> Aerospike Data Loader can help in migrating data from any other database to
> Aerospike. User can dump data from different databases in .DSV format and use
> this tool to parse and load them in Aerospike server. User need to provide
> .DSV data files to load and aerospike schema file in JSON format. It parse
> those .DSV files and load data in Aerospike Server according to given schema
> in schema files.

- [Prerequisites](#Prerequisites)
- [Installation](#Installation)
- [Dependencies](#Dependencies)
- [Usage](#Usage)
    - [Options](doc/options.md)
    - [Config file format](doc/configformat.md)
    - [Data file format](doc/datafileformat.md)
- [Examples](doc/examples.md)
    - [Demo example](#demoexample)
    - [Detailed examples](doc/examples.md)
- [Release Notes](doc/releasenotes.md)

<a name="Prerequisites"></a>
## Prerequisites
* Java 1.8 or greater
* Maven 3.0 or greater

<a name="Installation"></a>
## Installation
* Source code is available on github:

        $ git clone https://github.com/aerospike/aerospike-loader.git

* Then build the utility by running following:

        $ cd aerospike-loader
        $ ./build

<a name="Dependencies"></a>
## Dependencies
Following dependencies are downloaded automatically:
* Aerospike Java client 7.2.1 or greater
* Apache commons cli 1.6.0
* Log4j 2.21.0
* Junit 4.13.1
* Json-simple 1.1.1

<a name="Usage"></a>
## Usage

If you downloaded the jar from [the releases page](https://github.com/aerospike/aerospike-loader/releases). Use

        $ java -cp aerospike-load-*-jar-with-dependencies.jar com.aerospike.load.AerospikeLoad <options> <data file name(s)/directory>

If you downloaded the source. Use **run_loader** script along with options and data files.  
    
        $ ./run_loader <options> <data file name(s)/directory>

"data file name(s)/directory" can either be space delimited files or a directory name containing data files. See "Data Files" section for more details.

For available options and their descriptions run with asloader's --usage option.

        $ java -cp aerospike-load-*-jar-with-dependencies.jar com.aerospike.load.AerospikeLoad --usage
        $ ./run_loader --usage

For more details, refer to [Options](https://aerospike.com/docs/tools/asloader/options).

### Some extra info about internal working:

* There are 2 types of threads:
    * reader threads (reads CSV files) (The number of reader threads = either number of CPUs or number of files in the directory, whichever one is lower.)
    * writer threads (writes to the cluster) (The number of writer threads = number of CPUs * 5 (5 is scaleFactor))

### Sample usage of common options:

        $ ./run_loader -h nodex -p 3000 -n test -T 3000 -e 2592000 -ec 100 -tz PST -wa update -c ~/pathto/config.json datafiles/

Where:

```
Server IP:                                  nodex (-h)
Port:                                       3000 (-p)
Namespace:                                  test (-n) 
Write Operation Timeout (in milliseconds):  3000 (-T)
Write Error Threshold:                      100 (-ec)
Record Expiration:                          2592000 (-e)
Timezone:                                   PST (-tz)
Write Action:                               update (-wa) 
Data Mapping:                               ~/pathto/config.json (-c)
Data Files:                                 datafiles/
```

<a name="demoexample"></a>
## Demo example
Example directory contains two files: allDatatype.json and data.csv. Run the following command to load data from data file data.csv.

    ./run_loader -h localhost -c example/alldatatype.json example/alldatatype.dsv

For more examples, see the examples/README.md.
