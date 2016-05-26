package org.broadinstitute.dsde.consent.ontology.service.validate;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.log4j.Logger;
import org.broadinstitute.dsde.consent.ontology.datause.api.OntologyTermSearchAPI;
import org.broadinstitute.dsde.consent.ontology.datause.models.OntologyTerm;
import org.broadinstitute.dsde.consent.ontology.datause.models.UseRestriction;
import org.broadinstitute.dsde.consent.ontology.datause.models.visitor.NamedVisitor;

import java.io.IOException;
import java.util.Collection;

@Singleton
public class UseRestrictionValidateImpl implements UseRestrictionValidateAPI{

    private final Logger log = Logger.getLogger(UseRestrictionValidateImpl.class);
    private OntologyTermSearchAPI ontologyTermSearchAPI;

    @Inject
    public void setOntologySearchTermAPI(OntologyTermSearchAPI ontologyTermSearchAPI) {
        this.ontologyTermSearchAPI = ontologyTermSearchAPI;
    }

    @Override
    public ValidateResponse validateUseRestriction(String useRestriction) throws Exception {
        log.debug("Received use restriction: " + useRestriction);
        ValidateResponse isValid = new ValidateResponse(true, useRestriction);
        try {
            UseRestriction restriction = UseRestriction.parse(useRestriction);
            String parsedRest = replaceChars(restriction.toString());
            if(!parsedRest.equals(replaceChars(useRestriction))){
                throw new Exception("There is more than one use restriction to validate. Please, send one each time.");
            }
            NamedVisitor validationVisitor = new NamedVisitor();
            restriction.visitAndContinue(validationVisitor);
            Collection<String> namedClasses = validationVisitor.getNamedClasses();
            for(String term: namedClasses){
                OntologyTerm oTerm = ontologyTermSearchAPI.findById(term);
                if(oTerm == null){
                    isValid.setValid(false);
                    isValid.addError("Term not found: "+ term);
                }
            }
        } catch (IOException e) {
            isValid.setValid(false);
            isValid.addError(e.getMessage());
        }
        return isValid;
    }

    private String replaceChars(String useRestriction){
        useRestriction = useRestriction.replaceAll("\\n", "");
        useRestriction = useRestriction.replaceAll("\\r", "");
        useRestriction = useRestriction.replaceAll("\\s","");
        if((useRestriction.charAt(useRestriction.length()-1)==',') || (useRestriction.charAt(useRestriction.length()-1)==';')){
            useRestriction = useRestriction.substring(0, useRestriction.length()-1);
        }
        return useRestriction;
    }

}
