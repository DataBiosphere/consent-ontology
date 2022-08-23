# Automation Testing

Tests are designed to be run in two ways. Manually from the command line, or through automation scripts.

References:
* [Gatling Documentation](https://gatling.io/docs/current/)

## Automated Testing (Local Development)
 
Update the configuration for which environment to test against in `src/test/resources/performance.conf`

Example for running tests locally against the dev environment:

### Run all tests:
From the project root directory:
```
mvn clean gatling:test 
```

### Run all tests under docker:
From the project root directory:
```
docker build -t automation-ontology:latest -f ./automation/Dockerfile .
```

## Development
To create new tests, use `Status` as a model. All tests extending `Simulation` will be run 
by default. `Endpoints` contains a summary of existing supported APIs. 
