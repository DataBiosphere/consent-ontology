package org.broadinstitute.dsde.consent.ontology.enumerations;

import java.util.Arrays;
import java.util.Optional;

public enum TranslateFor {

  DATASET, PURPOSE, PARAGRAPH;

  public static TranslateFor find(final String value) {
    Optional<TranslateFor> optional = Arrays
        .stream(TranslateFor.values())
        .filter(it -> it.name().equalsIgnoreCase(value))
        .findFirst();
    return optional.orElse(null);
  }
}
