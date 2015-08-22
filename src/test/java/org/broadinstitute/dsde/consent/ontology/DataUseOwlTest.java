package org.broadinstitute.dsde.consent.ontology;

import org.broadinstitute.dsde.consent.ontology.datause.api.ResearchPurposeMatch;
import org.broadinstitute.dsde.consent.ontology.datause.models.*;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.UUID;

public class DataUseOwlTest {

    private static ResearchPurposeMatch matcher;

    private static final String UID = UUID.randomUUID().toString();

    private static final ResearchPurpose methodsPurpose = new ResearchPurpose(
            UUID.randomUUID().toString(),
            "researcher",
            new Named("http://www.genomebridge.org/ontologies/DURPO/methods_research"),
            new String[]{});

    private static final ResearchPurpose aggregatePurpose = new ResearchPurpose(
            UUID.randomUUID().toString(),
            "researcher",
            new Named("http://www.genomebridge.org/ontologies/DURPO/aggregate_research"),
            new String[]{});

    @BeforeClass
    public static void setUp() throws Exception {
        matcher = new ResearchPurposeMatch("data-use.owl");
    }

    @Test
    public void testNegativeMethodsAgainstInverse() {
        SampleSet consent = new SampleSet(UID, "owner", false,
                new Not(new Named("http://www.genomebridge.org/ontologies/DURPO/methods_research")));
        Boolean b = matcher.matchPurpose(methodsPurpose, consent);
        Assert.assertFalse(b);
    }

    @Test
    public void testNegativeMethodsAgainstNothing() {
        SampleSet consent = new SampleSet(UID, "owner", false, new Nothing());
        Boolean b = matcher.matchPurpose(methodsPurpose, consent);
        Assert.assertFalse(b);
    }

    @Test
    public void testPositiveAggregate() {
        SampleSet consent = new SampleSet(UID, "owner", false, new Everything());
        Boolean b = matcher.matchPurpose(aggregatePurpose, consent);
        Assert.assertTrue(b);
    }

    @Test
    public void testNegativeAggregateAgainstInverse() {
        SampleSet consent = new SampleSet(UID, "owner", false,
                new Not(new Named("http://www.genomebridge.org/ontologies/DURPO/aggregate_research")));
        Boolean b = matcher.matchPurpose(aggregatePurpose, consent);
        Assert.assertFalse(b);
    }

    @Test
    public void testNegativeAggregateAgainstNothing() {
        SampleSet consent = new SampleSet(UID, "owner", false, new Nothing());
        Boolean b = matcher.matchPurpose(aggregatePurpose, consent);
        Assert.assertFalse(b);
    }

}
