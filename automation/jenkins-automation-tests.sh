#!/bin/bash

if [ -z "${ENV}" ]; then
    echo "FATAL ERROR: ENV undefined."
    exit 1
fi

TEST_IMAGE=automation-ontology
VAULT_TOKEN=$(cat /etc/vault-token-dsde)

# Build docker image
docker build -f Dockerfile -t ${TEST_IMAGE} .

# run tests
docker run ${TEST_IMAGE}
TEST_EXIT_CODE=$?

# do some cleanup after
#sudo chmod -R 777 logs

# exit with exit code of test script
exit $TEST_EXIT_CODE
