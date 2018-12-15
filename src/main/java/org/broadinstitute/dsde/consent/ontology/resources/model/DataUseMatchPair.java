package org.broadinstitute.dsde.consent.ontology.resources.model;

import com.google.gson.Gson;

@SuppressWarnings("unused")
public class DataUseMatchPair {

    private DataUse purpose;

    private DataUse dataset;

    public DataUseMatchPair() { }

    public DataUseMatchPair(DataUse purpose, DataUse dataset) {
        this.purpose = purpose;
        this.dataset = dataset;
    }

    public DataUse getPurpose() {
        return purpose;
    }

    public void setPurpose(DataUse purpose) {
        this.purpose = purpose;
    }

    public DataUse getDataset() {
        return dataset;
    }

    public void setDataset(DataUse dataset) {
        this.dataset = dataset;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
