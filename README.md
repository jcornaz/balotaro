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

### Use it
When running, you can find the API documentation at [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

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

**Important note** : For a production use, make sure to use **only** https.

[More informations about authentication with Stormpath and Spring boot](http://docs.stormpath.com/java/spring-boot-web/http-request-authentication.html) 

#### Create a poll
Hit `/poll/create` with at least some *choices* as argument :
```bash
curl -u LOGIN:PASSWORD -d "{ 'choices': ['lundo', 'mardo', 'merkredo', 'ĵaŭdo', 'vendredo'], 'tokenCount': 3 }" http://localhost:8080/poll/create
```

It would return the poll id and tokens :
```json
{
	"poll": {
		"id": "57ed744820337a09b8f47464",
		"createdDate": 1475179592293,
		"timeToLive": 43200,
		"choices": ["lundo", "mardo", "merkredo", "ĵaŭdo", "vendredo"]
	},
	
	"tokens": [
		{ "id": "57ed744820337a09b8f47465", "secret": "fqnsvbnjjdanocgcl26jjcurnq" },
		{ "id": "57ed744820337a09b8f47466", "secret": "h7misrbkqlq7vc7k1cq3pp774g" },
		{ "id": "57ed744820337a09b8f47467", "secret": "reji7pn2ar8ahlfuj2og1p8v4h" }
	]
}
```

#### The vote tokens
Tokens have two part (*id* and *secret*) and are needed to vote. They are specific fore the poll and cannot be used of an another. Each token allow to make exactly one vote (not more).

You can create more tokens for an existing poll with `/poll/generateTokens` :
```bash
curl -u LOGIN:PASSWORD -d "{ tokenCount: 10 }" http://localhost:8080/poll/generateTokens
```

#### Vote
Hit `/vote` with an unused token and your ballot (choices ordered by preferences) :
```bash
curl -u LOGIN:PASSWORD -d "{ tokenID: <my_token_id>, tokenSecret: <my_token_secret>, ballot: ['mardo', 'merkredo', 'vendredo'] }" http://localhost:8080/vote
```

##### Notes
* You can specify status quo between candidates.
* You can omit candidates. (They will be considerated as equally undesired result)