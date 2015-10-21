package org.broadinstitute.dsde.consent.ontology;

import org.broadinstitute.dsde.consent.ontology.datause.api.ResearchPurposeMatch;
import org.broadinstitute.dsde.consent.ontology.datause.models.*;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.UUID;
import org.broadinstitute.dsde.consent.ontology.datause.ontologies.OntologyList;
import org.broadinstitute.dsde.consent.ontology.datause.ontologies.OntologyModel;
import org.junit.Ignore;

public class DataUseOwlTest {

    private static ResearchPurposeMatch matcher;

    private static final String UID = UUID.randomUUID().toString();

    private static final ResearchPurpose methodsPurpose = new ResearchPurpose(
            UUID.randomUUID().toString(),
            new Named("http://www.broadinstitute.org/ontologies/DURPO/methods_research"));

    private static final ResearchPurpose aggregatePurpose = new ResearchPurpose(
            UUID.randomUUID().toString(),
            new Named("http://www.broadinstitute.org/ontologies/DURPO/aggregate_research"));

    @BeforeClass
    public static void setUp() throws Exception {
        matcher = new ResearchPurposeMatch();
        OntologyModel ontologyList = new OntologyList();
        matcher.setOntologyList(ontologyList);
    }

    @Test
    public void testNegativeMethodsAgainstInverse() {
        Consent consent = new Consent(UID, 
                new Not(new Named("http://www.broadinstitute.org/ontologies/DURPO/methods_research")));
        Boolean b = matcher.matchPurpose(methodsPurpose, consent);
        Assert.assertFalse(b);
    }

    @Test
    public void testNegativeMethodsAgainstNothing() {
        Consent consent = new Consent(UID, new Nothing());
        Boolean b = matcher.matchPurpose(methodsPurpose, consent);
        Assert.assertFalse(b);
    }

    @Ignore
    @Test
    public void testPositiveAggregate() {
        Consent consent = new Consent(UID, new Everything());
        Boolean b = matcher.matchPurpose(aggregatePurpose, consent);
        Assert.assertTrue(b);
    }

    @Test
    public void testNegativeAggregateAgainstInverse() {
        Consent consent = new Consent(UID,
                new Not(new Named("http://www.broadinstitute.org/ontologies/DURPO/aggregate_research")));
        Boolean b = matcher.matchPurpose(aggregatePurpose, consent);
        Assert.assertFalse(b);
    }
    @Test
    public void testNegativeAggregateAgainstNothing() {
        Consent consent = new Consent(UID, new Nothing());
        Boolean b = matcher.matchPurpose(aggregatePurpose, consent);
        Assert.assertFalse(b);
    }

}
