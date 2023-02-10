# Smoke/Performance Testing

Provides a mechanism for running simple smoke/performance focused API tests. 
Gatling test results can (and will) be parsed and uploaded to capture performance
statistics over time.

## Local development/testing process

1. Update configuration (`/src/test/resources/performance.conf`) to point to your preferred environment. For localhost, it would be `https://local.broadinstitute.org:28443` 
2. Spin up a local instance in a terminal window using a proper docker compose file
3. Run gatling tests against that environment: `mvn gatling:test`

## TODO
1. We need to run these tests against an on-demand environment. Capabilities for that are in development now.
2. Test results will then need to be pushed to a QA BQ database where daily test result reports are generated.
3. Remove all `/automation` code and the jenkins job that runs the old automation suite.