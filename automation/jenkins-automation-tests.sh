#!/bin/bash

if [ -z "${FC_ENV}" ]; then
    echo "FATAL ERROR: FC_ENV undefined."
    exit 1
fi

if [ -z "${ENV}" ]; then
    echo "FATAL ERROR: ENV undefined."
    exit 2
fi

TEST_IMAGE=automation-ontology
VAULT_TOKEN=$(cat /etc/vault-token-dsde)

# Render Configs
./render-local-env.sh ${FC_ENV} ${ENV}

# Build docker image
docker build -f Dockerfile -t ${TEST_IMAGE} .

# run tests
docker run ${TEST_IMAGE}
TEST_EXIT_CODE=$?

# do some cleanup after
#sudo chmod -R 777 logs

# exit with exit code of test script
exit $TEST_EXIT_CODE
