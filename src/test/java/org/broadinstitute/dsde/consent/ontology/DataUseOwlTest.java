package org.broadinstitute.dsde.consent.ontology;

import static org.broadinstitute.dsde.consent.ontology.datause.builder.UseRestrictionBuilderSupport.AGGREGATE_RESEARCH;
import static org.broadinstitute.dsde.consent.ontology.datause.builder.UseRestrictionBuilderSupport.METHODS_RESEARCH;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.io.Resources;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import org.broadinstitute.dsde.consent.ontology.datause.models.Everything;
import org.broadinstitute.dsde.consent.ontology.datause.models.Named;
import org.broadinstitute.dsde.consent.ontology.datause.models.Not;
import org.broadinstitute.dsde.consent.ontology.datause.models.Nothing;
import org.broadinstitute.dsde.consent.ontology.datause.models.UseRestriction;
import org.broadinstitute.dsde.consent.ontology.model.MatchMessage;
import org.broadinstitute.dsde.consent.ontology.model.MatchPair;
import org.broadinstitute.dsde.consent.ontology.service.OntModelFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;


public class DataUseOwlTest extends AbstractTest {

    private static final OntModelFactory ONT_MODEL_CACHE = OntModelFactory.INSTANCE;
    private static final Collection<URL> resources = Collections.singletonList(Resources.getResource("data-use.owl"));
    private static final UseRestriction methodsPurpose = new Named(METHODS_RESEARCH);
    private static final UseRestriction aggregatePurpose = new Named(AGGREGATE_RESEARCH);

    @BeforeClass
    public static void setUp() throws Exception {
    }

    @AfterClass
    public static void after() {
    }


    @Test
    public void testNegativeMethodsAgainstInverse() throws Exception {
        UseRestriction consent = new Not(new Named(METHODS_RESEARCH));
        Boolean b = ONT_MODEL_CACHE.matchPurpose(new MatchMessage(resources, new MatchPair(methodsPurpose, consent)));
        assertFalse(b);
    }

    @Test
    public void testNegativeMethodsAgainstNothing() throws Exception {
        UseRestriction consent = new Nothing();
        Boolean b = ONT_MODEL_CACHE.matchPurpose(new MatchMessage(resources, new MatchPair(methodsPurpose, consent)));
        assertFalse(b);
    }

    @Test
    public void testPositiveAggregate() throws Exception {
        UseRestriction consent = new Everything();
        Boolean b = ONT_MODEL_CACHE.matchPurpose(new MatchMessage(resources, new MatchPair(aggregatePurpose, consent)));
        assertTrue(b);
    }

    @Test
    public void testNegativeAggregateAgainstInverse() throws Exception {
        UseRestriction consent = new Not(new Named(AGGREGATE_RESEARCH));
        Boolean b = ONT_MODEL_CACHE.matchPurpose(new MatchMessage(resources, new MatchPair(aggregatePurpose, consent)));
        assertFalse(b);
    }

    @Test
    public void testNegativeAggregateAgainstNothing() throws Exception {
        UseRestriction consent = new Nothing();
        Boolean b = ONT_MODEL_CACHE.matchPurpose(new MatchMessage(resources, new MatchPair(aggregatePurpose, consent)));
        assertFalse(b);
    }

}
