Calypso
=======

**News and event management system for Datasektionen.se**

Spring Boot with Spring MVC + Spring Data JPA + Spring Security + Thymeleaf + PostgreSQL.

## Development

1. Configure environment variables
2. `mvn -DskipTests clean dependency:list install`
3. `java -jar target/calypso-1.0-SNAPSHOT.jar`

-----------

Run with `-Dspring.profiles.active=dev` to use `application-dev.properties`
for development configuration. All config in this file will override the global
`application.properties`

## Environment variables

A login API-key can be received by [Systemansvarig](mailto:d-sys@d.kth.se)

| Name                         | Description                 | Default                 | Example                                  |
| ---------------------------- | --------------------------- | ----------------------- | ---------------------------------------- |
| LOGIN_KEY                    | Login key                   | ---                     | ---                                      |
| APPLICATION_URL              | URL to backend              | ---                     | http://localhost.datasektionen.se:8080   |
| JDBC_DATABASE_URL            | URL to database             | ---                     | jdbc:postgresql://localhost:5432/calypso |
| JDBC_DATABASE_USERNAME       | Database username           | ---                     | user                                     |
| JDBC_DATABASE_PASSWORD       | Database password           | ---                     | password                                 |