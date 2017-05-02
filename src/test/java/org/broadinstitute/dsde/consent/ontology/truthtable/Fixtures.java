package org.broadinstitute.dsde.consent.ontology.truthtable;

import org.broadinstitute.dsde.consent.ontology.resources.model.DataUse;
import org.broadinstitute.dsde.consent.ontology.resources.model.DataUseBuilder;

import java.util.Arrays;
import java.util.List;

/**
 * Created by davidan on 5/2/17.
 */
class Fixtures {
    
    static final String cancerNode = "http://purl.obolibrary.org/obo/DOID_162";
    static final List<String> cancerNodeList = Arrays.asList(cancerNode);
    
    static class HMB {
       static final DataUse uc1 = new DataUseBuilder().setHmbResearch(true).build();
       static final DataUse uc1_1 = new DataUseBuilder().setDiseaseRestrictions(cancerNodeList).build();
       static final DataUse uc2 = new DataUseBuilder().setHmbResearch(true).setMethodsResearch(false).build();
       static final DataUse uc2_1 = new DataUseBuilder().setDiseaseRestrictions(cancerNodeList).setMethodsResearch(false).build();
       static final DataUse uc3 = new DataUseBuilder().setGeneralUse(true).setMethodsResearch(false).build();
       static final DataUse uc4 = new DataUseBuilder().setHmbResearch(true).setMethodsResearch(true).build();
       static final DataUse uc4_1 = new DataUseBuilder().setDiseaseRestrictions(cancerNodeList).setMethodsResearch(true).build();
       
       static final DataUse mrpa = new DataUseBuilder().setDiseaseRestrictions(cancerNodeList).setMethodsResearch(true).build();
       static final DataUse mrpb = new DataUseBuilder().setDiseaseRestrictions(cancerNodeList).build();
       static final DataUse mrpc = new DataUseBuilder().setMethodsResearch(true).build();
    }
    
    
}
