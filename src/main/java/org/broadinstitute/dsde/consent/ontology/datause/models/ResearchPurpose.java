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

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class ResearchPurpose {

    private String id, researcher;
    private UseRestriction purpose;
    private String[] sensitiveTags;

    public ResearchPurpose() {}
    public ResearchPurpose(String id, String researcher, UseRestriction purpose, String[] sensitiveTags) {
        this.id = id;
        this.researcher = researcher;
        this.purpose = purpose;
        this.sensitiveTags = sensitiveTags.clone();
    }

    public String getId() { return id; }
    public String getResearcher() { return researcher; }
    public String[] getSensitiveTags() { return sensitiveTags; }
    public UseRestriction getPurpose() { return purpose; }

    public void setId(String id) { this.id = id; }
    public void setResearcher(String researcher) { this.researcher = researcher; }
    public void setSensitiveTags(String[] tags) { this.sensitiveTags = tags.clone(); }
    public void setPurpose(UseRestriction purpose) { this.purpose = purpose; }

    public int hashCode() { return id.hashCode(); }
    public boolean equals(Object o) {
        return ((o instanceof ResearchPurpose) && ((ResearchPurpose)o).id.equals(id));
    }

    public String toString() {
        try {
            return (new ObjectMapper()).writer().writeValueAsString(this);
        } catch (IOException e) {
            return id;
        }
    }
}
