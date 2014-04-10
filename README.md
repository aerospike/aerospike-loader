#Aerospike Loader
> Aerospike Loader is a tool which parses a set of files with known format, and load the data into Aerospike server. Currently the tool supports CSV files.

- [Prerequisites](#Prerequisites)
- [Dependencies](#Dependencies)
- [Installation](#Installation)
- [Usage](#Usage)
    - [Options](#Options)
    - [Config file](doc/configformatt.md) 
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

| Option                    | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        | Default             |
|---------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------|
| -h <host>                 | Host that acts as seed (entry point) to the cluster. Any single node can be specified and the entire cluster will be automatically discovered.                                                                                                                                                                                                                                                                                                                                                                     | 127.0.0.1           |
| -p <port>                 | Port to use with the host specified in the -h option.                                                                                                                                                                                                                                                                                                                                                                                                                                                              | 3000                |
| -s <set>                  | Set name. Its Optional. We can get set name from command-line or from data file.                                                                                                                                                                                                                                                                                                                                                                                                                                   | null                |
| -n <namespace>            | Namespace to load data to.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         | test                |
| -wt <write-threads>       | Number of writer threads to use on the local machine running the Aerospike-Loader. Increasing the number of threads will make the loading process faster, but setting the value too high may overload the local machine.                                                                                                                                                                                                                                                                                           | Number of cores * 5 |
| -rt <read-threads>        | Number of reader threads to use on the local machine running the Aerospike-Loader. Increasing the number of threads will make the reading process faster, but setting the value too high may overload the local machine. Usually reader-threads are faster than writer-threads.                                                                                                                                                                                                                                    | Number of cores * 1 |
| -wa <write-action>        | Write action if key already exists. The options can be one of following:UPDATE: Create or update record. Merge incoming bins with existing bins.UPDATE_ONLY: Update record only. Fail if record does not exist. Merge incoming bins with existing bins.REPLACE: Create or replace record.REPLACE_ONLY: Replace record only. Fail if record does not exist.CREATE_ONLY: Create only.  Fail if record exists.                                                                                                        | UPDATE              |
| -tt <transaction-timeout> | Timeout for a transaction while doing write operation. Timeout is specified in milliseconds.                                                                                                                                                                                                                                                                                                                                                                                                                       | 0 (no timeout)      |
| -et<expiration-time>      | Expiration time of a record in seconds. -1 means never expire. Zero means use server default.                                                                                                                                                                                                                                                                                                                                                                                                                      | -1                  |
| -T <TimeZone>             | Timezone of source where data dump is taken. This option is used while loading timestamp type data. So if data dump location and destination is different, then you have to specify the source(where dump is taken) timestamp. E.g if data dump is taken from location X and we load data into server located in Y, then we have to specify X's timezone. You can specify 3 letter timezone like IST or PST . E.g. "-T PST" command line option is used to load data from a file which is taken from PST timezone. |  local timezone     |
| -ec <error_count>         | Error count is a counter to stop aerospike-loader after certain number of errors. Zero error_count means don't halt with any error.                                                                                                                                                                                                                                                                                                                                                                                | 0                   |
| -c <config>               | JSON formatted configuration file specifying parsing attributes (eg delimiter) and schema mapping.                                                                                                                                                                                                                                                                                                                                                                                                                 |                     |
| -v                        | Verbose mode.  If this option is specified, verbose mode is enabled and additional information is displayed on the console.                                                                                                                                                                                                                                                                                                                                                                                        | DISABLED            |
| -u                   | Display command usage.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |                     |

	
##Example

	./run_loader -h 127.0.0.1 -p 3000 -n test -s demo -c src/test/resources/columns.json src/test/resources/data.csv
