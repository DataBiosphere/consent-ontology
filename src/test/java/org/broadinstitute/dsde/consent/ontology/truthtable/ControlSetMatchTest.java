package org.broadinstitute.dsde.consent.ontology.truthtable;

import io.dropwizard.testing.junit.ResourceTestRule;
import org.broadinstitute.dsde.consent.ontology.datause.models.*;
import org.broadinstitute.dsde.consent.ontology.resources.MatchPair;
import org.broadinstitute.dsde.consent.ontology.resources.MatchResource;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.InjectMocks;

public class ControlSetMatchTest extends TruthTableTests {

    @InjectMocks
    private static MatchResource matchResource = new MatchResource();

    /**
     * Use GrizzlyTestContainerFactory to process async requests.
     */
    @ClassRule
    public static final ResourceTestRule RULE = ResourceTestRule.builder()
        .setTestContainerFactory(new GrizzlyWebTestContainerFactory())
        .addResource(matchResource).build();

    @BeforeClass
    public static void setUp() throws Exception {
        matchResource.setOntologyList(getOntologyListMock());
    }
    
    private UseRestriction darCSA = new And(
        new Named("http://www.broadinstitute.org/ontologies/DURPO/control"),
        new Named("http://purl.obolibrary.org/obo/DOID_162")
    );

    private UseRestriction darDefaultCSA = new And(
            new And(
                new Not(new Named("http://www.broadinstitute.org/ontologies/DURPO/methods_research")),
                new Not(new Named("http://www.broadinstitute.org/ontologies/DURPO/population")),
                new Named("http://www.broadinstitute.org/ontologies/DURPO/control")
            ),
            new Named("http://purl.obolibrary.org/obo/DOID_162"),
            new Named("http://www.broadinstitute.org/ontologies/DURPO/Non_profit")

    );

    private UseRestriction darCSB = new Named("http://purl.obolibrary.org/obo/DOID_162");

    private UseRestriction darDefaultCSB = new And(
            new And(
                new Not(new Named("http://www.broadinstitute.org/ontologies/DURPO/methods_research")),
                new Not(new Named("http://www.broadinstitute.org/ontologies/DURPO/population")),
                new Not(new Named("http://www.broadinstitute.org/ontologies/DURPO/control"))
            ),
            new Named("http://purl.obolibrary.org/obo/DOID_162"),
            new Named("http://www.broadinstitute.org/ontologies/DURPO/Non_profit")
    );

    private UseRestriction darCSC = new Named("http://www.broadinstitute.org/ontologies/DURPO/control");

    private UseRestriction darDefaultCSC = new And(
            new And(
                new Not(new Named("http://www.broadinstitute.org/ontologies/DURPO/methods_research")),
                new Not(new Named("http://www.broadinstitute.org/ontologies/DURPO/population")),
                new Named("http://www.broadinstitute.org/ontologies/DURPO/control")
            ),
            new Named("http://www.broadinstitute.org/ontologies/DURPO/Non_profit")
    );

    // Combined example from OD-329
    private UseRestriction dulUC1 = new Or(
        new Named("http://www.broadinstitute.org/ontologies/DURPO/aggregate_analysis"),
        new Or(
            new Named("http://www.broadinstitute.org/ontologies/DURPO/methods_research"),
            new Named("http://purl.obolibrary.org/obo/DOID_162")
        )
    );

    // Combined example from OD-335
    private UseRestriction dulUC2 = new Or(
        new Or(
            new Named("http://www.broadinstitute.org/ontologies/DURPO/aggregate_analysis"),
            new Or(
                new Named("http://www.broadinstitute.org/ontologies/DURPO/methods_research"),
                new Named("http://purl.obolibrary.org/obo/DOID_162")
            )
        ),
        new And(
            new Or(
                new Named("http://www.broadinstitute.org/ontologies/DURPO/aggregate_analysis"),
                new Or(
                    new Named("http://www.broadinstitute.org/ontologies/DURPO/methods_research"),
                    new Named("http://purl.obolibrary.org/obo/DOID_162")
                )
            ),
            new Named("http://www.broadinstitute.org/ontologies/DURPO/control")
        )
    );

    // Combined example from OD-336
    private UseRestriction dulUC3 = new Or(
        new Named("http://www.broadinstitute.org/ontologies/DURPO/aggregate_analysis"),
        new Or(
            new Named("http://www.broadinstitute.org/ontologies/DURPO/methods_research"),
            new Named("http://purl.obolibrary.org/obo/DOID_162")
        )
    );


    @Test
    public void testControlSetA_UC1() {

        // Testing the case where:
        // DAR is yes control set, yes cancer
        // DUL is yes cancer
        // Response should be positive

        MatchPair pair = new MatchPair(darCSA, dulUC1);
        assertResponse(getResponseFuture(RULE, pair), true);
    }

    @Test
    public void testDefaultControlSetA_UC1() {

        // Testing the case where:
        // DAR is yes control set, yes cancer and not profit. Not methods and population
        // DUL is yes cancer
        // Response should be positive

        MatchPair pair = new MatchPair(darDefaultCSA, dulUC1);
        assertResponse(getResponseFuture(RULE, pair), true);
    }

    @Test
    public void testControlSetA_UC2() {

        // Testing the case where:
        // DAR is yes cancer, yes control set
        // DUL is yes cancer, control set other than cancer prohibited
        // Response should be positive

        MatchPair pair = new MatchPair(darCSA, dulUC2);
        assertResponse(getResponseFuture(RULE, pair), true);
    }

