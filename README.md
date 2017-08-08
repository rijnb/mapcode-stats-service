# README for Mapcode REST API Web Services

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/8911cd282272497d9e5047887f64a270)](https://www.codacy.com/app/rijnb/mapcode-stats-service?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=rijnb/mapcode-stats-service&amp;utm_campaign=Badge_Grade)
[![Build Status](https://img.shields.io/travis/mapcode-foundation/mapcode-stats-service.svg?maxAge=3600&branch=master)](https://travis-ci.org/mapcode-foundation/mapcode-stats-service)
[![Coverage Status](https://coveralls.io/repos/github/mapcode-foundation/mapcode-stats-service/badge.svg?branch=master&maxAge=3600)](https://coveralls.io/github/mapcode-foundation/mapcode-stats-service?branch=master)
[![License](http://img.shields.io/badge/license-APACHE2-blue.svg)]()
[![Release](https://img.shields.io/github/release/mapcode-foundation/mapcode-stats-service.svg?maxAge=3600)](https://github.com/mapcode-foundation/mapcode-stats-service/releases)

**Copyright (C) 2014-2017 Stichting Mapcode Foundation (http://www.mapcode.com)**

This application provides a visualization REST API for the mapcode service statistics.
It uses the Java Library for Mapcodes extensively. 


### Build and Run

The service always runs from a WAR file.
To build the WAR file, type

```bash
    cd {project-root}
    mvn clean package
```

You can run the WAR file in 3 ways:

1. directly from the **command-line**, using:

```bash
    java -jar deployment/target/stats-{version}.war [--port {port}] [--silent] [--debug] [--help]
```

  This will start the service at `http://localhost:<port>/stats`. If `<port>` is not specified, the
  default value is `8080`. If it is `0`, the server will choose any free port.

2. directly from **Maven** using:
 
 ```bash
     cd deployment
     mvn jetty:run
 ```
     
  This will start the service at `http://localhost:8080/stats`.

3. in a **Tomcat server**, deploying the file `deployment/target/stats-<version>.war` into
your Tomcat instance.

The first method, running the WAR file from the command-line, using `java` only is particularly
useful if you wish use the XML services, for example, in a Microsoft Excel spreadsheet.


### Missing `stats-secret.properties` and `log4j.xml` Files

The service requires 2 files called `stats-secret.properties` and `log4j.xml` to be present on the
classpath. They are specifically **not** included in the WAR file by default, because that would
make it impossible to change them without recompiling the service.


#### `log4j.xml`

The file `log4j.xml` specifies the log levels during operations. An example of a `log4j.xml` file
can be found in `resources/src/main/external-resources-test/log4j.xml`. 

Make sure that file can be found on the classpath
or add it `resources/src/main/external-resources` before building and it will be integrated in the WAR file.


#### `stats-secret.properties`
 
The properties file `stats-secret.properties` contains the username and password for
your MongDB database server for tracing, should you wish to use that.

If you get a start-up error complaining about a missing `stats-secret.properties` file,
make sure you add it to the classpath (or add it to `resources/src/main/external-resources`) before building.

By default, you can simply use an empty `stats-secret.properties` file. So, you may want to
use the example file as a starting point:

```bash
    cd resources/src/main
    cp external-resources-test/* external-resources/
```

This will copy an example `log4j.xml` and `stats-secret.properties` file to your 
resources.

Note that the files in `external-resources` are ignored by Git in `.gitignore`.

If you wish to use MongoDB tracing, will need to provide your own local
`stats-secret.properties`, which override the following properties:

```properties
    MongoDBTrace.writeEnabled = false
    MongoDBTrace.servers = your-server:27017 (eg. localhost:27017)
    MongoDBTrace.database = your-database (eg. trace)
    MongoDBTrace.userName = your-username
    MongoDBTrace.password = your-password
```

The service will work with an empty file as well, but will not trace events to the
database.


### Using Java 8 on MacOSX

The source uses Java JDK 1.8, so make sure your Java compiler is set to 1.8, for example
using something like (MacOSX):

```bash
    export JAVA_HOME=`/usr/libexec/java_home -v 1.8`
```

### Smoke Testing The REST API

Try out if the web services work by entering the following URL in your web browser
(this should show you a HTML help page):

```bash
    http://localhost:8080/stats
    http://localhost:8080/stats/version
    http://localhost:8080/stats/clusters/-80,-100,80,100
```
Or use a tool like cURL:

```bash
    curl -X GET http://localhost:8080/stats
    curl -X GET http://localhost:8080/stats/version
    curl -X GET http://localhost:8080/stats/clusters/-80,-100,80,100
```

## Setting up a test trace database in MongoDB

The trace database for testing needs to have authentication enabled.
Use the username `test` and password `test` for that, like this, 
in the `mongo` shell:
 
```
    use trace
    db.createUser({user: "test", pwd: "test", roles: ["readWrite"]})
```

## Using Git and `.gitignore`

It's good practice to set up a personal global `.gitignore` file on your machine which filters a number of files
on your file systems that you do not wish to submit to the Git repository. You can set up your own global
`~/.gitignore` file by executing:
`git config --global core.excludesfile ~/.gitignore`

In general, add the following file types to `~/.gitignore` (each entry should be on a separate line):
`*.com *.class *.dll *.exe *.o *.so *.log *.sql *.sqlite *.tlog *.epoch *.swp *.hprof *.hprof.index *.releaseBackup *~`

If you're using a Mac, filter:
`.DS_Store* Thumbs.db`

If you're using IntelliJ IDEA, filter:
`*.iml *.iws .idea/`

If you're using Eclips, filter:
`.classpath .project .settings .cache`

If you're using NetBeans, filter:
`nb-configuration.xml *.orig`

The local `.gitignore` file in the Git repository itself to reflect those file only that are produced by executing
regular compile, build or release commands, such as:
`target/ out/`


## Bug Reports and New Feature Requests

If you encounter any problems with this library, don't hesitate to use the `Issues` session to file your issues.
Normally, one of our developers should be able to comment on them and fix.


## Release Notes

### 0.0.1
 
* Initial version.
