# Release notes:

-  1.0: Initial implementation
-  1.1: Insert system time along with the record.

## Aerospike Loader [2.0] Release Date [26 Apr 2017]
### New features:
-   Added Json datatype support (List, Maps can be nested also.)
-   Added TLS security support
-	Added mapping section. user can mention any number of mappings in one file. User can add secondary_mapping (used for any column to primary key mapping.) by adding attribute secondary_mapping: 'true'.
-	Added (-g) (max-throughput) to limit max average throughput of loader.

### Fix/Changes:
-   Removed Specific List/Map datatype option as supported type. Json datatype will cover them.
-   Removed CSV specific options from config file. There will be only DSV support (covers CSV also.)
-   Changed parameter options to be consistent with Aerospike-java-client parameters.
-   Added simple examples explaing all datatype and config options simply.
-   Deprecated -s (--set) commandline param. Will be passed only by config file.
-   Deprecated -wt (write-threads), -rt (read-threads) and introduce new option -g defining max throughput for loader.
-	Changed schema for mapping definitions.

## Aerospike Loader [2.1] Release Date [27 Jun 2017]
### New features:
-   None

### Fix/Changes:
-   Fixed config-name binlist in docs. Changed to bin_list

## Aerospike Loader [2.2] Release Date [12 Feb 2018]
### New features:
-   Added native float support. Earlier float was stored as blob.

### Fix/Changes:
-   Fixed loading for JSON object. Now JSON object format in datafile will be of Standerd JSON. limitation (JSON specific special char ('[', '{', ',', ':') can't be used as delimiter.)
-   Fixed tests.
-   Fix docs related to json spec.
-   Fix examples for JSON object.

## Aerospike Loader [2.3] Release Date [12 Mar 2018]
### New features:
-   Added GeoJson support.

### Fix/Changes:
-   Fixed data upload counters.
-   Fix docs related to json/geojson spec.
-   Fix examples for GeoJSON object.

## Aerospike Loader [2.4] Release Date [19 Oct 2021]
### New features:
-   Add shebang to run_loader script to allow execution from non-bash based shells.

## Aerospike Loader [2.4.1] Release Date [13 Dec 2021]
### Fix/Changes:
-   Update log4j to version 2.15.0.

## Aerospike Loader [2.4.2] Release Date [16 Dec 2021]
### Fix/Changes:
-   Update log4j to version 2.16.0.

## Aerospike Loader [2.4.3] Release Date [4 Jan 2022]
### Fix/Changes:
-   Update log4j to version 2.17.1.

## Aerospike Loader [3.0.0] Release Date [27 Jan 2023]
### Breaking Changes:
-   Upgrade Aerospike Java client to 6.1.6 which is only compatible with Aerospike server 4.9 or newer.
-   Write key ordered maps by default.
### New features:
-   Add -um/--unorderedMaps flags to force writing unordered maps.
