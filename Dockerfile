FROM maven:3.5-jdk-8 AS mvn-build
WORKDIR /build
ADD . /build
RUN mvn clean verify

FROM openjdk:8-jre
WORKDIR /app
COPY --from=mvn-build /build/parameter-service/target/parameter-service-*.jar /app/parameter-service.jar
EXPOSE 8090
ENV SERVER_PORT=8090
CMD ["java", "-jar", "parameter-service.jar"]
