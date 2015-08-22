package org.broadinstitute.dsde.consent.ontology.datause.models;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.RDFList;
import com.hp.hpl.jena.rdf.model.RDFNode;

import java.util.Arrays;

public class Or extends UseRestriction {
    private UseRestriction[] operands;

    public Or() {}

    public Or(UseRestriction... operands) {
        this.operands = operands;
        if(operands.length < 2) {
            throw new IllegalArgumentException("Disjunction must have at least two operands");
        }
    }

    public void setOperands(UseRestriction[] ops) { this.operands = ops.clone(); }
    public UseRestriction[] getOperands() { return operands; }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < operands.length; i++) {
            if(i > 0) { sb.append(" "); }
            sb.append(operands[i].toString());
        }
        return String.format("{ \"type\": \"or\", \"operands\": [%s] }", sb.toString());
    }


    public int hashCode() { return Arrays.hashCode(operands); }

    public boolean equals(Object o) {
        if(!(o instanceof Or)) { return false; }
        Or r = (Or)o;
        return Arrays.deepEquals(operands, r.operands);
    }

    @Override
    public OntClass createOntologicalRestriction(OntModel model) {
        RDFNode[] nodes = new RDFNode[operands.length];
        for(int i = 0; i < nodes.length; i++) {
            nodes[i] = operands[i].createOntologicalRestriction(model);
        }
        RDFList list = model.createList(nodes);
        return model.createUnionClass(null, list);
    }

    public boolean visitAndContinue(UseRestrictionVisitor visitor) {
        for(UseRestriction child : operands) {
            if(!child.visit(visitor)) {
                return false;
            }
        }
        return true;
    }
}