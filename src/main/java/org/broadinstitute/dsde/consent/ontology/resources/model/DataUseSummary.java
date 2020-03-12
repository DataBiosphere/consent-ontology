package org.broadinstitute.dsde.consent.ontology.resources.model;

import java.util.List;

public class DataUseSummary {

    List<DataUseElement> primary;
    List<DataUseElement> secondary;

    public DataUseSummary() {
    }

    public DataUseSummary(List<DataUseElement> primary, List<DataUseElement> secondary) {
        this.primary = primary;
        this.secondary = secondary;
    }

    public List<DataUseElement> getPrimary() {
        return primary;
    }

    public void setPrimary(List<DataUseElement> primary) {
        this.primary = primary;
    }

    public List<DataUseElement> getSecondary() {
        return secondary;
    }

    public void setSecondary(List<DataUseElement> secondary) {
        this.secondary = secondary;
    }

}
