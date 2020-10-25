FROM adoptopenjdk/maven-openjdk11 AS build
COPY src app/src
COPY pom.xml app/
RUN mvn -f app/pom.xml clean package

FROM adoptopenjdk/openjdk11:alpine-jre
COPY --from=build app/target/enrollment-service-0.0.1-SNAPSHOT.jar app/app.jar
WORKDIR app
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]