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
- TOOLS-2346 Upgrade Java client to 6.1.6. 
  - Aerospike Java client to 6.1.6 is only compatible with Aerospike server versions 4.9 or newer.
- TOOLS-2322 Write key ordered maps by default.
### New features:
- TOOLS-2347  Add -um, --unorderedMaps flags.
  - Forces all maps to be written as unorderd maps. This was standard before the 3.0.0 asloader release.

## Aerospike Loader [4.0.0] Release Date [12 Apr 2023]
## Breaking Changes:
* TOOLS-2469 \(ASLOADER\) Float data type loses precision, change to double.
   * Asloader 4.0.0 will parse data specified as "float" in the JSON spec as Java doubles. This means you may see changes in the precision of floating point values when compared to previous versions of asloader. Parsing as double matches the precision of the [Aerospike double data type](https://docs.aerospike.com/server/guide/data-types/scalar-data-types#double) which is what all floats are stored as in the Aerospike database.

## Security:
* TOOLS-1669 Handle CVE-2020-9488 in asloader.
* TOOLS-1670 Handle CVE-2020-15250 in asloader.

## Bug Fixes:
* TOOLS-2469 \(ASLOADER\) Float data type loses precision, change to double.

## Updates:
* [Snyk] Upgrade com.aerospike:aerospike-client from 6.1.6 to 6.1.7 by @snyk-bot
* [DOCS-1320] [Snyk] Upgrade org.apache.logging.log4j:log4j-core from 2.17.1 to 2.19.0 by @snyk-bot
* [DOCS-1320] [Snyk] Upgrade commons-cli:commons-cli from 1.2 to 1.5.0 by @snyk-bot
* [TOOLS-1670] [TOOLS-1690] Bump junit from 4.11 to 4.13.1 by @dependabot

## Aerospike Loader [4.0.1] Release Date [7 Aug 2023]
## Security:
* [Snyk] Security upgrade com.aerospike:aerospike-client from 6.1.7 to 7.0.0 by @arrowplum in https://github.com/aerospike/aerospike-loader/pull/40
  * TOOLS-2640 fix [CVE-2023](https://aerospike.atlassian.net/browse/TOOLS-2640)

## Aerospike Loader [4.0.2] Release Date [15 Jan 2024]
## Security
* [Snyk] Upgrade org.apache.logging.log4j:log4j-api from 2.20.0 to 2.21.0
* [Snyk] Upgrade org.apache.logging.log4j:log4j-core from 2.20.0 to 2.21.0
* [Snyk] Upgrade commons-cli:commons-cli from 1.5.0 to 1.6.0

## Bug Fixes:
* TOOLS-2826 \(ASLOADER\) Fixed an issue where ldap users fail read-write role validation.
Asloader no longer checks if the Aerospike user has read-write permissions before beginning writes.

## Aerospike Loader [4.0.3] Release Date [7 Aug 2024]
## Security
* [Snyk] fix: upgrade org.apache.logging.log4j:log4j-core from 2.21.0 to 2.22.1
* [Snyk] fix: upgrade org.apache.logging.log4j:log4j-api from 2.21.0 to 2.22.1
* [Snyk] fix: upgrade org.apache.logging.log4j:log4j-api from 2.21.0 to 2.22.1

## Bug Fixes:
* [TOOLS-2690] \(ASLOADER\) Set client policy maxConnsPerNode to the max amount of worker threads to prevent out of connection errors.