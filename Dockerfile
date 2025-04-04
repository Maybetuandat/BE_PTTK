## buidl stage ##
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY . .
RUN mvn install -DskipTests=true

#run stage#
FROM alpine/java:21-jre
WORKDIR /run
COPY --from=build /app/target/*.jar /run//app.jar
EXPOSE 8080
ENTRYPOINT java -jar /run/app.jar