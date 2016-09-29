# Balotaro
[![Build Status](https://travis-ci.org/slimaku/balotaro.svg?branch=master)](https://travis-ci.org/slimaku/balotaro)
[![GPLv3](https://img.shields.io/badge/license-GPLv3-blue.svg)](https://raw.githubusercontent.com/slimaku/balotaro/master/LICENSE)

RESFull Web service to create to vote on any subject using the condorcet method.

## Run the server
You need a jdk 8 or newer

### Setup the StormPath authentication
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

You may also take a look at the [Stormpath starter documentation for spring boot](https://docs.stormpath.com/java/spring-boot-web)

### Setup a MongoDB server
Install a mongodb server.

If MongoDB is not on the same machine or has not a standard installation add the relevant lines in the `./src/main/resources/application.properties` file :
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

You may also take a look at this [MongoDB with spring boot tutorial](https://spring.io/guides/gs/accessing-data-mongodb/)

### Run the server
* Run the mongodb server `mongod`
* Run spring boot `./gradlew bootRun`

The service root endpoint will be : [http://localhost:8080](http://localhost:8080)

The API documentation will be available at `[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### Use it
For a production use make sure to use **only** https.

#### Register
To create and manage poll you need an account. You can create one at [http://localhost:8080/register](http://localhost:8080/register)

#### Authentication
Some requests need an authentication

You can use Basic authentication. Example :
```bash
curl -u LOGIN:PASSWORD http://localhost:8080/
```

Or you can get beared token at `/oauth/token`. Example :
```bash
# Get a token
curl -X POST -d 'grant_type=password&username=ACCOUNT_USERNAME&password=ACCOUNT_PASSWORD' http://localhost:8080/oauth/token
# Will return something like : { "expires_in": 3600, "token_type": "Bearer", "access_token": "eyJhbGciOiJJhbGciOiJIUzI1NiJ9.eyJqdGkiOi..." }

# Use the token
curl -X POST -H "Authorization: Bearer eyJhbGciOiJJhbGciOiJIUzI1NiJ9..." http://localhost:8080/
```

[More informations](http://docs.stormpath.com/java/spring-boot-web/http-request-authentication.html)