FROM maven:3.6.3-openjdk-15 AS MAVEN_BUILD

# copy the pom and src code to the container
COPY ./ ./

# package our application code
RUN mvn -s settings.xml package

FROM openjdk:15

# copy only the artifacts we need from the first stage and discard the rest
COPY --from=MAVEN_BUILD /target/DBSchemaManagerBackend-0.0.1.jar /home/app/backend/backend.jar

WORKDIR /home/app/backend

# set the startup command to execute the jar
CMD ["java", "-jar", "/home/app/backend/backend.jar"]