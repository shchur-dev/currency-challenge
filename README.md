# To Start Currency Exchange and Conversion locally

### Build tools
* Gradle 7.3

### Language used
* Java 17

### Guides
* clone this repo (to use SSH, run from cmd `git clone git@github.com:shchur-dev/currency-challenge.git`)
* run `gradle build` from cmd (OR if using IntelliJ IDE run `build` from the IDE Gradle panel)
* run either `./gradlew bootRun` from cmd, or `bootRun` from your IDE Gradle menu

### OPEN API
OPEN API UI is reachable from 
http://localhost:8080/swagger-ui/index.html#/currency-ops-controller

### POSTMAN
Sample cURL's
* Get all exchange rates from Currency A
`curl --location 'http://localhost:8080/currency?source=USD'`

* Get exchange rate from Currency A to Currency B
`curl --location 'http://localhost:8080/rate?from=USD&to=ILS'`

* Get value conversion from Currency A to Currency B
  `curl --location 'http://localhost:8080/convert?from=GBP&to=USD&amount=1'`

* Get value conversion from Currency A to a list of supplied currencies
`curl --location --request PUT 'http://localhost:8080/rates?source=USD' \
--header 'Content-Type: application/json' \
--data '["AUD", "CHF"]'`

