package org.broadinstitute.dsde.consent.ontology.truthtable;

import org.broadinstitute.dsde.consent.ontology.datause.models.*;
import org.broadinstitute.dsde.consent.ontology.resources.MatchPair;
import org.junit.Test;

public class AggregateAnalysisMatchTest extends TruthTableTests {

    private UseRestriction darAAA = new And(
        new Named("http://www.broadinstitute.org/ontologies/DURPO/aggregate_analysis"),
        new Named("http://purl.obolibrary.org/obo/DOID_162")
    );

    private UseRestriction darAAB = new Named("http://purl.obolibrary.org/obo/DOID_162");

    private UseRestriction darAAC = new Named("http://www.broadinstitute.org/ontologies/DURPO/aggregate_analysis");

    // Combined example from OD-329
    private UseRestriction dulUC1 = new Or(
        new Named("http://www.broadinstitute.org/ontologies/DURPO/aggregate_analysis"),
        new Or(
            new Named("http://www.broadinstitute.org/ontologies/DURPO/methods_research"),
            new Named("http://purl.obolibrary.org/obo/DOID_162")
        )
    );

    // Combined example from OD-333
    private UseRestriction dulUC2 = new Or(
        new Named("http://www.broadinstitute.org/ontologies/DURPO/aggregate_analysis"),
        new Or(
            new Named("http://www.broadinstitute.org/ontologies/DURPO/methods_research"),
            new Named("http://purl.obolibrary.org/obo/DOID_162")
        )
    );

    // Combined example from OD-333
    private UseRestriction dulUC3 = new Or(
        new Not(new Named("http://www.broadinstitute.org/ontologies/DURPO/aggregate_analysis")),
        new Or(
            new Named("http://www.broadinstitute.org/ontologies/DURPO/methods_research"),
            new Named("http://purl.obolibrary.org/obo/DOID_162")
        )
    );

    @Test
    public void testAggregateAnalysisA_UC1() {

        // Testing the case where:
        // DAR is yes aggregate, yes cancer
        // DUL is yes cancer
        // Response should be positive

        MatchPair pair = new MatchPair(darAAA, dulUC1);
        assertResponse(getResponseFuture(pair), true);
    }

    @Test
    public void testAggregateAnalysisA_UC2() {

        // Testing the case where:
        // DAR is yes cancer, yes aggregate
        // DUL is yes cancer, yes aggregate
        // Response should be positive

        MatchPair pair = new MatchPair(darAAA, dulUC2);
        assertResponse(getResponseFuture(pair), true);
    }

    @Test
    public void testAggregateAnalysisA_UC3() {

        // Testing the case where:
        // DAR is yes cancer, yes aggregate analysis
        // DUL is yes cancer, no aggregate analysis (i.e., outside of cancer)
        // Response should be positive

        MatchPair pair = new MatchPair(darAAA, dulUC3);
        assertResponse(getResponseFuture(pair), true);
    }

    @Test
    public void testAggregateAnalysisB_UC1() {

        // Testing the case where:
        // DAR is yes cancer
        // DUL is yes cancer
        // Response should be positive

        MatchPair pair = new MatchPair(darAAB, dulUC1);
        assertResponse(getResponseFuture(pair), true);
    }

    @Test
    public void testAggregateAnalysisB_UC2() {

        // Testing the case where:
        // DAR is yes cancer
        // DUL is yes cancer, yes aggregate
        // Response should be positive

        MatchPair pair = new MatchPair(darAAB, dulUC2);
        assertResponse(getResponseFuture(pair), true);
    }

    @Test
    public void testAggregateAnalysisB_UC3() {

        // Testing the case where:
        // DAR is yes cancer
        // DUL is yes cancer, no aggregate analysis (i.e., outside of cancer)
        // Response should be positive

        MatchPair pair = new MatchPair(darAAB, dulUC3);
        assertResponse(getResponseFuture(pair), true);
    }

    @Test
    public void testAggregateAnalysisC_UC1() {

        // Testing the case where:
        // DAR is aggregate research
        // DUL is yes cancer
        // Response should be positive

        MatchPair pair = new MatchPair(darAAC, dulUC1);
        assertResponse(getResponseFuture(pair), true);
    }

    @Test
    public void testAggregateAnalysisC_UC2() {

        // Testing the case where:
        // DAR is aggregate research
        // DUL is yes cancer, yes aggregate
        // Response should be positive

        MatchPair pair = new MatchPair(darAAC, dulUC2);
        assertResponse(getResponseFuture(pair), true);
    }

    @Test
    public void testAggregateAnalysisC_UC3() {

        // Testing the case where:
        // DAR is yes aggregate only
        // DUL is yes cancer, no aggregate analysis (i.e., outside of cancer)
        // Response should be positive

        MatchPair pair = new MatchPair(darAAC, dulUC3);
        assertResponse(getResponseFuture(pair), false);
    }

}
