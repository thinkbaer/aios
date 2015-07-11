# Aios server


## Arguments

### Server
Syntax
```
-Daios.{argument}=value
```

Argument |  Default value |  Description
--- | --- | ---
host | localhost | host or address for the server
port | 8118 | port for the server

### Logging

see https://logging.apache.org/log4j/2.x/manual/configuration.html

```
-Dlog4j.configurationFile={path/to/log4j2/config/file}
```

## Build

```
$ mvn clean package
```

## Run with maven

```
$ mvn exec:java -Dexec.mainClass="de.thinkbaer.aios.server.ServerMain"
```

## Run with aios.sh

```
$ sh build/aios-dist/bin/aios.sh
```

