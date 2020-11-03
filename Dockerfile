FROM adoptopenjdk/maven-openjdk11 AS build
WORKDIR app
COPY src src
COPY pom.xml .
RUN mvn clean package

FROM adoptopenjdk/openjdk11:alpine-jre
COPY --from=build app/target/enrollment-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 80
ENTRYPOINT ["sh", "-c", "java -Dclient.members.url=${MEMBERS_URL} -Dstripe.api.key=${STRIPE_API_KEY} -Dspring.kafka.bootstrap-servers=${BOOTSTRAP_SERVERS} -jar /app.jar"]