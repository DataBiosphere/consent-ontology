# Automation Testing

Tests are designed to be run in two ways. Manually from the command line, or through automation scripts.

References:
* [Gatling Documentation](https://gatling.io/docs/current/)
* [Gatling SBT Plugin](https://github.com/gatling/gatling-sbt-plugin-demo)

## Automated Testing


## Manual Testing

### Run all tests:
```
sbt gatling:test 
```

### Run specific tests:
```
sbt gatling:testOnly *.DefaultScenarios 
```
