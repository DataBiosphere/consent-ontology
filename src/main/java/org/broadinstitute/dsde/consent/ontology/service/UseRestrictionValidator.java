package org.broadinstitute.dsde.consent.ontology.service;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.google.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.broadinstitute.dsde.consent.ontology.OntologyLogger;
import org.broadinstitute.dsde.consent.ontology.datause.api.OntologyTermSearchAPI;
import org.broadinstitute.dsde.consent.ontology.datause.models.OntologyTerm;
import org.broadinstitute.dsde.consent.ontology.datause.models.UseRestriction;
import org.broadinstitute.dsde.consent.ontology.datause.models.visitor.NamedVisitor;
import org.broadinstitute.dsde.consent.ontology.enumerations.UseRestrictionKeys;
import org.broadinstitute.dsde.consent.ontology.model.ValidationResponse;

public class UseRestrictionValidator implements UseRestrictionValidationService, OntologyLogger {

    private OntologyTermSearchAPI ontologyTermSearchAPI;

    @Inject
    public UseRestrictionValidator(OntologyTermSearchAPI ontologyTermSearchAPI) {
        this.ontologyTermSearchAPI = ontologyTermSearchAPI;
    }

    @Override
    public ValidationResponse validateUseRestriction(String useRestriction) throws Exception {
        logDebug("Received use restriction: " + useRestriction);
        ValidationResponse isValid = new ValidationResponse(true, useRestriction);
        try {
            UseRestriction restriction = UseRestriction.parse(useRestriction);
            String parsedRest = replaceChars(restriction.toString());
            if (!parsedRest.equals(replaceChars(useRestriction))) {
                throw new IllegalArgumentException("There is more than one use restriction to validate. Please, send one each time.");
            }
            NamedVisitor validationVisitor = new NamedVisitor();
            restriction.visitAndContinue(validationVisitor);
            Collection<String> namedClasses = validationVisitor.getNamedClasses();
            Set<String> invalidTerms = new HashSet<>();
            for (String term : namedClasses) {
                OntologyTerm oTerm = ontologyTermSearchAPI.findById(term);
                if (oTerm == null) {
                    invalidTerms.add(term);
                }
            }
            if(invalidTerms.size() > 0){
                isValid.setValid(false);
                isValid.addError("Term not found: " + String.join(", ", invalidTerms));
            }
        }
        catch(UnrecognizedPropertyException e){
            String validValues = Stream.of(UseRestrictionKeys.values()).map(Enum::name).collect(Collectors.joining(", "));
            isValid.setValid(false);
            isValid.addError("Could not resolve the following keys: " + e.getPropertyName() + " into a subtype of " + validValues.toLowerCase());
        }
        catch (Exception e) {
            isValid.setValid(false);
            isValid.addError(e.getMessage());
        }
        return isValid;
    }

    private String replaceChars(String useRestriction) {
        useRestriction = useRestriction.replaceAll("\\n", "");
        useRestriction = useRestriction.replaceAll("\\r", "");
        useRestriction = useRestriction.replaceAll("\\s", "");
        if ((useRestriction.charAt(useRestriction.length() - 1) == ',') || (useRestriction.charAt(useRestriction.length() - 1) == ';')) {
            useRestriction = useRestriction.substring(0, useRestriction.length() - 1);
        }
        return useRestriction;
    }

}
