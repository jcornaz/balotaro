# Balotaro
[![Build Status](https://travis-ci.org/slimaku/balotaro.svg?branch=master)](https://travis-ci.org/slimaku/balotaro)

Web service to create poll and vote using the condorcet method

## Run the server
### Setup the authentication system
1. Get an API key from [Stormpath](https://stormpath.com)
2. Save the key in your home directory in the following location:
    * `~/.stormpath/apiKey.properties` on Unix, Linux and Mac OS
    * `C:\Users\YOUR_USERNAME\.stormpath\apiKey.properties` on Windows
3. Change the file permissions to ensure only you can read this file and not accidentally write or modify it. For example:
    * `chmod go-rwx ~/.stormpath/apiKey.properties`
    * `chmod u-w ~/.stormpath/apiKey.properties`
4. Create `./src/main/resources/application.properties` with the following :
    * `stormpath.application.href = <HREF>` (replace <HREF> by your stormpath application href)
    
***If you encounter problems look at this [tutorial](https://docs.stormpath.com/java/spring-boot-web/quickstart.html).***

### Run the server
`./gradlew bootRun`

The service will be accessible at http://localhost:8080
