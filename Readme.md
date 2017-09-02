# Description

This tool works in a very similar way than a copy program. It reads data from an input source and writes it to
an output source. We call them the reader and the writer respectively. The tool allows you to configure which reader
and writer to use from a few possible options described below.

# Usage

## Package command

mvn clean package

## Run command

java -jar target/financial-event-recorder.jar [configuration parameters]

## Possible configuration parameters are:

### Writer
--writer=[console, database] (only one value is allowed at a time)

## Database Writer
To write events to the database you need to edit the corresponding database **connection parameters** in the **application.properties** configuration file placed in the **config folder** in the root of the project.

The **default value** for the **configuration parameters** are:
writer=database

## Complete commands to run are:

#### Write to database
```
java -jar target/financial-event-recorder.jar
```

#### Write to console
```
java -jar target/financial-event-recorder.jar --writer=console
```