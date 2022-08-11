# Local Development

* Maven 3.8
* Java 17
* Dropwizard Docs: http://www.dropwizard.io/

### Check out repository:
```bash
git clone git@github.com:broadinstitute/consent-ontology.git
```

### Build, test
```bash
cd consent-ontology
mvn clean compile
APP_NAME=consent-ontology ENV=local OUTPUT_DIR=config ../firecloud-develop/configure.rb
```

Tests spin up an embedded http server that run against localhost. 
Ensure that your test environment supports that. 

#### Docker

Consent-Ontology is packaged into a docker image that is stored in GCR: `gcr.io/broad-dsp-gcr-public/consent-ontology`
```
# build the docker image
docker build . -t ontology
```

This image can then be run with the proper configuration files provided.

### Render Configs 
Specific to internal Broad systems:
```bash
APP_NAME=consent-ontology ENV=local OUTPUT_DIR=config ../firecloud-develop/configure.rb
```
Otherwise, use `src/test/resources/config.yml` as a template to 
create your own environment-specific configuration. 

### Spin up application:
Specific to internal Broad systems:
```bash
docker-compose -p consent-ontology -f config/docker-compose.yaml up
```
Or, if not using docker:
```bash
java -jar /path/to/consent-ontology.jar server /path/to/config/file
```

Visit local swagger page: https://local.broadinstitute.org:28443/swagger/

### Debugging
Port 7777 is open in the configured docker compose. 
Set up a remote debug configuration pointing to `local.broadinstitute.org`
and the defaults should be correct.

### Developing with a local ElasticSearch instance:

Update the compose file to include a new section for an ES instance:
```
es:
  image: docker.elastic.co/elasticsearch/elasticsearch:5.5.0
  ports:
    - "9200:9200"
  volumes:
    - ../data:/usr/share/elasticsearch/data
  environment:
    transport.host: 127.0.0.1
    xpack.security.enabled: "false"
    http.host: 0.0.0.0
```
Add a line to the `app` section to link to that:
```
  links:
    - es:es
```
Finally, update the servers in consent.conf to point to this instance:
```
elasticSearch:
  servers:
    - es
  indexName: local-ontology    
```

Consent will now point to a local ES instance. 
