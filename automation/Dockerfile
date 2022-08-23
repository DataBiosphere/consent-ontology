FROM maven:3.8.6-eclipse-temurin-17

RUN mkdir -p /usr/src/app

COPY ../.git /usr/src/app/.git
COPY ../pom.xml /usr/src/app/pom.xml
COPY ../src /usr/src/app/src
COPY ../automation/test.sh /usr/src/app/test.sh

WORKDIR /usr/src/app

RUN mvn -v
RUN mvn clean compile --no-transfer-progress
RUN mvn gatling:test
RUN cp target/gatling/testrunner-*/js/stats.json target/classes
RUN mvn exec:java -Dexec.mainClass="org.broadinstitute.dsp.ontology.performance.results.ResultsFormatter" -Dexec.classpathScope="test"

#ENTRYPOINT ["bash", "test.sh"]
#CMD ["ls", "-al", "target/classes"]
