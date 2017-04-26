#Aerospike Loader
> Aerospike Loader parses a set of .DSV files and loads the data into Aerospike server.

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
* Source code is available on github:

        $ git clone git@github.com:aerospike/aerospike-loader.git

* Then build the utility by running following:

        $ cd aerospike-loader
        $ ./build

<a name="Dependencies"></a>
##Dependencies
Following dependencies are downloaded automatically:
* Aerospike Java client 3.1.2 or greater
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
-h,--hosts <arg>                List of seed hosts (default: localhost)
-p,--port <arg>                 Server port (default: 3000)
-U,--user <arg>                 User name
-P,--password <arg>             Password
-n,--namespace <arg>            Namespace (default: test)
-c,--config <arg>               Column definition file in JSON format
-g,--max-throughput <arg>       Set a target max transactions per second for the loader (default: 0 (don't limit TPS)).
-T,--transaction-timeout <arg>  Transaction timeout in milliseconds for write (default: no timeout)
-e,--expiration-time <arg>      Time to expire of a record in seconds (default: never expire)
-tz,--timezone <arg>            TimeZone of source where datadump is taken (default: local timeZone)
-ec,--abort-Error-Count<arg>    Abort when error occurs more than this value (default: 0 (don't abort))
-wa,--write-Action <arg>        Write action if key already exists (default: update)
-u,--usage                      Print usage.
-v,--verbose                    Verbose mode for debug logging (default: INFO)
-tls,--tlsEnable                Use TLS/SSL sockets(default: False)
-tp,--tlsProtocols              Allow TLS protocols. Values:  SSLv3,TLSv1,TLSv1.1,TLSv1.2 separated by comma (default: TLSv1.2)
-tlsCiphers,--tlsCipherSuite    Allow TLS cipher suites. Values:  cipher names defined by JVM separated by comma (default: null (default cipher list provided by JVM))
-tr,--tlsRevoke                 Revoke certificates identified by their serial number. Values:  serial numbers separated by comma (default: null (Do not revoke certificates))
-te,--tlsEncryptOnly            Enable TLS encryption and disable TLS certificate validation
```

For more details, refer to [Options](doc/options.md).

###Sample usage of all options:

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

    ./run_loader -h localhost -c example/allDatatype.json example/data.csv

For more examples, see the examples/README.md.
