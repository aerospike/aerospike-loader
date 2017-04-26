#Release notes:


-  1.0: Initial implementation
-  1.1: Insert system time along with the record.

##Aerospike Loader [2.0] Release Date [....]
###New features:
-   Added Json datatype support (List, Maps can be nested also.)
-   Added TLS security support
-	Added mapping section. user can mention any number of mappings in one file. User can add secondary_mapping (used for any column to primary key mapping.) by adding attribute secondary_mapping: 'true'.
-	Added (-g) (max-throughput) to limit max average throughput of loader.
###Fix/Changes:
-   Removed Specific List/Map datatype option as supported type. Json datatype will cover them.
-   Removed CSV specific options from config file. There will be only DSV support (covers CSV also.)
-   Changed parameter options to be consistent with Aerospike-java-client parameters.
-   Added simple examples explaing all datatype and config options simply.
-   Deprecated -s (--set) commandline param. Will be passed only by config file.
-   Deprecated -wt (write-threads), -rt (read-threads) and introduce new option -g defining max throughput for loader.
-	Changed schema for mapping definitions.

