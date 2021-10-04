package org.broadinstitute.dsde.consent.ontology.truthtable;

import com.google.common.io.Resources;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import org.broadinstitute.dsde.consent.ontology.AbstractTest;
import org.broadinstitute.dsde.consent.ontology.model.MatchMessage;
import org.broadinstitute.dsde.consent.ontology.model.MatchPair;
import org.broadinstitute.dsde.consent.ontology.service.OntModelFactory;
import org.junit.Assert;

/**
 * See https://docs.google.com/document/d/1xyeYoIKBDFGAsQ_spoK5Ye5esMOXqpRBojd6ijZWJkk
 * for a summary of use cases for which this test class covers.
 *
 * See org.broadinstitute.dsde.consent.ontology.datause.builder.ConsentUseCases and
 * org.broadinstitute.dsde.consent.ontology.datause.builder.DARUseCases
 * for test cases.
 */
@SuppressWarnings("UnstableApiUsage")
public class TruthTableTests extends AbstractTest {

    private final Collection<URL> RESOURCES = Arrays.asList(
        Resources.getResource("diseases.owl"),
        Resources.getResource("data-use.owl"));

    private final OntModelFactory ontModelFactory = OntModelFactory.INSTANCE;

    void assertResponse(MatchPair pair, Boolean expected) {
        MatchMessage message = new MatchMessage(RESOURCES, pair);
        try {
            Assert.assertEquals(ontModelFactory.matchPurpose(message), expected);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

}
