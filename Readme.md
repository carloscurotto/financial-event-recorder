# Description

This tool works in a very similar way than a copy program. It reads data from an input source and writes it to
an output source. We call them the reader and the writer respectively. The tool allows you to configure which reader
and writer to use from a few possible options described below.

# Usage

## Package command

mvn clean package

## Run command

java [configuration parameters] -jar target/financial-event-recorder.jar

## Possible configuration parameters are:

### Reader
-Dreader-type=[file, snmp] (only one value is allowed at a time).

**Note: the snmp option is not allowed yet!!.**

### Writer
-Dwriter-type=[console, database] (only one value is allowed at a time)

The **default value** for the **configuration parameters** are:
reader-type=file
writer-type=console

## Complete commands to run are:

#### Read from file and write to console
```
java -jar target/financial-event-recorder.jar
```

#### Read from file and write to database
```
java -Dwriter-type=database -jar target/financial-event-recorder.jar
```

#### Read from snmp and write to console
```
java -Dreader-type=snmp -jar target/financial-event-recorder.jar
```

#### Read from snmp and write to database
```
java -Dreader-type=snmp -Dwriter-type=database -jar target/financial-event-recorder.jar
```
