package org.broadinstitute.dsde.consent.ontology.service.validate;

public interface UseRestrictionValidationService {

    ValidateResponse validateUseRestriction(String useRestriction) throws Exception;

}
