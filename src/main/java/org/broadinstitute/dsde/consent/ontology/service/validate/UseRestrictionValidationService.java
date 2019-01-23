package org.broadinstitute.dsde.consent.ontology.service.validate;

import com.google.inject.ImplementedBy;

@ImplementedBy(UseRestrictionValidator.class)
public interface UseRestrictionValidationService {

    ValidateResponse validateUseRestriction(String useRestriction) throws Exception;

}
