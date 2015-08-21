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

public class OntologyTerm {
    private String id;
    private String label;
    private String definition;
    private String[] synonyms;

    public OntologyTerm() {
    }

    public OntologyTerm(String id, String label, String definition, String[] synonyms) {
        this.id = id;
        this.label = label;
        this.definition = definition;
        this.synonyms = synonyms;
    }

    public String getId() { return id; }
    public String getLabel() { return label; }
    public String getDefinition() { return definition; }
    public String[] getSynonyms() { return synonyms; }

    public void setId(String id) { this.id = id; }
    public void setLabel(String label) { this.label = label; }
    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public void setSynonyms(String[] synonyms) {
        this.synonyms = synonyms;
    }

    @Override
    public String toString() {
        String synonym = null;
        for (String s: synonyms) {
            if (synonym == null) {
                synonym = s;
            } else {
                synonym += "," + s;
            }
        }
        return "OntologyTerm{" +
                "id='" + id + '\'' +
                ", label='" + label + '\'' +
                ", definition='" + definition + '\'' +
                ", synonyms=" + synonym +
                '}';
    }
}
