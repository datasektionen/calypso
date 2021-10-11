Calypso
=======

**News and event management system for Datasektionen.se**

Spring Boot with Spring MVC + Spring Data JPA + Spring Security + Thymeleaf + PostgreSQL.

## Development

`mvn -DskipTests clean dependency:list install`
`java -jar target/calypso-1.0-SNAPSHOT.jar`

-----------

Run with `-Dspring.profiles.active=dev` to use `application-dev.properties`
for development configuration. All config in this file will override the global
`application.properties`

## Environment variables
LOGIN_KEY
APPLICATION_URL