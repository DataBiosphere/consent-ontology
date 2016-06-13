package org.broadinstitute.dsde.consent.ontology.truthtable;

import org.broadinstitute.dsde.consent.ontology.datause.models.*;
import org.broadinstitute.dsde.consent.ontology.resources.MatchPair;
import org.junit.Test;

public class AggregateAnalysisMatchTest extends TruthTableTests {

    private UseRestriction darAAA = new And(
        new Named("http://www.broadinstitute.org/ontologies/DUOS/aggregate_analysis"),
        new Named("http://purl.obolibrary.org/obo/DOID_162")
    );

    private UseRestriction darDefaultAAA = new And(
            new Not(new Named("http://www.broadinstitute.org/ontologies/DUOS/methods_research")),
            new Not(new Named("http://www.broadinstitute.org/ontologies/DUOS/population")),
            new Not(new Named("http://www.broadinstitute.org/ontologies/DUOS/control")),
            new Named("http://www.broadinstitute.org/ontologies/DUOS/aggregate_analysis"),
            new Named("http://purl.obolibrary.org/obo/DOID_162"),
            new Named("http://www.broadinstitute.org/ontologies/DUOS/Non_profit"));

    private UseRestriction darAAB = new Named("http://purl.obolibrary.org/obo/DOID_162");

    private UseRestriction darDefaultAAB = new And(
            new Not(new Named("http://www.broadinstitute.org/ontologies/DUOS/methods_research")),
            new Not(new Named("http://www.broadinstitute.org/ontologies/DUOS/population")),
            new Not(new Named("http://www.broadinstitute.org/ontologies/DUOS/control")),
            new Named("http://purl.obolibrary.org/obo/DOID_162"),
            new Named("http://www.broadinstitute.org/ontologies/DUOS/Non_profit"));

    private UseRestriction darAAC = new Named("http://www.broadinstitute.org/ontologies/DUOS/aggregate_analysis");

    private UseRestriction darDefaultAAC = new And(
            new Not(new Named("http://www.broadinstitute.org/ontologies/DUOS/methods_research")),
            new Not(new Named("http://www.broadinstitute.org/ontologies/DUOS/population")),
            new Not(new Named("http://www.broadinstitute.org/ontologies/DUOS/control")),
            new Named("http://www.broadinstitute.org/ontologies/DUOS/aggregate_analysis"),
            new Named("http://www.broadinstitute.org/ontologies/DUOS/Non_profit"));

    // Combined example from OD-329
    private UseRestriction dulUC1 = new Or(
        new Named("http://www.broadinstitute.org/ontologies/DUOS/aggregate_analysis"),
        new Or(
            new Named("http://www.broadinstitute.org/ontologies/DUOS/methods_research"),
            new Named("http://purl.obolibrary.org/obo/DOID_162")
        )
    );

    // Combined example from OD-333
    private UseRestriction dulUC2 = new Or(
        new Named("http://www.broadinstitute.org/ontologies/DUOS/aggregate_analysis"),
        new Or(
            new Named("http://www.broadinstitute.org/ontologies/DUOS/methods_research"),
            new Named("http://purl.obolibrary.org/obo/DOID_162")
        )
    );

