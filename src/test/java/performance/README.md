# Performance Testing

Provides a mechanism for running simple performance based API tests

## Local development process

1. Update configuration to point to your desired environment. For localhost, it would be `https://local.broadinstitute.org:28443` 
2. Spin up a local instance in a terminal window using a proper docker compose file
3. Run gatling tests against that environment: `mvn gatling:test`
4. Run the results formatter to parse the generated `stats.json` into a BQ-import-friendly format: `mvn compile -Dexec.mainClass="performance.ResultsFormatter" -Dexec.classpathScope=test exec:java`

## Nightly
These tests are configured to run against the `perf` environment by default and post results to QA