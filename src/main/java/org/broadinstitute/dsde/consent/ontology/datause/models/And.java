/**
 * Copyright 2014 Genome Bridge LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.broadinstitute.dsde.consent.ontology.datause.models;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.RDFList;
import com.hp.hpl.jena.rdf.model.RDFNode;

import java.util.Arrays;

public class And extends UseRestriction {
    private UseRestriction[] operands;

    public And() {}
    public And(UseRestriction... operands) {
        this.operands = operands;
    }

    public void setOperands(UseRestriction[] ops) { this.operands = ops.clone(); }
    public UseRestriction[] getOperands() { return operands; }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < operands.length; i++) {
            if(i > 0) { sb.append(","); }
            sb.append(operands[i].toString());
        }
        return String.format("{ \"type\": \"and\", \"operands\": [%s] }", sb.toString());
    }

    public int hashCode() { return Arrays.hashCode(operands); }

    public boolean equals(Object o) {
        if(!(o instanceof And)) { return false; }
        And r = (And)o;
        return Arrays.deepEquals(operands, r.operands);
    }

    @Override
    public OntClass createOntologicalRestriction(OntModel model) {
        RDFNode[] nodes = new RDFNode[operands.length];
        for(int i = 0; i < nodes.length; i++) {
            nodes[i] = operands[i].createOntologicalRestriction(model);
        }
        RDFList list = model.createList(nodes);
        return model.createIntersectionClass(null, list);
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
