package org.broadinstitute.dsde.consent.ontology.truthtable;

import org.broadinstitute.dsde.consent.ontology.datause.models.*;
import org.broadinstitute.dsde.consent.ontology.resources.MatchPair;
import org.junit.Test;

public class MethodsResearchMatchTest extends TruthTableTests {

    private UseRestriction darMRPA = new And(
        new Named("http://www.broadinstitute.org/ontologies/DURPO/methods_research"),
        new Named("http://purl.obolibrary.org/obo/DOID_162")
    );

    private UseRestriction darMRPB = new Named("http://purl.obolibrary.org/obo/DOID_162");

    private UseRestriction darMRPC = new Named("http://www.broadinstitute.org/ontologies/DURPO/methods_research");


    private UseRestriction dulUC1 = new Or(
        new Named("http://purl.obolibrary.org/obo/DOID_162"),
        new Named("http://www.broadinstitute.org/ontologies/DURPO/methods_research")
    );

    private UseRestriction dulUC2 = new Or(
        new And(
            new Named("http://purl.obolibrary.org/obo/DOID_162"),
            new Not(new Named("http://www.broadinstitute.org/ontologies/DURPO/methods_research"))
        ),
        new Named("http://purl.obolibrary.org/obo/DOID_162")
    );

    private UseRestriction dulUC3 = new Named("http://purl.obolibrary.org/obo/DOID_162");

    private UseRestriction dulUC4 = new Or(
        new Named("http://purl.obolibrary.org/obo/DOID_162"),
        new Named("http://www.broadinstitute.org/ontologies/DURPO/methods_research")
    );


    @Test
    public void testMRPA_UC1() {

        // Testing the case where:
        // DAR is yes methods, yes cancer
        // DUL is yes cancer
        // Response should be positive

        MatchPair pair = new MatchPair(darMRPA, dulUC1);
        assertResponse(getResponseFuture(pair), true);
    }

    @Test
    public void testMRPA_UC2() {

        // Testing the case where:
        // DAR is yes methods, yes cancer
        // DUL is yes cancer, no methods
        // Response should be positive

        MatchPair pair = new MatchPair(darMRPA, dulUC2);
        assertResponse(getResponseFuture(pair), true);
    }

    @Test
    public void testMRPA_UC3() {

        // Testing the case where:
        // DAR is yes methods, yes cancer
        // DUL is yes for everything, but no methods research allowed
        // Response should be positive

        MatchPair pair = new MatchPair(darMRPA, dulUC3);
        assertResponse(getResponseFuture(pair), true);
    }

    @Test
    public void testMRPA_UC4() {

        // Testing the case where:
        // DAR is yes methods, yes cancer
        // DUL is yes for everything, but no methods research allowed
        // Response should be positive

        MatchPair pair = new MatchPair(darMRPA, dulUC4);
        assertResponse(getResponseFuture(pair), true);
    }

    @Test
    public void testMRPB_UC1() {

        // Testing the case where:
        // DAR is yes cancer
        // DUL is yes cancer
        // Response should be positive

        MatchPair pair = new MatchPair(darMRPB, dulUC1);
        assertResponse(getResponseFuture(pair), true);
    }

    @Test
    public void testMRPB_UC2() {

        // Testing the case where:
        // DAR is yes cancer
        // DUL is yes cancer, no methods
        // Response should be positive

        MatchPair pair = new MatchPair(darMRPB, dulUC2);
        assertResponse(getResponseFuture(pair), true);
    }

    @Test
    public void testMRPB_UC3() {

        // Testing the case where:
        // DAR is yes cancer
        // DUL is yes for everything, but no methods research allowed
        // Response should be positive

        MatchPair pair = new MatchPair(darMRPB, dulUC3);
        assertResponse(getResponseFuture(pair), true);
    }

    @Test
    public void testMRPB_UC4() {

        // Testing the case where:
        // DAR is yes cancer
        // DUL is yes for everything, but no methods research allowed
        // Response should be positive

        MatchPair pair = new MatchPair(darMRPB, dulUC4);
        assertResponse(getResponseFuture(pair), true);
    }


    @Test
    public void testMRPC_UC1() {

        // Testing the case where:
        // DAR is methods research
        // DUL is yes cancer
        // Response should be positive

        MatchPair pair = new MatchPair(darMRPC, dulUC1);
        assertResponse(getResponseFuture(pair), true);
    }

    @Test
    public void testMRPC_UC2() {

        // Testing the case where:
        // DAR is methods research
        // DUL is yes cancer, no methods
        // Response should be negative

        MatchPair pair = new MatchPair(darMRPC, dulUC2);
        assertResponse(getResponseFuture(pair), false);
    }

    @Test
    public void testMRPC_UC3() {

        // Testing the case where:
        // DAR is methods research
        // DUL is yes for everything, but no methods research allowed
        // Response should be negative

        MatchPair pair = new MatchPair(darMRPC, dulUC3);
        assertResponse(getResponseFuture(pair), false);
    }

    @Test
    public void testMRPC_UC4() {

        // Testing the case where:
        // DAR is methods research
        // DUL is yes for everything, but no methods research allowed
        // Response should be positive

        MatchPair pair = new MatchPair(darMRPC, dulUC4);
        assertResponse(getResponseFuture(pair), true);
    }

}
