# Country Routing application

## Building the application with Maven
`mvn clean install`

## Running it locally
`mvn spring-boot:run`

## Accessing GET endpoint in browser
- Positive case - route found: http://localhost:8080/routing/CZE/ITA
- Negative case - no land route: http://localhost:8080/routing/CZE/USA

## Postman collection
Can be used for testing, see at: `postman/Country-Routing-collection.postman_collection.json`

## JUnit test for routing logic
See class: `RoutingServiceTest.java`
