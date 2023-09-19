FROM amazoncorretto:11-alpine3.18 AS base

WORKDIR /app

FROM base AS builder

RUN apk add maven

COPY pom.xml system.properties ./

RUN mvn -DskipTests clean dependency:list install

COPY src src

RUN mvn -DskipTests clean dependency:list install

FROM base AS runner

COPY --from=builder /app/target/calypso-1.0-SNAPSHOT.jar .

CMD ["java", "-jar", "calypso-1.0-SNAPSHOT.jar"]
