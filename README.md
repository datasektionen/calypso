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

All necessary environment variables are set automatically when running with docker compose.

| Name                         | Description                          | Default                           | Example                                  |
| ---------------------------- | ------------------------------------ | --------------------------------- | ---------------------------------------- |
| LOGIN_KEY                    | Login key                            | ---                               | ---                                      |
| LOGIN_FRONTEND_URL           | URL from browser to login            | https://login.datasektionen.se    | http://localhost:7002/                   |
| LOGIN_API_URL                | URL from backend to login            | https://login.datasektionen.se    | http://login:7002/                       |
| APPLICATION_URL              | URL where calypso is hosted          | ---                               | http://localhost.datasektionen.se:8080   |
| JDBC_DATABASE_URL            | URL to database                      | ---                               | jdbc:postgresql://localhost:5432/calypso |
| JDBC_DATABASE_USERNAME       | Database username                    | ---                               | user                                     |
| JDBC_DATABASE_PASSWORD       | Database password                    | ---                               | password                                 |
| S3_BUCKET_NAME               | Amazon AWS s3 bucket name            | ---                               | dsekt-awesome-bucket-name                |
| S3_ACCESS_KEY_ID             | Amazon AWS IAM access key id         | ---                               | ABCDEFGHJIKLMNOPQRST                     |
| S3_SECRET_ACCESS_KEY         | Amazon AWS IAM secret access key     | ---                               | abcdefghjiklmnopqrstuvxyz123456789secret |
| DARKMODE_URL                 | URL to darkmode or `true` or `false` | https://darkmode.datasektionen.se | http://localhost:2000                    |
