# Balotaro
[![GPLv3](https://img.shields.io/badge/license-GPLv3-blue.svg)](https://raw.githubusercontent.com/slimaku/balotaro/master/LICENSE)
[![Build Status](https://travis-ci.org/slimaku/balotaro.svg?branch=master)](https://travis-ci.org/slimaku/balotaro)
[![Codebeat](https://codebeat.co/badges/04435b31-0902-4b20-b315-138e4e7d8562)](https://codebeat.co/projects/github-com-slimaku-balotaro)
[![Issues](https://img.shields.io/github/issues/slimaku/balotaro.svg)](https://github.com/slimaku/balotaro/issues)
[![Pull requests](https://img.shields.io/github/issues-pr/slimaku/balotaro.svg)](https://github.com/slimaku/balotaro/pulls)

RESTFul Web service to vote on any subject using the [Condorcet method](https://en.wikipedia.org/wiki/Condorcet_method).

## Setup
You need a jdk 8 or newer

### MongoDB
Install a mongodb server.

If MongoDB is not on the same machine or has not a standard installation add the relevant lines in the `./src/main/resources/application.properties` file :
```properties
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

Run it : `mongod`

### Launch the web service
Run it with gradle : `./gradlew bootRun`

The service root endpoint will be : [http://localhost:8080](http://localhost:8080)

## Use it
When running, you can find a complete API documentation at [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

All requests require and produces JSON.

### Create a poll
Make a `POST /poll/create` request with at least some *choices* as argument :
```bash
curl -X POST -H "Content-Type: application/json" -H "Accept: application/json" -d "{ \"candidates\": [\"lundo\", \"mardo\", \"merkredo\", \"ĵaŭdo\", \"vendredo\" }, \"method\": \"schulze\" }" "http://localhost:8080/poll/create"
```

I would return this kind of JSON : 
```json
{
  "poll": "V_EaDe3BMA6I5E2MZG1wOExHwoAnGjuVzfmUvg==",
  "tokens": [
    "V_EaDe3BMA6I5E2NYuSIfPZ992g3xRgAGmqqkQ==",
    "V_EaDe3BMA6I5E2OAIhdj0iwUKKO5UXyhHfHai8=",
    "V_EaDe3BMA6I5E2PaXDBP4wwXyRW8UWFpcPxtw=="
  ]
}
```

#### Supported voting method
* [Schulze](https://en.wikipedia.org/wiki/Schulze_method) (default, if not specified)
* [Condorcet](https://en.wikipedia.org/wiki/Condorcet_method)
* [Relative majority](https://en.wikipedia.org/wiki/Plurality_(voting)#Majority_versus_plurality)

### Vote tokens
Tokens are needed to vote. They are specific fore the poll and cannot be used of an another. Each token allow to make exactly one vote (not more).

You can create more tokens for an existing poll with `POST /poll/createTokens` using the poll id and secret returned by `POST /poll/create` :
```bash
curl -X POST -H "Content-Type: application/json" -H "Accept: application/json" -d "{ \"poll\": \"V_EaDe3BMA6I5E2MZG1wOExHwoAnGjuVzfmUvg==\" }" "http://localhost:8080/poll/createTokens"
```

It will return a list of token like that :
```json
[
  "V_EcGO3BMBSM2y9PAJiQehuGoMNM70d9nURDdaM=",
  "V_EcGO3BMBSM2y9QORlCHeipCkclf86EEw3MeA==",
  "V_EcGO3BMBSM2y9RP2bBdrs2nszGSn8I-CtVnw=="
]
```

#### Unsafe poll
When creating a poll, you can specify `"secure": false` in the JSON argument. If you do that, the poll will have only vote token (not more, not less) and the vote token can be used many times.

### Vote
Make a `POST /vote` request with an unused token and your ballot (choices ordered by preferences) :
```bash
curl -X POST -H "Content-Type: application/json" -H "Accept: application/json" -d "{ \"candidates\": [[\"mardo\"], [\"lundo\", \"ĵaŭdo\"], [\"vendredo\"]], \"token\": \"V_EcGO3BMBSM2y9RP2bBdrs2nszGSn8I-CtVnw==\" }" "http://localhost:8080/vote/"
```

Note that `candidates` is list of list ordered from the most preferred candidates to the least preferred.
You can omit candidates. (They will be considerated as equally not preferred)

### Exemples of lists :
* `[[A], [B], [C]]` :  **A** is preferred to **B** which is preferred to **C**
* `[[A, B], [C]]` : **A** and **B** are equally preferred, but both are preferred to **C**
* `[[A]]` : **A** Is the preferred. **B** and **C** are equally no preferred (omitted)

### Close poll
Make a `DELETE /poll/close` request with the poll token (not a vote token)
```bash
curl -X DELETE -H "Content-Type: application/json" -H "Accept: application/json" -d "{ \"poll\": \"DFfyef3EGHwMjJjGAwCFv9QQujqd6rCUFw9vmc7-\" }"     "http://localhost:8080/poll/close"
```

It will return a list of the candidates ordered from the winner(s) to the losers :
```json
[["mardo"], ["lundo", "ĵaŭdo"], ["vendredo"], ["lundo", "merkredo"]]
```
