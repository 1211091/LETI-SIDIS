# README #


## Running multiple instances ##

Predefined run configurations are provided under /sidis-1211091-1211064/.run
These include two instances of each module (UsersAPI, SubsAPI and PlansAPI) as required by the project.

#

To change or define more instances do the following:



Open IntelliJ's 'Run/Debug Configurations', select a configuration 
(or create a new one with one of the modules if none exists),
go to 'Modify options' and do the following:
* Override configuration properties:
  * Name: spring.data.source.url, Value: <<your_db_location>>
* Allow multiple instances
* Add VM options
  * -Dserver.port=<<port_number>>


### Ports to use ###

The ports for each instance should be defined in the .env ait the project root
and must coincide with the ones used in the run configuration for each instance.

## Databases ##
Databases must use H2 Driver 1.4.200 to run correctly.

Databases can not be shared between instances or modules. Each instance must run its own database.

### Bootstrapping ###
You may find a 'Bootstrap.sql' file under each module's resources folder.