    // Combined example from OD-333
    private UseRestriction dulUC3 = new Or(
        new Not(new Named("http://www.broadinstitute.org/ontologies/DUOS/aggregate_analysis")),
        new Or(
            new Named("http://www.broadinstitute.org/ontologies/DUOS/methods_research"),
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
        assertResponse(pair, true);
    }

    @Test
    public void testDefaultAggregateAnalysisA_UC1() {

        // Testing the case where:
        // DAR is yes aggregate, yes cancer and non profit. Not controls, population and methods
        // DUL is yes cancer
        // Response should be positive

        MatchPair pair = new MatchPair(darDefaultAAA, dulUC1);
        assertResponse(pair, true);
    }

    @Test
    public void testAggregateAnalysisA_UC2() {

        // Testing the case where:
        // DAR is yes cancer, yes aggregate
        // DUL is yes cancer, yes aggregate
        // Response should be positive

        MatchPair pair = new MatchPair(darAAA, dulUC2);
        assertResponse(pair, true);
    }

    @Test
    public void testDefaultAggregateAnalysisA_UC2() {

        // Testing the case where:
        // DAR is yes cancer, yes aggregate  and non profit. Not controls, population and methods
        // DUL is yes cancer, yes aggregate
        // Response should be positive

        MatchPair pair = new MatchPair(darDefaultAAA, dulUC2);
        assertResponse(pair, true);
    }

    @Test
    public void testAggregateAnalysisA_UC3() {

        // Testing the case where:
        // DAR is yes cancer, yes aggregate analysis
        // DUL is yes cancer, no aggregate analysis (i.e., outside of cancer)
        // Response should be positive

        MatchPair pair = new MatchPair(darAAA, dulUC3);
        assertResponse(pair, true);
    }

    @Test
    public void testDefaultAggregateAnalysisA_UC3() {

        // Testing the case where:
        // DAR is yes cancer, yes aggregate  and non profit. Not controls, population and methods
        // DUL is yes cancer, no aggregate analysis (i.e., outside of cancer)
        // Response should be positive

        MatchPair pair = new MatchPair(darDefaultAAA, dulUC3);
        assertResponse(pair, true);
    }

    @Test
    public void testAggregateAnalysisB_UC1() {

        // Testing the case where:
        // DAR is yes cancer
        // DUL is yes cancer
        // Response should be positive

        MatchPair pair = new MatchPair(darAAB, dulUC1);
        assertResponse(pair, true);
    }

    @Test
    public void testDefaultAggregateAnalysisB_UC1() {

        // Testing the case where:
        // DAR is yes cancer and non profit. Not controls, population and methods
        // DUL is yes cancer
        // Response should be positive

        MatchPair pair = new MatchPair(darDefaultAAB, dulUC1);
        assertResponse(pair, true);
    }

    @Test
    public void testAggregateAnalysisB_UC2() {

        // Testing the case where:
        // DAR is yes cancer
        // DUL is yes cancer, yes aggregate
        // Response should be positive

        MatchPair pair = new MatchPair(darAAB, dulUC2);
        assertResponse(pair, true);
    }



    @Test
    public void testDefaultAggregateAnalysisB_UC2() {

        // Testing the case where:
        // DAR is yes cancer and non profit. Not controls, population and methods
        // DUL is yes cancer, yes aggregate
        // Response should be positive

        MatchPair pair = new MatchPair(darDefaultAAB, dulUC2);
        assertResponse(pair, true);
    }

    @Test
    public void testAggregateAnalysisB_UC3() {

        // Testing the case where:
        // DAR is yes cancer
        // DUL is yes cancer, no aggregate analysis (i.e., outside of cancer)
        // Response should be positive

        MatchPair pair = new MatchPair(darAAB, dulUC3);
        assertResponse(pair, true);
    }

    @Test
    public void testDefaultAggregateAnalysisB_UC3() {

        // Testing the case where:
        // DAR is yes cancer and non profit. Not controls, population and methods
        // DUL is yes cancer, no aggregate analysis (i.e., outside of cancer) and non profit.
        // Response should be positive

        MatchPair pair = new MatchPair(darDefaultAAB, dulUC3);
        assertResponse(pair, true);
    }

    @Test
    public void testAggregateAnalysisC_UC1() {

        // Testing the case where:
        // DAR is aggregate research
        // DUL is yes cancer
        // Response should be positive

        MatchPair pair = new MatchPair(darAAC, dulUC1);
        assertResponse(pair, true);
    }

    @Test
    public void testDefaultAggregateAnalysisC_UC1() {

        // Testing the case where:
        // DAR is aggregate research and non profit. Not controls, population and methods
        // DUL is yes cancer
        // Response should be positive

        MatchPair pair = new MatchPair(darDefaultAAC, dulUC1);
        assertResponse(pair, true);
    }

    @Test
    public void testAggregateAnalysisC_UC2() {

        // Testing the case where:
        // DAR is aggregate research
        // DUL is yes cancer, yes aggregate
        // Response should be positive

        MatchPair pair = new MatchPair(darAAC, dulUC2);
        assertResponse(pair, true);
    }

    @Test
    public void testDefaultAggregateAnalysisC_UC2() {

        // Testing the case where:
        // DAR is aggregate research and non profit. Not controls, population and methods
        // DUL is yes cancer, yes aggregate
        // Response should be positive

        MatchPair pair = new MatchPair(darDefaultAAC, dulUC2);
        assertResponse(pair, true);
    }

    @Test
    public void testAggregateAnalysisC_UC3() {

        // Testing the case where:
        // DAR is yes aggregate only
        // DUL is yes cancer, no aggregate analysis (i.e., outside of cancer)
        // Response should be positive

        MatchPair pair = new MatchPair(darAAC, dulUC3);
        assertResponse(pair, false);
    }

    @Test
    public void testDefaultAggregateAnalysisC_UC3() {

        // Testing the case where:
        // DAR is aggregate research and non profit. Not controls, population and methods
        // DUL is yes cancer, no aggregate analysis (i.e., outside of cancer)
        // Response should be positive

        MatchPair pair = new MatchPair(darDefaultAAC, dulUC3);
        assertResponse(pair, false);
    }

}
