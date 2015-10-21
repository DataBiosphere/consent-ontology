package org.broadinstitute.dsde.consent.ontology;

import io.dropwizard.testing.junit.ResourceTestRule;
import java.io.IOException;
import org.broadinstitute.dsde.consent.ontology.resources.TranslateResource;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import org.broadinstitute.dsde.consent.ontology.datause.ontology.TranslationHelper;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.mockito.InjectMocks;

public class TranslateTest {

    @InjectMocks
    private static TranslateResource translateResource = new TranslateResource();

    @ClassRule
    public static final ResourceTestRule gRule = ResourceTestRule.builder()
            .addResource(translateResource).addProperty("helper", new MockTranslationHelper()).build();

    
    @BeforeClass
    public static void testInTempFolder() throws IOException {
        TranslationHelper mockTranslationHelper = new MockTranslationHelper();
        translateResource.setHelper(mockTranslationHelper);
    }

    @Test
    public void testPediatricCancerSample() {
        translate("sampleset",
                "{\"type\":\"and\",\"operands\":[{\"type\":\"named\",\"name\":\"http://purl.obolibrary.org/obo/DOID_162\"}," +
                "{\"type\":\"named\",\"name\":\"http://www.broadinstitute.org/ontologies/DURPO/children\"}," +
                "{\"type\":\"named\",\"name\":\"http://www.broadinstitute.org/ontologies/DURPO/Non_profit\"}]}",
                "translated sampleset");
    }

    @Test
    public void testBroadPurpose() {
        translate("purpose",
                "{\"type\":\"and\",\"operands\":[{\"type\":\"named\",\"name\":\"http://www.broadinstitute.org/ontologies/DURPO/Broad\"}," +
                "{\"type\":\"named\",\"name\":\"http://www.broadinstitute.org/ontologies/DURPO/Non_profit\"}]}",
                "translated purpose");
    }

    private void translate(String qType, String json, String expect) {
        Response response =
                gRule.client().target("/translate")
                        .queryParam("for", qType)
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .accept(MediaType.TEXT_PLAIN_TYPE)
                        .post(Entity.json(json));
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.readEntity(String.class)).isEqualTo(expect);
    }
}
