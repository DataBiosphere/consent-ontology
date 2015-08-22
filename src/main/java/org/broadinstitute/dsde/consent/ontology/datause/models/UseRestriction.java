package org.broadinstitute.dsde.consent.ontology.datause.models;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;

import java.io.IOException;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME,
        include=JsonTypeInfo.As.PROPERTY,
        property="type")
@JsonSubTypes({
        @JsonSubTypes.Type(value=Everything.class, name="everything"),
        @JsonSubTypes.Type(value=Nothing.class, name="nothing"),
        @JsonSubTypes.Type(value=Named.class, name="named"),
        @JsonSubTypes.Type(value=Some.class, name="some"),
        @JsonSubTypes.Type(value=Only.class, name="only"),
        @JsonSubTypes.Type(value=And.class, name="and"),
        @JsonSubTypes.Type(value=Or.class, name="or"),
        @JsonSubTypes.Type(value=Not.class, name="not")
})
public abstract class UseRestriction {

    private static Logger LOG = Logger.getLogger(UseRestriction.class);

    private static ObjectMapper mapper = new ObjectMapper();

    public abstract OntClass createOntologicalRestriction(OntModel model);

    public static UseRestriction parse(String str) throws IOException {
        try {
            ObjectReader reader = mapper.reader(UseRestriction.class);
            return reader.readValue(str);

        } catch(IOException e) {
            LOG.error(String.format("Parse exception on \"%s\"", str));
            throw e;
        }
    }

    public static String serialize(UseRestriction restriction) throws IOException {
        return mapper.writer().writeValueAsString(restriction);
    }


    public boolean visit(UseRestrictionVisitor visitor) {
        boolean shouldContinue = true;
        if((shouldContinue = visitor.visit(this))) {
            visitor.startChildren();
            shouldContinue = visitAndContinue(visitor);
            visitor.endChildren();
        }

        return shouldContinue;
    }

    public abstract boolean visitAndContinue(UseRestrictionVisitor visitor);
}
