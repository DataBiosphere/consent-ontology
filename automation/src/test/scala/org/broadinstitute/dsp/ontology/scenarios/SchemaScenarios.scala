package org.broadinstitute.dsp.ontology.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.broadinstitute.dsp.ontology.scenarios.GroupedScenarios._
import org.broadinstitute.dsp.ontology.TestRunner
import org.broadinstitute.dsp.ontology.requests.Requests
import org.broadinstitute.dsp.ontology.factories.DataUseFactory

class SchemaScenarios extends Simulation with TestRunner {

  runScenarios(
    List(
      GroupedScenario("Schema: DataUse") { exec(Requests.Schemas.dataUseSchema) },
    )
  )

}
