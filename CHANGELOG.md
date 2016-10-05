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

#### Support of voting methods
* [Schulze](https://en.wikipedia.org/wiki/Schulze_method)
* [Condorcet](https://en.wikipedia.org/wiki/Condorcet_method)
* [Relative majority](https://en.wikipedia.org/wiki/Plurality_(voting)#Majority_versus_plurality)

#### Other
* API documentation with Swagger (UI at `/swagger-ui.html`)
* Automatically delete expired polls (a poll expire 30 days after it creation)