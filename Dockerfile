# Builder
FROM maven:3.8.3-eclipse-temurin-11 AS build

RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app

COPY .git /usr/src/app/.git
COPY pom.xml /usr/src/app/pom.xml
COPY src /usr/src/app/src

RUN mvn clean package -Dmaven.test.skip=true

# Published
FROM us.gcr.io/broad-dsp-gcr-public/base/jre:17-debian
COPY --from=build /usr/src/app/target/consent-ontology.jar /opt/consent-ontology.jar
