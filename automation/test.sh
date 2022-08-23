#!/usr/bin/env bash
set -e
set -o pipefail

mvn gatling:test
TEST_EXIT_CODE=$?

cp target/gatling/testrunner-*/js/stats.json target/classes

mvn exec:java -Dexec.mainClass="org.broadinstitute.dsp.ontology.performance.results.ResultsFormatter" -Dexec.classpathScope="test"

if [[ $TEST_EXIT_CODE != 0 ]]; then exit $TEST_EXIT_CODE; fi
