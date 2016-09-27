# Change log
## 1.0-SNAPSHOT
### Added
* `/` return the app name, description and version
* `/version` return the version of the web service
* User authentication with [stormpath](http://stormpath.com)
* `/poll/create?choices={choices}` Create a new poll (authenticated users only)
* `/poll/createTokens/{pollID}?count={count}` Create new tokens for a poll (Authenticated admin of the poll only)
* `/poll/vote/{tokenID}/{secret}?ballot={ballot}` Submit a ballot and consume the vote-token
* Swagger to document the API (UI at `/swagger-ui.html`)