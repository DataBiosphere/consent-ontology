package org.broadinstitute.dsde.consent.ontology.resources.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TermResourceMinimalDeserializer extends StdDeserializer<TermResource> {

    public TermResourceMinimalDeserializer() {
        this(null);
    }

    public TermResourceMinimalDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public TermResource deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        JsonNode idNode = node.get("id");
        String id = idNode == null ? "" : idNode.asText();
        JsonNode labelNode = node.get("label");
        String label = labelNode == null ? "" : labelNode.asText();
        JsonNode definitionNode = node.get("definition");
        String definition = definitionNode == null ? "" : definitionNode.asText();
        List<String> synonyms = new ArrayList<>();
        JsonNode arrayNode = node.get("synonyms");
        if (arrayNode != null && arrayNode.isArray()) {
            for (JsonNode synonym: arrayNode) {
                if (synonym != null) {
                    synonyms.add(synonym.asText());
                }
            }
        }
        TermResource term = new TermResource();
        term.setId(id);
        term.setLabel(label);
        term.setDefinition(definition);
        term.setSynonyms(synonyms);
        return term;
    }
}