    @Test
    public void testDefaultControlSetA_UC2() {

        // Testing the case where:
        // DAR is yes cancer, yes control set  and not profit. Not methods and population
        // DUL is yes cancer, control set other than cancer prohibited
        // Response should be positive

        MatchPair pair = new MatchPair(darDefaultCSA, dulUC2);
        assertResponse(getResponseFuture(RULE, pair), true);
    }

    @Test
    public void testControlSetA_UC3() {

        // Testing the case where:
        // DAR is yes cancer, yes control set
        // DUL is yes cancer, control set other than cancer allowed
        // Response should be positive

        MatchPair pair = new MatchPair(darCSA, dulUC3);
        assertResponse(getResponseFuture(RULE, pair), true);
    }

    @Test
    public void testDefaultControlSetA_UC3() {

        // Testing the case where:
        // DAR is yes cancer, yes control set  and not profit. Not methods and population
        // DUL is yes cancer, control set other than cancer allowed
        // Response should be positive

        MatchPair pair = new MatchPair(darDefaultCSA, dulUC3);
        assertResponse(getResponseFuture(RULE, pair), true);
    }

    @Test
    public void testControlSetB_UC1() {

        // Testing the case where:
        // DAR is yes cancer
        // DUL is yes cancer
        // Response should be positive

        MatchPair pair = new MatchPair(darCSB, dulUC1);
        assertResponse(getResponseFuture(RULE, pair), true);
    }


    @Test
    public void testDefaultControlSetB_UC1() {

        // Testing the case where:
        // DAR is yes cancer
        // DAR is yes cancer and not profit. Not methods, population and controls
        // DUL is yes cancer
        // Response should be positive

        MatchPair pair = new MatchPair(darDefaultCSB, dulUC1);
        assertResponse(getResponseFuture(RULE, pair), true);
    }

    @Test
    public void testControlSetB_UC2() {

        // Testing the case where:
        // DAR is yes cancer
        // DUL is yes cancer, control set other than cancer prohibited
        // Response should be positive

        MatchPair pair = new MatchPair(darCSB, dulUC2);
        assertResponse(getResponseFuture(RULE, pair), true);
    }

    @Test
    public void testDefaultControlSetB_UC2() {

        // Testing the case where:
        // DAR is yes cancer and not profit. Not methods, population and controls
        // DUL is yes cancer, control set other than cancer prohibited
        // Response should be positive

        MatchPair pair = new MatchPair(darDefaultCSB, dulUC2);
        assertResponse(getResponseFuture(RULE, pair), true);
    }

    @Test
    public void testControlSetB_UC3() {

        // Testing the case where:
        // DAR is yes cancer
        // DUL is yes cancer, control set other than cancer allowed
        // Response should be positive

        MatchPair pair = new MatchPair(darCSB, dulUC3);
        assertResponse(getResponseFuture(RULE, pair), true);
    }

    @Test
    public void testDefaultControlSetB_UC3() {

        // Testing the case where:
        // DAR is yes cancer and not profit. Not methods, population and controls
        // DUL is yes cancer, control set other than cancer allowed
        // Response should be positive

        MatchPair pair = new MatchPair(darDefaultCSB, dulUC3);
        assertResponse(getResponseFuture(RULE, pair), true);
    }


    @Test
    public void testControlSetC_UC1() {

        // Testing the case where:
        // DAR is control only
        // DUL is yes cancer
        // Response should be negative

        MatchPair pair = new MatchPair(darCSC, dulUC1);
        assertResponse(getResponseFuture(RULE, pair), false);
    }

    @Test
    public void testDefaultControlSetC_UC1() {

        // Testing the case where:
        // DAR is control and  not profit. Not methods and population
        // DUL is yes cancer
        // Response should be negative

        MatchPair pair = new MatchPair(darDefaultCSC, dulUC1);
        assertResponse(getResponseFuture(RULE, pair), false);
    }

    @Test
    public void testControlSetC_UC2() {

        // Testing the case where:
        // DAR is control only
        // DUL is yes cancer,  control set other than cancer prohibited
        // Response should be negative

        MatchPair pair = new MatchPair(darCSC, dulUC2);
        assertResponse(getResponseFuture(RULE, pair), false);
    }

    @Test
    public void testDefaultControlSetC_UC2() {

        // Testing the case where:
        // DAR is control and  not profit. Not methods and population
        // DUL is yes cancer,  control set other than cancer prohibited
        // Response should be negative

        MatchPair pair = new MatchPair(darDefaultCSC, dulUC2);
        assertResponse(getResponseFuture(RULE, pair), false);
    }

    @Test
    public void testControlSetC_UC3() {

        // Testing the case where:
        // DAR is controls only
        // DUL is yes cancer, control set other than cancer allowed
        // Response should be positive

        MatchPair pair = new MatchPair(darCSB, dulUC3);
        assertResponse(getResponseFuture(RULE, pair), true);
    }

    @Test
    public void testDefaultControlSetC_UC3() {

        // Testing the case where:
        // DAR is control and  not profit. Not methods and population
        // DUL is yes cancer, control set other than cancer allowed
        // Response should be positive

        MatchPair pair = new MatchPair(darDefaultCSB, dulUC3);
        assertResponse(getResponseFuture(RULE, pair), true);
    }

}
