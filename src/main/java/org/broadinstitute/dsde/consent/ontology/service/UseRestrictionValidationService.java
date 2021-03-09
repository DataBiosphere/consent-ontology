package org.broadinstitute.dsde.consent.ontology.service;

import org.broadinstitute.dsde.consent.ontology.model.ValidationResponse;

public interface UseRestrictionValidationService {

    ValidationResponse validateUseRestriction(String useRestriction) throws Exception;

}
