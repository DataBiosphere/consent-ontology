package org.broadinstitute.dsde.consent.ontology.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.broadinstitute.dsde.consent.ontology.model.TermParent;
import org.broadinstitute.dsde.consent.ontology.model.TermResource;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OntologySearchParentTest {

  @Mock
  AutocompleteService api;

  private OntologySearchResource resource;

  // Construction of children and parents.
  private static final TermResource child = new TermResource();
  private static final TermResource parent1 = new TermResource();
  private static final TermResource parent2 = new TermResource();

  @BeforeEach
  void setUp() {
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
  }

  @Test
  void testGetNodeWithNoParents() throws Exception {
    when(api.lookupById(parent1.getId())).thenReturn(Collections.singletonList(parent1));
    Response response = resource.getOntologyById(parent1.getId());
    assertOKstatusAndTermSize(response);
  }

  @Test
  void testGetChildWithParents() throws Exception {
    when(api.lookupById(child.getId())).thenReturn(Collections.singletonList(child));
    Response response = resource.getOntologyById(child.getId());
    List<TermResource> terms = assertOKstatusAndTermSize(response);

    TermResource term = terms.get(0);
    assertEquals(child.getId(), term.getId());
    assertEquals(child.getLabel(), term.getLabel());
    assertEquals(child.getDefinition(), term.getDefinition());
    assertEquals(child.getSynonyms(), term.getSynonyms());

    assertEquals(2, term.getParents().size(), "Expected two parents");
    term.getParents().sort(Comparator.comparingInt(TermParent::getOrder));

    TermParent actualParent1 = term.getParents().get(0);
    TermParent actualParent2 = term.getParents().get(1);

    assertEquals(actualParent1.getId(), parent1.getId());
    assertEquals(actualParent2.getId(), parent2.getId());
  }

  @SuppressWarnings("unchecked")
  private List<TermResource> assertOKstatusAndTermSize(Response response) {
    assertEquals(200, response.getStatus());
    List<TermResource> terms = (List<TermResource>) response.getEntity();
    assertEquals(1, terms.size());
    return terms;
  }

}
