#!/usr/bin/env bash
set -e

## Run from automation/
## Pulls templatized configs and renders them to src/test/resources

# Defaults
WORKING_DIR=$PWD
FC_INSTANCE=fiab
VAULT_TOKEN=$(cat ~/.vault-token)
ENV=dev
SERVICE=consent-ontology

# Parameters
FC_INSTANCE=${1:-$FC_INSTANCE}
VAULT_TOKEN=${2:-$VAULT_TOKEN}
ENV=${3:-$ENV}
SERVICE_ROOT=${4:-$SERVICE}

SCRIPT_ROOT=${SERVICE_ROOT}/automation

render_configs() {
    original_dir=$WORKING_DIR
    docker pull broadinstitute/dsde-toolbox:dev

    # render application.conf
    docker run -it --rm -e VAULT_TOKEN=${VAULT_TOKEN} \
        -e ENVIRONMENT=${ENV} -e FC_INSTANCE=${FC_INSTANCE} -e ROOT_DIR=${WORKING_DIR} \
        -e OUT_PATH=/output/src/test/resources -e INPUT_PATH=/input \
        -v $PWD/configs:/input -v $PWD:/output \
        broadinstitute/dsde-toolbox:dev render-templates.sh
    cd $original_dir
}

if [[ $PWD != *"${SCRIPT_ROOT}" ]]; then
    echo "Error: this script needs to be running from the ${SCRIPT_ROOT} directory!"
    exit 1
fi

render_configs
