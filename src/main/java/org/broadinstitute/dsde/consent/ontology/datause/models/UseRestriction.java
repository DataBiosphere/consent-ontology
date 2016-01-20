package org.broadinstitute.dsde.consent.ontology.datause.models;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.google.gson.Gson;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import io.dropwizard.jackson.Jackson;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.broadinstitute.dsde.consent.ontology.datause.api.OntologyTermSearchAPI;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
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

    private static Logger LOG = Logger.getLogger(UseRestriction.class);

    private static ObjectMapper mapper = Jackson.newObjectMapper();

    public abstract OntClass createOntologicalRestriction(OntModel model);

    public static UseRestriction parse(String str) throws IOException {
        try {
            ObjectReader reader = mapper.reader(UseRestriction.class);
            return reader.readValue(str);
        } catch (IOException e) {
            LOG.error(String.format("Parse exception on \"%s\"", str));
            throw e;
        }
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

    @JsonIgnore
    public abstract String getDescriptiveLabel(OntologyTermSearchAPI api) throws IOException;

    @JsonIgnore
    protected static String wrapListItem(String item) {
        return "<li>" + item + "</li>";
    }

    @JsonIgnore
    protected static String wrapList(String item) {
        return "<ul>" + item + "</ul>";
    }

    @JsonIgnore
    protected static String capitalize(String item) {
        return StringUtils.capitalize(item);
    }

}
