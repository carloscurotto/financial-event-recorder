# Description

This tool reads events from an snmp socket and stores them in a database or console (depending on how it is configured).
It also allows you to synchronize the events from a file located in the configuration folder to populate event that
might be missed from the socket, since it uses a udp connection.

# Usage

## Package command

mvn clean package

## Run command

java -jar target/financial-event-recorder.jar [configuration parameters]

## Possible configuration parameters are:

### Writer
--writer=[console, database] (only one value is allowed at a time)

## Console Writer
To write events to the console you need to pass the **--writer=console** parameter to the application.

## Database Writer
To write events to the database you need to pass the **--writer=database** parameter to the application. Check the corresponding database **connection parameters** in the **application.properties** configuration file placed in the **config folder** in the root of the project.

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