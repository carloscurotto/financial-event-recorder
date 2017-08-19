Usage

Package command

mvn clean package

Run command

java [configuration parameters] -jar target/financial-event-recorder-1.0-SNAPSHOT.jar

Possible configuration parameters are:

-Dreader-type=[file, snmp] (only one value is allowed at a time). Note: the snmp option is not allowed yet!.
-Dwriter-type=[console, database] (only one value is allowed at a time)

The default value for the configuration parameters are:
reader-type=file
writer-type=console