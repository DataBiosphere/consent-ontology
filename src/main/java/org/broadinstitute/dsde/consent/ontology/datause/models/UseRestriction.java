package org.broadinstitute.dsde.consent.ontology.datause.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.google.gson.Gson;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import io.dropwizard.jackson.Jackson;
import org.broadinstitute.dsde.consent.ontology.datause.models.visitor.UseRestrictionVisitor;

import java.io.IOException;

@JsonIgnoreProperties()
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = Everything.class, name = "everything"),
    @JsonSubTypes.Type(value = Nothing.class, name = "nothing"),
    @JsonSubTypes.Type(value = Named.class, name = "named"),
    @JsonSubTypes.Type(value = Some.class, name = "some"),
    @JsonSubTypes.Type(value = Only.class, name = "only"),
    @JsonSubTypes.Type(value = And.class, name = "and"),
    @JsonSubTypes.Type(value = Or.class, name = "or"),
    @JsonSubTypes.Type(value = Not.class, name = "not")
})
public abstract class UseRestriction {

    private static ObjectMapper mapper = Jackson.newObjectMapper();

    public abstract OntClass createOntologicalRestriction(OntModel model);

    public static UseRestriction parse(String str) throws IOException {
        ObjectReader reader = mapper.readerFor(UseRestriction.class);
        return reader.readValue(str);
    }

    public boolean visit(UseRestrictionVisitor visitor) {
        boolean shouldContinue;
        if ((shouldContinue = visitor.visit(this))) {
            visitor.startChildren();
            shouldContinue = visitAndContinue(visitor);
            visitor.endChildren();
        }

        return shouldContinue;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public abstract boolean visitAndContinue(UseRestrictionVisitor visitor);

}
