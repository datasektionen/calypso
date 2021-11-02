Calypso
=======

**News and event management system for Datasektionen.se**

Spring Boot with Spring MVC + Spring Data JPA + Spring Security + Thymeleaf + PostgreSQL.

## Development

1. Configure database connection in `resources/application.properties`.
```
spring.datasource.url=jdbc:postgresql://localhost:5432/DB_NAME
spring.datasource.username=USER
spring.datasource.password=PSWD
```
Do not commit these changes

2. `mvn -DskipTests clean dependency:list install`
3. `java -jar target/calypso-1.0-SNAPSHOT.jar`

-----------

Run with `-Dspring.profiles.active=dev` to use `application-dev.properties`
for development configuration. All config in this file will override the global
`application.properties`

## Environment variables

| Name                         | Description                 | Default                 | Example                                |
| ---------------------------- | --------------------------- | ----------------------- | -------------------------------------- |
| LOGIN_KEY                    | Login key                   | ---                     | ---                                    |
| APPLICATION_URL              | URL to backend              | ---                     | http://localhost.datasektionen.se:8080 |