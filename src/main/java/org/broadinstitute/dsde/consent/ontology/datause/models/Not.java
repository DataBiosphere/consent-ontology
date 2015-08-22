package org.broadinstitute.dsde.consent.ontology.datause.models;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;

public class Not extends UseRestriction {

    private UseRestriction operand;

    public Not() {}

    public Not(UseRestriction operand) {
        this.operand = operand;
    }

    public UseRestriction getOperand() { return operand; }
    public void setOperand(UseRestriction op) { operand = op; }

    public String toString() {
        return String.format("{ \"type\": \"not\", \"operand\": %s }", operand.toString());
    }

    public int hashCode() { return 37 * (operand.hashCode() + 1); }

    public boolean equals(Object o) {
        return (o instanceof Not) && ((Not)o).operand.equals(operand);
    }

    @Override
    public OntClass createOntologicalRestriction(OntModel model) {
        return model.createComplementClass(null, operand.createOntologicalRestriction(model));
    }

    public boolean visitAndContinue(UseRestrictionVisitor visitor) {
        return visitor.visit(operand);
    }
}