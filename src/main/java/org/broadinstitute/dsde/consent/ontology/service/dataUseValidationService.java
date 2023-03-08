package org.broadinstitute.dsde.consent.ontology.service;

import org.broadinstitute.dsde.consent.ontology.model.ValidationResponse;

public interface dataUseValidationService {

  ValidationResponse validateDataUse(String useRestriction) throws Exception;

}