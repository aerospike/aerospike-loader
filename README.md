#Aerospike Loader
> Aerospike Loader is a tool which parses a set of files with known format, and load the data into Aerospike server. Currently the tool supports CSV files.

- [Prerequisites](#Prerequisites)
- [Dependencies](#Dependencies)
- [Installation](#Installation)
- [Usage](#Usage)
    - [Options](doc/options.md)
    - [Config file format](doc/configformat.md)
    - [Data file format](doc/datafileformat.md)
- [Examples](doc/examples.md)
    - [Demo examples](#demoexample)
    - [Detail examples](doc/examples.md)

<a name="Prerequisites"></a>
## Prerequisites
* Java 1.6 or higher
* Maven 2.3.2

<a name="Dependencies"></a>
##Dependencies
Following dependencies are used and downloaded automatically:
* Aerospike Java client 3.0.22 or greater
* Apache commons cli 1.2
* Log4j 1.2.14
* Junit 4.4
* Json-simple 1.1.1

<a name="Installation"></a>
## Installation
* Source code is available on git-hub:

        $ git clone git@github.com:citrusleaf/aerospike-loader.git

* Then build the utility by running following:

        $ cd aerospike-loader
        $ mvn clean install

<a name="Usage"></a>
## Usage
Use **run_loader** script to run this tool using options and data files.  
    
        $ ./run_loader <options> <data file names>
"data file names" can be list of space separated files, or a directory name containing data files. See "Data Files" section later.


__Options are__:

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

###Sample example for use of all options:
Following command runs aerospike loader onto aerospike server. Server ip is nodex(-h nodex) and port to use is 3000(-p 3000). Data will be inserted into namespace test(-n test) under set name demo(-s demo). Aerospike loader uses 4 reader thread(-rt 4) to read data from 4 different files and 20 writter thread(-wt 20) to write parallelly to aerospike server. The write operation timeout is 3000 mili seconds(-tt 3000). This operation will stop after getting 100 errors(-ec 100). Every record is loaded with expiration time of 30 days(-et 2592000). Timezone is PST where the data dump is taken(-T PST). Write action is update the record if it already exists(-wa update). config.json contains data mapping information(-c ~/pathto/config.json ) . datafiles/ contain all the data dump files.

        $ ./run_loader -h nodex -p 3000 -n test -s demo -tt 3000 -et 3600 -et 2592000 -ec 100 -rt 4 -wt 20 -T PST -wa update -c ~/pathto/config.json datafiles/

A variety of example applications are provided in the examples directory. See the examples/README.md for details.


<a name="demoexample"></a>
## Demo example
Example directory contains two file one is allDatatype.json and data.csv. Run following command to load data from data file.

    ./run_loader -h localhost -c example/allDatatype.json example/data.csv


