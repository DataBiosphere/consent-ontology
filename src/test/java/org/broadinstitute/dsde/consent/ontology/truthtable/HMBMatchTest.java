package org.broadinstitute.dsde.consent.ontology.truthtable;

import org.broadinstitute.dsde.consent.ontology.datause.builder.ConsentRestrictionBuilder;
import org.broadinstitute.dsde.consent.ontology.datause.builder.DARRestrictionBuilder;
import org.broadinstitute.dsde.consent.ontology.datause.models.UseRestriction;
import org.broadinstitute.dsde.consent.ontology.resources.MatchPair;
import org.broadinstitute.dsde.consent.ontology.resources.model.DataUse;
import org.broadinstitute.dsde.consent.ontology.resources.model.DataUseBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class HMBMatchTest extends TruthTableTests {
    
    private DataUse purpose;
    private DataUse consent;
    private Boolean expectedMatchResult;
    private String testName;
    
    public HMBMatchTest(DataUse purpose, DataUse consent, Boolean expectedMatchResult, String testName) {
        this.purpose = purpose;
        this.consent = consent;
        this.expectedMatchResult = expectedMatchResult;
        this.testName = testName;
    }
    
    
    @Parameterized.Parameters(name="HMBMatchTest {index}: {3}")
    public static Collection<Object[]> tests() {
        
        String cancerNode = "http://purl.obolibrary.org/obo/DOID_162";
        
        // define all your test cases here! order is purpose, consent, expected result, test name.
        return Arrays.asList(new Object[][]{
            {
                new DataUseBuilder().build(),
                new DataUseBuilder().build(),
                true,
                "both empty"
            },
            { Fixtures.HMB.mrpa, Fixtures.HMB.uc1, true, "MRPA vs. UC1" },
            { Fixtures.HMB.mrpa, Fixtures.HMB.uc1_1, true, "MRPA vs. UC1_1" },
            { Fixtures.HMB.mrpa, Fixtures.HMB.uc2, true, "MRPA vs. UC2" },
            { Fixtures.HMB.mrpa, Fixtures.HMB.uc2_1, true, "MRPA vs. UC2_1" },
            { Fixtures.HMB.mrpa, Fixtures.HMB.uc3, true, "MRPA vs. UC3" },
            { Fixtures.HMB.mrpa, Fixtures.HMB.uc4, true, "MRPA vs. UC4" },
            { Fixtures.HMB.mrpa, Fixtures.HMB.uc4_1, true, "MRPA vs. UC4_1" },
            
            { Fixtures.HMB.mrpb, Fixtures.HMB.uc1, true, "MRPB vs. UC1" },
            { Fixtures.HMB.mrpb, Fixtures.HMB.uc1_1, true, "MRPB vs. UC1_1" },
            { Fixtures.HMB.mrpb, Fixtures.HMB.uc2, true, "MRPB vs. UC2" },
            { Fixtures.HMB.mrpb, Fixtures.HMB.uc2_1, true, "MRPB vs. UC2_1" },
            { Fixtures.HMB.mrpb, Fixtures.HMB.uc3, true, "MRPB vs. UC3" },
            { Fixtures.HMB.mrpb, Fixtures.HMB.uc4, true, "MRPB vs. UC4" },
            { Fixtures.HMB.mrpb, Fixtures.HMB.uc4_1, true, "MRPB vs. UC4_1" },
                
            { Fixtures.HMB.mrpc, Fixtures.HMB.uc1, true, "MRPC vs. UC1" },
            { Fixtures.HMB.mrpc, Fixtures.HMB.uc1_1, true, "MRPC vs. UC1_1" },
            // { Fixtures.HMB.mrpc, Fixtures.HMB.uc2, false, "MRPC vs. UC2" }, // TODO: fix test. Doc says match should fail.
            { Fixtures.HMB.mrpc, Fixtures.HMB.uc2_1, false, "MRPC vs. UC2_1" },
            // { Fixtures.HMB.mrpc, Fixtures.HMB.uc3, false, "MRPC vs. UC3" }, // TODO: fix test. Doc says match should fail.
            { Fixtures.HMB.mrpc, Fixtures.HMB.uc4, true, "MRPC vs. UC4" },
            { Fixtures.HMB.mrpc, Fixtures.HMB.uc4_1, true, "MRPC vs. UC4_1" }
        });
    }

    @Test
    public void parameterizedTest() {
        assertResponse(toMatchPair(purpose, consent), expectedMatchResult);
    }

        
    private MatchPair toMatchPair(DataUse purpose, DataUse consent) {
        UseRestriction structuredPurpose = new DARRestrictionBuilder().buildUseRestriction(purpose);
        UseRestriction structuredConsent = new ConsentRestrictionBuilder().buildUseRestriction(consent);
        return new MatchPair(structuredPurpose, structuredConsent);
    }
    
}
