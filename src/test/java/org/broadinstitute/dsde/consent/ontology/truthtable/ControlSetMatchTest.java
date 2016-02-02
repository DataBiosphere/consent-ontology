package org.broadinstitute.dsde.consent.ontology.truthtable;

import org.broadinstitute.dsde.consent.ontology.datause.models.And;
import org.broadinstitute.dsde.consent.ontology.datause.models.Named;
import org.broadinstitute.dsde.consent.ontology.datause.models.Or;
import org.broadinstitute.dsde.consent.ontology.datause.models.UseRestriction;
import org.broadinstitute.dsde.consent.ontology.resources.MatchPair;
import org.junit.Test;

public class ControlSetMatchTest extends TruthTableTests {

    private UseRestriction darCSA = new And(
        new Named("http://www.broadinstitute.org/ontologies/DURPO/control"),
        new Named("http://purl.obolibrary.org/obo/DOID_162")
    );

    private UseRestriction darCSB = new Named("http://purl.obolibrary.org/obo/DOID_162");

    private UseRestriction darCSC = new Named("http://www.broadinstitute.org/ontologies/DURPO/control");


    private UseRestriction dulUC1 = new Named("http://purl.obolibrary.org/obo/DOID_162");

    private UseRestriction dulUC2 = new Or(
        new Named("http://purl.obolibrary.org/obo/DOID_162"),
        new And(
            new Named("http://purl.obolibrary.org/obo/DOID_162"),
            new Named("http://www.broadinstitute.org/ontologies/DURPO/control")
    ));

    private UseRestriction dulUC3 = new Named("http://purl.obolibrary.org/obo/DOID_162");


    @Test
    public void testControlSetA_UC1() {

        // Testing the case where:
        // DAR is yes control set, yes cancer
        // DUL is yes cancer
        // Response should be positive

        MatchPair pair = new MatchPair(darCSA, dulUC1);
        assertResponse(getResponseFuture(pair), true);
    }

    @Test
    public void testControlSetA_UC2() {

        // Testing the case where:
        // DAR is yes cancer, yes control set
        // DUL is yes cancer, control set other than cancer prohibited
        // Response should be positive

        MatchPair pair = new MatchPair(darCSA, dulUC2);
        assertResponse(getResponseFuture(pair), true);
    }

    @Test
    public void testControlSetA_UC3() {

        // Testing the case where:
        // DAR is yes cancer, yes control set
        // DUL is yes cancer, control set other than cancer allowed
        // Response should be positive

        MatchPair pair = new MatchPair(darCSA, dulUC3);
        assertResponse(getResponseFuture(pair), true);
    }

    @Test
    public void testControlSetB_UC1() {

        // Testing the case where:
        // DAR is yes cancer
        // DUL is yes cancer
        // Response should be positive

        MatchPair pair = new MatchPair(darCSB, dulUC1);
        assertResponse(getResponseFuture(pair), true);
    }

    @Test
    public void testControlSetB_UC2() {

        // Testing the case where:
        // DAR is yes cancer
        // DUL is yes cancer, control set other than cancer prohibited
        // Response should be positive

        MatchPair pair = new MatchPair(darCSB, dulUC2);
        assertResponse(getResponseFuture(pair), true);
    }

    @Test
    public void testControlSetB_UC3() {

        // Testing the case where:
        // DAR is yes cancer
        // DUL is yes cancer, control set other than cancer allowed
        // Response should be positive

        MatchPair pair = new MatchPair(darCSB, dulUC3);
        assertResponse(getResponseFuture(pair), true);
    }


    @Test
    public void testControlSetC_UC1() {

        // Testing the case where:
        // DAR is control only
        // DUL is yes cancer
        // Response should be negative

        MatchPair pair = new MatchPair(darCSC, dulUC1);
        assertResponse(getResponseFuture(pair), false);
    }

    @Test
    public void testControlSetC_UC2() {

        // Testing the case where:
        // DAR is control only
        // DUL is yes cancer,  control set other than cancer prohibited
        // Response should be negative

        MatchPair pair = new MatchPair(darCSC, dulUC2);
        assertResponse(getResponseFuture(pair), false);
    }

    @Test
    public void testControlSetC_UC3() {

        // Testing the case where:
        // DAR is controls only
        // DUL is yes cancer, control set other than cancer allowed
        // Response should be positive

        MatchPair pair = new MatchPair(darCSB, dulUC3);
        assertResponse(getResponseFuture(pair), true);
    }

}
