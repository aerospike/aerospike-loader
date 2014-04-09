#Aerospike Loader
> Aerospike Loader is a tool which parses a set of files with known format, and load the data into Aerospike server. Currently the tool supports CSV files.

- [Prerequisites](#Prerequisites)
- [Dependencies](#Dependencies)
- [Installation](#Installation)
- [Usage](#Usage)
    * [Options](#Options)
- [Documentation](https://aerospike.atlassian.net/wiki/display/~jyoti/Documentation+for+Aerospike+Loader)


<a name="Prerequisites"></a>
## Prerequisites
* Java 1.6 or higher
* Maven 2.3.2

<a name="Dependencies"></a>
##Dependencies
Following dependencies are used:
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
Use run_loader script to run this tool using options and data file.  
    
        $ run_loader <options> <data file names>
"data file names" can be list of space separated files, or a directory name containing data files. See "Data Files" section later.
Different options are explained below:

<a name="Options"></a>
### Options:
The command options for aerospike-loader are:

	-c,--config <arg>                Column definition file name
	-ec,--abort-error-count <arg>    Error count to abort (default: 0)
	-et,--expiration-time <arg>      Expiration time of records in seconds(default: never expire)
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
	
##Example

	./run_loader -h 127.0.0.1 -p 3000 -n test -s demo -c src/test/resources/columns.json src/test/resources/data.csv
