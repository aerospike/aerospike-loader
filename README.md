#Aerospike Loader
> Aerospike Loader parses a set of .CSV files and loads the data into Aerospike server.

- [Features](doc/features.md)
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
* Java 1.6 or higher
* Maven 2.3.2

<a name="Installation"></a>
## Installation
* Source code is available on git-hub:

        $ git clone git@github.com:aerospike/aerospike-loader.git

* Then build the utility by running following:

        $ cd aerospike-loader
        $ mvn clean install

<a name="Dependencies"></a>
##Dependencies
Following dependencies are downloaded automatically:
* Aerospike Java client 3.0.22 or greater
* Apache commons cli 1.2
* Log4j 1.2.14
* Junit 4.4
* Json-simple 1.1.1

<a name="Usage"></a>
## Usage
Use **run_loader** script along with options and data files.  
    
        $ ./run_loader <options> <data file name(s)/directory>
"data file name(s)/directory" can either be space delimited files or a directory name containing data files. See "Data Files" section for more details.

__Options__:

``` java
-c,--config <arg>                Column definition file name
-ec,--abort-error-count <arg>    Error count to abort (default: 0)
-et,--expiration-time <arg>      Expiration time of records in seconds (default: never expire)
-h,--host <arg>                  Server hostname (default: localhost)
-n,--namespace <arg>             Namespace (default: test)
-p,--port <arg>                  Server port (default: 3000)
-rt,--read-threads <arg>         Number of reader threads (default: Number of cores * 1)
-s,--set <arg>                   Set name. (default: null)
-T,--timezone <arg>              Timezone of source where data dump is taken (default: local timezone)
-tt,--transaction-timeout <arg>  write transaction timeout in miliseconds(default: No timeout)
-u,--usage                       Print usage.
-v,--verbose                     Logging all
-wa,--write-action <arg>         Write action if key already exists (default: update)
-wt,--write-threads <arg>        Number of writer threads (default: Number of cores * 5)

```

###Sample usage of all options:

        $ ./run_loader -h nodex -p 3000 -n test -s demo -tt 3000 -et 3600 -et 2592000 -ec 100 -rt 4 -wt 20 -T PST -wa update -c ~/pathto/config.json datafiles/

Where:

Server IP: nodex (-h nodex)
Port: 3000 (-p 3000)
Namespace: test (-n test) 
Set: demo (-s demo)
Read Threads: 4 (-rt 4)
Write Threads: 20 (-wt 20)
Write Operation Timeout (in milliseconds): 3000 (-tt 3000)
Write Error Threshold: 100 (-ec 100) -- Write operation will stop once number of errors hits 100
Record Expiration: 30 days (-et 2592000)
Timezone: PST (-T PST)
Write Action: Update, if it already exists (-wa update) 
Data Mapping: File containing data mappings (-c ~/pathto/config.json)
Data Files: Folder containing data giles (datafiles/)

For more details, refer to [Options](doc/options.md).

<a name="demoexample"></a>
## Demo example
Example directory contains two files: allDatatype.json and data.csv. Run the following command to load data from data file data.csv.

    ./run_loader -h localhost -c example/allDatatype.json example/data.csv

For more examples, see the examples/README.md.
