package org.broadinstitute.dsp.ontology.scenarios

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder

object GroupedScenarios {
  def GroupedScenario(name: String)(chain: ChainBuilder) = scenario(name).group(name) {
    exec(chain)
  }
}