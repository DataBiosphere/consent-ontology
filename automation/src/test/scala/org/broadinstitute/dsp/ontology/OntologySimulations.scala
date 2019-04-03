package org.broadinstitute.dsp.ontology

import Scenarios._
import io.gatling.core.Predef._

class OntologySimulations extends Simulation {

  setUp(
    scenarioList.map(scn =>
      scn.inject(atOnceUsers(defaultUsers)))
  ).protocols(httpProtocol)

}
