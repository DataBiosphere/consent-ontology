package org.broadinstitute.dsde.consent.ontology.resources;

import org.apache.commons.io.FileUtils;
import org.broadinstitute.dsde.consent.ontology.resources.model.TermParent;
import org.broadinstitute.dsde.consent.ontology.resources.model.TermResource;
import org.broadinstitute.dsde.consent.ontology.service.ElasticSearchAutocompleteAPI;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class OntologySearchParentTest {

    static ElasticSearchAutocompleteAPI api;
    static OntologySearchResource resource;
    static Node node;
    static String index = "test-index";

    // Construction of children and parents.
    static TermResource child = new TermResource();
    static TermResource parent1 = new TermResource();
    static TermResource parent2 = new TermResource();

    @BeforeClass
    public static void setUp() throws Exception {
        // Mocks an in-memory elastic-search node
        node = NodeBuilder.nodeBuilder().node();
        Runtime.getRuntime().addShutdownHook(new Thread(node::close));
        node.start();
        Client client = node.client();
        api = new ElasticSearchAutocompleteAPI(client, index);
        resource = new OntologySearchResource(api);

        parent1.setId("parent1");
        parent1.setLabel("parent1 label");
        parent1.setDefinition("parent1 definition");
        parent1.setSynonyms(new ArrayList<>(Arrays.asList("parent 1", "parent 01")));

        parent2.setId("parent2");
        parent2.setLabel("parent2 label");
        parent2.setDefinition("parent2 definition");
        parent2.setSynonyms(new ArrayList<>(Arrays.asList("parent 2", "parent 02")));

        // These are the parents for the child - each one refers to a full parent node.
        TermParent childParent1 = new TermParent();
        TermParent childParent2 = new TermParent();
        childParent1.setId(parent1.getId());
        childParent1.setOrder(1);
        childParent2.setId(parent2.getId());
        childParent2.setOrder(2);

        child.setId("child");
        child.setLabel("child label");
        child.setDefinition("child definition");
        child.setSynonyms(new ArrayList<>(Arrays.asList("child 1", "child 01")));
        child.setParents(new ArrayList<>());
        child.getParents().add(childParent1);
        child.getParents().add(childParent2);

        // Push the terms to the in-memory ES index
        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
        // prevents a race condition where the data isn't available when it's queried.
        bulkRequestBuilder.setRefresh(true);
        for (TermResource term : Arrays.asList(child, parent1, parent2)) {
            bulkRequestBuilder.add(client.prepareIndex(index, "ontology_term")
                .setSource(buildDocument(term))
                .setId(term.getId())
            );
        }
        bulkRequestBuilder.execute().actionGet();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        node.stop();
        // The in-memory ES creates a "data" directory for indexes. Clean that up after tests.
        FileUtils.deleteDirectory(new File("data"));
    }
    
    @Test
    public void testGetChildWithParents() throws Exception {
        Response response = resource.getOntologyById(child.getId());
        assertTrue(response.getStatus() == 200);
        List<TermResource> terms = (List<TermResource>) response.getEntity();
        assertTrue(terms.size() == 1);

        TermResource term = terms.get(0);
        assertTrue(child.getId().equals(term.getId()));
        assertTrue(child.getLabel().equals(term.getLabel()));
        assertTrue(child.getDefinition().equals(term.getDefinition()));
        assertTrue(child.getSynonyms().equals(term.getSynonyms()));

        for (TermParent p : term.getParents()) {
            boolean parentChecked = false;
            if (p.getId().equals(parent1.getId())) {
                assertTrue(p.getLabel().equals(parent1.getLabel()));
                assertTrue(p.getDefinition().equals(parent1.getDefinition()));
                assertTrue(p.getSynonyms().equals(parent1.getSynonyms()));
                parentChecked = true;
            } else if (p.getId().equals(parent2.getId())) {
                assertTrue(p.getLabel().equals(parent2.getLabel()));
                assertTrue(p.getDefinition().equals(parent2.getDefinition()));
                assertTrue(p.getSynonyms().equals(parent2.getSynonyms()));
                parentChecked = true;
            }
            // Check that the current parent was actually tested.
            assertTrue(parentChecked);
        }

    }

    private static XContentBuilder buildDocument(TermResource term) throws IOException {
        XContentBuilder builder = XContentFactory.jsonBuilder()
            .startObject()
            .field("id", term.id)
            .field("ontology", "test-type")
            .field("usable", true);
        if (term.getLabel() != null) {
            builder = builder.field("label", term.getLabel());
        }
        if (term.getDefinition() != null) {
            builder = builder.field("definition", term.getDefinition());
        }
        if (term.getSynonyms() != null && term.getSynonyms().size() != 0) {
            builder = builder.array("synonyms", term.getSynonyms().stream().toArray(String[]::new));
        }
        if (term.getParents() != null && !term.getParents().isEmpty()) {
            builder.startArray("parents");
            for (TermParent parent : term.getParents()) {
                builder.startObject();
                builder.field("id", parent.getId());
                builder.field("order", parent.getOrder());
                builder.endObject();
            }
            builder.endArray();
        }
        return builder.endObject();
    }

}
