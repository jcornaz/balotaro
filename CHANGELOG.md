# Change log
## 1.0-SNAPSHOT
### Added
#### Web service methods
* `GET /` return the app name, description and version
* `GET /version` return the version of the web service
* `POST /poll/create` Create a new poll (safe or unsafe)
* `POST /poll/createTokens` Create new tokens for a poll
* `POST /vote` Submit a ballot and consume the vote-token
* `DELETE /poll/close` Close a poll and return the result
* Automatically delete expired polls (a poll expire 30 days after it creation)

#### Other
* API documentation with Swagger (UI at `/swagger-ui.html`)