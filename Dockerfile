FROM maven:3.8.4-openjdk-17-slim AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package

FROM openjdk:17-alpine

WORKDIR /app

COPY --from=builder /app/target/NotifcationVertx-1.0-SNAPSHOT-jar-with-dependencies.jar /app/app.jar

RUN curl -sSf https://temporal.download/cli.sh | sh

EXPOSE 9198

CMD ["temporal", "server", "start-dev","--ui-port", "9199" ]
CMD ["java", "-jar", "app.jar"]

















































#FROM maven:3.8.4-openjdk-17-slim AS builder
#
#WORKDIR /app
#
#COPY pom.xml .
#COPY src ./src
#
#RUN mvn clean package
#
#FROM openjdk:17-alpine
#
#
#WORKDIR /app
#
## Copy the built JAR file from the builder stage
#COPY --from=builder /app/target/NotifcationVertx-1.0-SNAPSHOT-jar-with-dependencies.jar /app/app.jar
#
#
#EXPOSE 9198
#
#CMD ["temporal", "server" , "start-dev" ]
#CMD ["java", "-jar", "app.jar"]


## Base image
#FROM openjdk:17-alpine
#
#COPY pom.xml .
#
#RUN apk add --no-cache maven
#
#RUN mvn dependency:go-offline
#
#WORKDIR /app
#
#COPY target/NotifcationVertx-1.0-SNAPSHOT-jar-with-dependencies.jar /app/app.jar
#
#EXPOSE 9198
#
#CMD ["java", "-jar", "app.jar"]
