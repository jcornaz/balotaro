# Balotaro
[![Build Status](https://travis-ci.org/slimaku/balotaro.svg?branch=master)](https://travis-ci.org/slimaku/balotaro)

Wen service to create poll and vote using the condorcet method.

It is developed in [Kotlin](https://kotlinlang.org) with [Spring Boot](https://projects.spring.io/spring-boot).

## Run the server
You need a jdk 8 or newer

### Setup a StormPath authentication
1. Get an API key from [Stormpath](https://stormpath.com)
2. Save the key in your home directory in the following location:
    * `~/.stormpath/apiKey.properties` on Unix, Linux and Mac OS
    * `C:\Users\YOUR_USERNAME\.stormpath\apiKey.properties` on Windows
3. Change the file permissions to ensure only you can read this file and not accidentally write or modify it. For example:
    * `chmod go-rwx ~/.stormpath/apiKey.properties`
    * `chmod u-w ~/.stormpath/apiKey.properties`

If you have many stormpath applications, add the folowing in the `./src/main/resources/application.properties` file :
```
stormpath.application.href = your_application_href_here
``` 

[Stormpath with spring source tutorial](https://docs.stormpath.com/java/spring-boot-web/quickstart.html)

### Setup a MongoDB server
1. Install a mongodb server

If not on the same machine or not a standard installation add the relevant lines in the `./src/main/resources/application.properties` file :
```
spring.data.mongodb.authentication-database= # Authentication database name.
spring.data.mongodb.database=test # Database name.
spring.data.mongodb.field-naming-strategy= # Fully qualified name of the FieldNamingStrategy to use.
spring.data.mongodb.grid-fs-database= # GridFS database name.
spring.data.mongodb.host=localhost # Mongo server host.
spring.data.mongodb.password= # Login password of the mongo server.
spring.data.mongodb.port=27017 # Mongo server port.
spring.data.mongodb.repositories.enabled=true # Enable Mongo repositories.
spring.data.mongodb.uri=mongodb://localhost/test # Mongo database URI. When set, host and port are ignored.
spring.data.mongodb.username= # Login user of the mongo server.
```

[MongoDB with spring boot tutorial](https://spring.io/guides/gs/accessing-data-mongodb/)

### Run the server
* Run the mongodb server `mongod`
* Run spring boot `./gradlew bootRun`

The service root endpoint will be : [http://localhost:8080](http://localhost:8080)
The API documentation will be available at `[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
