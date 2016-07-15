package org.broadinstitute.dsde.consent.ontology;

import io.dropwizard.testing.junit.ResourceTestRule;
import org.broadinstitute.dsde.consent.ontology.datause.services.TextTranslationService;
import org.broadinstitute.dsde.consent.ontology.resources.TranslateResource;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.InjectMocks;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class TranslateTest {

    @InjectMocks
    private static TranslateResource translateResource = new TranslateResource();

    @ClassRule
    public static final ResourceTestRule gRule = ResourceTestRule.builder()
            .addResource(translateResource).addProperty("helper", new MockTranslationHelper()).build();

    
    @BeforeClass
    public static void testInTempFolder() throws IOException {
        TextTranslationService mockTranslationHelper = new MockTranslationHelper();
        translateResource.setHelper(mockTranslationHelper);
    }

    @Test
    public void testPediatricCancerSample() {
        translate("sampleset",
                "{\"type\":\"and\",\"operands\":[{\"type\":\"named\",\"name\":\"http://purl.obolibrary.org/obo/DOID_162\"}," +
                "{\"type\":\"named\",\"name\":\"http://www.broadinstitute.org/ontologies/DUOS/children\"}," +
                "{\"type\":\"named\",\"name\":\"http://www.broadinstitute.org/ontologies/DUOS/Non_profit\"}]}",
                "translated sampleset");
    }

    @Test
    public void testBroadPurpose() {
        translate("purpose",
                "{\"type\":\"and\",\"operands\":[{\"type\":\"named\",\"name\":\"http://www.broadinstitute.org/ontologies/DUOS/Broad\"}," +
                "{\"type\":\"named\",\"name\":\"http://www.broadinstitute.org/ontologies/DUOS/Non_profit\"}]}",
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
