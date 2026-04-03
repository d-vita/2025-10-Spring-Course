FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /url-service

ENV JAVA_OPTS="-XX:InitialRAMPercentage=25.0 -XX:MaxRAMPercentage=75.0"

COPY --from=build /build/target/*.jar url-service.jar

EXPOSE 8081

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar url-service.jar"]