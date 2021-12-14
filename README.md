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
* Aerospike Java client 4.3.0 or greater
* Apache commons cli 1.2
* Log4j 2.15.0
* Junit 4.4
* Json-simple 1.1.1

<a name="Usage"></a>
## Usage
Use **run_loader** script along with options and data files.  
    
        $ ./run_loader <options> <data file name(s)/directory>

"data file name(s)/directory" can either be space delimited files or a directory name containing data files. See "Data Files" section for more details.

__Options__:

``` java
-h,--hosts <arg>                List of seed hosts (default: localhost)
-p,--port <arg>                 Server port (default: 3000)
-U,--user <arg>                 User name
-P,--password <arg>             Password
-n,--namespace <arg>            Namespace (default: test)
-c,--config <arg>               Column definition file in JSON format
-g,--max-throughput <arg>       Set a target max transactions per second for the loader (default: 0 (don`t limit TPS)).
-T,--transaction-timeout <arg>  Transaction timeout in milliseconds for write (default: no timeout)
-e,--expiration-time <arg>      Time to expire of a record in seconds (default: never expire)
-tz,--timezone <arg>            TimeZone of source where datadump is taken (default: local timeZone)
-ec,--abort-Error-Count<arg>    Abort when error occurs more than this value (default: 0 (don`t abort))
-wa,--write-Action <arg>        Write action if key already exists (default: update)
-tls,--tls-enable               Use TLS/SSL sockets(default: False)
-tp,--tls-protocols             Allow TLS protocols. Values:  TLSv1,TLSv1.1,TLSv1.2 separated by comma (default: TLSv1.2)
-tlsCiphers,--tls-cipher-suite  Allow TLS cipher suites. Values:  cipher names defined by JVM separated by comma (default: null (default cipher list provided by JVM))
-tr,--tls-revoke                Revoke certificates identified by their serial number. Values:  serial numbers separated by comma (default: null (Do not revoke certificates))
-te,--tls-encrypt-only          Enable TLS encryption and disable TLS certificate validation
-uk,--send-user-key             Send user defined key in addition to hash digest to store on the server. (default: userKey is not sent to reduce meta-data overhead)
-u,--usage                      Print usage.
-v,--verbose                    Verbose mode for debug logging (default: INFO)
-V,--version                    Print version
```

For more details, refer to [Options](doc/options.md).

### Some extra info about internal working:

* There are 2 types of threads:
    * reader threads (reads CSV files) (The number of reader threads = either number of CPUs or number of files in the directory, whichever one is lower.)
    * writer threads (writes to the cluster) (The number of writer threads = number of CPUs * 5 (5 is scaleFactor))

### Sample usage of all options:

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
