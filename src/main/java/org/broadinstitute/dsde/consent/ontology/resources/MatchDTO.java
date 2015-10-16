package org.broadinstitute.dsde.consent.ontology.resources;

import com.google.gson.Gson;
import org.broadinstitute.dsde.consent.ontology.datause.models.UseRestriction;
import org.broadinstitute.dsde.consent.ontology.datause.ontologies.OntologyModel;

public class MatchDTO {

    private MatchPair matchPair;
    private OntologyModel ontologyModel;

    public MatchDTO(MatchPair matchPair) {
        this.matchPair = matchPair;
    }

    public MatchDTO(MatchPair matchPair, OntologyModel ontologyModel) {
        this.matchPair = matchPair;
        this.ontologyModel = ontologyModel;
    }

    public void setMathPair(MatchPair mathPair) {
        this.matchPair = mathPair;
    }

    public MatchPair getMatchPair() {
        return matchPair;
    }

    public OntologyModel getOntologyModel() {
        return ontologyModel;
    }

    public void setOntologyModel(OntologyModel ontologyModel) {
        this.ontologyModel = ontologyModel;
    }

    public UseRestriction getPurpose() {
        return matchPair.purpose;
    }

    public void setPurpose(UseRestriction purpose) {
        matchPair.purpose = purpose;
    }

    public UseRestriction getConsent() {
        return matchPair.consent;
    }

    public void setConsent(UseRestriction consent) {
        matchPair.consent = consent;
    }

    @Override
    public String toString() {
        return new Gson().toJson(matchPair);
    }
}
