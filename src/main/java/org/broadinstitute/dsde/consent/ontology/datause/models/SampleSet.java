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

public class SampleSet {

    private String id, owner;
    private Boolean requiresManualReview;
    private UseRestriction restriction;

    public SampleSet() {
        this.id = null;
        this.owner = null;
        this.requiresManualReview = null;
        this.restriction = null;
    }

    public SampleSet(String id, String owner, Boolean requiresManualReview, UseRestriction restriction) {
        this.id = id;
        this.owner = owner;
        this.requiresManualReview = requiresManualReview;
        this.restriction = restriction;
    }

    public String getId() { return id; }
    public String getOwner() { return owner; }
    public Boolean getRequiresManualReview() { return requiresManualReview; }
    public UseRestriction getRestriction() { return restriction; }

    public void setId(String id) { this.id = id; }
    public void setOwner(String owner) { this.owner = owner; }
    public void setRequiresManualReview(Boolean requiresManualReview) {
        this.requiresManualReview = requiresManualReview;
    }
    public void setUseRestriction(UseRestriction useRestriction) { this.restriction = useRestriction; }

    public String toString() { return String.format("%s \"%s\"", id, owner); }

    public int hashCode() { return id != null ? id.hashCode() : 13; }

    public boolean equals(Object o) {
        if(!(o instanceof SampleSet)) { return false; }
        SampleSet ss = (SampleSet)o;
        // test == equality, to catch the situation where they're both null.
        return ss.id == id || ss.id.equals(id);
    }
}
