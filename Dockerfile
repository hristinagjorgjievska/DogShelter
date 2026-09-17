FROM maven:3.9-eclipse-temurin-21 AS build_phase

WORKDIR /build_phase

COPY pom.xml .

RUN mvn -B dependency:go-offline

COPY src ./src

RUN mvn -B clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

RUN addgroup -S app && adduser -S app -G app

WORKDIR /app

COPY --from=build_phase /build_phase/target/*.jar app.jar

USER app

HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=3 \
    CMD wget -qO- http://localhost:8080/actuator/health || exit 1

CMD ["java", "-jar", "app.jar"]