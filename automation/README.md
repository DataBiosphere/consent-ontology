# Automation Testing

Tests are designed to be run in two ways. Manually from the command line, or through automation scripts.

References:
* [Gatling Documentation](https://gatling.io/docs/current/)
* [Gatling SBT Plugin](https://github.com/gatling/gatling-sbt-plugin-demo)

## Automated Testing

```
../jenkins/run-integration-tests.sh
```

## Run Tests

First, render configs:
```bash
./render-local-env.sh [fc instance] [vault token] [env]
```

**Arguments:** (arguments are positional)

* FC Instance 
  * defaults to `fiab`
  * can be `live`, `local`, or `fiab`
* Vault Auth Token
  * Defaults to reading it from the .vault-token via `$(cat ~/.vault-token)`.
* env
  * Environment of your FiaB; defaults to `dev`

### Run all tests:
```
sbt gatling:test 
```

### Run specific tests:
```
sbt gatling:testOnly *.DefaultScenarios 
```

## Development
To create new tests, use `org.broadinstitute.dsp.ontology.DefaultScenarios` as a model. 
All tests extending `Simulation` will be run during normal test running. 
`org.broadinstitute.dsp.ontology.requests.Requests` contains a summary of existing API calls supported
by the application. 
