# Consent Ontology Services

[![Build Status](https://travis-ci.com/broadinstitute/consent-ontology.svg?token=3ve6QNemvC5zpJzsoKzf&branch=develop)](https://travis-ci.com/broadinstitute/consent-ontology)

This repository contains all ontology-related services

## Local Development

Check out repository:
```bash
git clone git@github.com:broadinstitute/consent-ontology.git
```

Build and render Configs:
```bash
cd consent-ontology
mvn clean compile
APP_NAME=consent-ontology ENV=local OUTPUT_DIR=config ./configure.rb
```

Spin up application:
```bash
docker-compose -p consent-ontology -f config/docker-compose.yaml up
```

Visit local swagger page: https://local.broadinstitute.org/swagger/

### Debugging
Port 5005 is open in the configured docker compose. 
Set up a remote debug configuration pointing to `local.broadinstitute.org`
and the defaults should be correct.

Execute the `fizzed-watcher:run` maven task (under consent plugins) 
to enable hot reloading of class and resource files.