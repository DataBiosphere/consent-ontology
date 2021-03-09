package org.broadinstitute.dsde.consent.ontology.model;

import com.google.gson.Gson;

@SuppressWarnings("unused")
public class DataUseMatchPair {

    private DataUse purpose;

    private DataUse consent;

    public DataUseMatchPair() { }

    public DataUseMatchPair(DataUse purpose, DataUse consent) {
        this.purpose = purpose;
        this.consent = consent;
    }

    public DataUse getPurpose() {
        return purpose;
    }

    public void setPurpose(DataUse purpose) {
        this.purpose = purpose;
    }

    public DataUse getConsent() {
        return consent;
    }

    public void setConsent(DataUse consent) {
        this.consent = consent;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
