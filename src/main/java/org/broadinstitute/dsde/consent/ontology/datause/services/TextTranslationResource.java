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
package org.broadinstitute.dsde.consent.ontology.datause.services;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import org.broadinstitute.dsde.consent.ontology.datause.api.LuceneOntologyTermSearchAPI;
import org.broadinstitute.dsde.consent.ontology.datause.models.Named;
import org.broadinstitute.dsde.consent.ontology.datause.models.UseRestriction;
import org.broadinstitute.dsde.consent.ontology.datause.models.UseRestrictionVisitor;
import org.broadinstitute.dsde.consent.ontology.datause.ontologies.OntologyList;
import org.broadinstitute.dsde.consent.ontology.datause.utils.ClassLoaderResourceLoader;
import org.mindswap.pellet.jena.PelletInfGraph;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TextTranslationResource {

    private LuceneOntologyTermSearchAPI api;

    // This is a cache, used to associate a type (element of
    // the set { "disease", "organization", "commercial-status" }) to each named class.
    // It's an expensive operation (we're going to have to use a reasoner!) so we want
    // to cache it here in this class and not use it again if we don't need to.
    private Map<String,String> namedClassTypes;

    private OntModel model;

    public TextTranslationResource(LuceneOntologyTermSearchAPI api)
            throws IOException, OWLOntologyCreationException {

        this.api = api;
        this.namedClassTypes = new ConcurrentHashMap<String,String>();

        OntologyList list = new OntologyList("ontologies.txt",
                new ClassLoaderResourceLoader(getClass().getClassLoader()));
        model = list.loadOntModel();
        ((PelletInfGraph) model.getGraph()).classify();

    }

    public String translateSample(String restrictionStr) throws IOException {
        return translate("sampleset", restrictionStr);
    }

    public String translatePurpose(String restrictionStr) throws IOException {
        return translate("purpose", restrictionStr);
    }

    private String translate(String translateFor, String restrictionStr) throws IOException {
        UseRestriction restriction = UseRestriction.parse(restrictionStr);
        return translate(translateFor, restriction);
    }

    private String translate(String translateFor,
                             UseRestriction restriction) {

        ArrayList<String> clauses = new ArrayList<String>();

        boolean forSampleSet = translateFor.equals("sampleset");

        String disease = buildDiseaseClause(forSampleSet, restriction);
        String geography = buildGeographyClause(forSampleSet, restriction);
        String population = buildPopulationClause(forSampleSet, restriction);
        String commercial = buildNonProfitClause(forSampleSet, restriction);

        if(disease != null) { clauses.add(disease); }
        if(geography != null) { clauses.add(geography); }
        if(population != null) { clauses.add(population); }
        if(commercial != null) { clauses.add(commercial); }

        if(clauses.isEmpty()) {
            return forSampleSet
                ? "No restrictions."
                : "Any sample which has no restrictions.";
        }

        String first = String.format(
                forSampleSet
                        ? "Samples %s."
                        : "Any sample which %s.", clauses.remove(0));
        String rest = "";
        if(!clauses.isEmpty()) {
            rest = String.format(
                    forSampleSet
                    ? " In addition, samples %s."
                    : " In addition, those samples %s.", buildAndClause(clauses));
        }

        return String.format("%s%s", first, rest);
    }

    // "Samples may only be used for studying men, and Asian-American populations."
    public String buildPopulationClause(boolean useMay, UseRestriction r) {
        Set<String> labels = findLabeledTypedClasses("population", r);

        return labels.isEmpty() ? null :
                String.format("%s be used for the study of %s",
                        useMay ? "may only" : "can",
                        buildOrClause(labels));
    }


    // "Samples may only be used for research at institutions in North America, Europe, or South America."
    public String buildGeographyClause(boolean useMay, UseRestriction r) {
        Set<String> labels = findLabeledTypedClasses("geography", r);

        return labels.isEmpty() ? null :
                String.format("%s be used for research at institutions in %s",
                        useMay ? "may only" : "can",
                        buildOrClause(labels));
    }

    // "Samples may not be used for commercial purposes."
    public String buildNonProfitClause(boolean useMay, UseRestriction r) {
        if(hasTypedClass("non-profit", r)) {
            return useMay ? "may not be used for commercial purposes" : null;
        } else {
            return useMay ? null : "can be used for commercial purposes";
        }
    }

    // "Samples may only be used for the purpose of studying breast cancer, thyroid cancer, or diabetes."
    public String buildDiseaseClause(boolean useMay, UseRestriction r) {
        Set<String> labels = findLabeledTypedClasses("disease", r);
        String diseaseNames = null;

        if(labels.size() == 0) {
            return null;
        } else {
            diseaseNames = buildOrClause(labels);
        }

        return String.format("%s be used for the purpose of studying %s", useMay ? "may only" : "can", diseaseNames);
    }

    public String buildOrClause(Collection<String> labels) {
        String[] array = labels.toArray(new String[0]);
        if(array.length == 1) { return array[0]; }
        if(array.length == 2) { return String.format("%s or %s", array[0], array[1]); }

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < array.length; i++) {
            if(i > 0) {
                sb.append(", ");
            }
            if(i == array.length-1) {
                sb.append("or ");
            }
            sb.append(array[i]);
        }

        return sb.toString();
    }

    public String buildAndClause(Collection<String> labels) {
        String[] array = labels.toArray(new String[0]);
        if(array.length == 1) { return array[0]; }
        if(array.length == 2) { return String.format("%s and %s", array[0], array[1]); }

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < array.length; i++) {
            if(i > 0) {
                sb.append(", ");
            }
            if(i == array.length-1) {
                sb.append("and ");
            }
            sb.append(array[i]);
        }

        return sb.toString();
    }

    public String getNamedClassLabel(Named n) {
        try {
            return api.findById(n.getName()).getLabel();
        } catch (IOException e) {
            e.printStackTrace(System.err);

            String[] array = n.getName().split("/");
            return array[array.length-1];
        }
    }

    public String getNamedClassType(Named n) {
        String result = namedClassTypes.get(n.getName());
        if ( result == null && !namedClassTypes.containsKey(n.getName()) ) {
            result = findNamedClassType(n);
            namedClassTypes.put(n.getName(), result);
        }
        return result;
    }

    private String findNamedClassType(Named n) {

        if(isDiseaseClass(n)) { return "disease"; }
        if(isNonProfitStatus(n)) { return "non-profit"; }

        OntClass cls = model.getOntClass(n.getName());

        OntClass geography = model.getOntClass("http://www.genomebridge.org/ontologies/DURPO/geography");
        if(cls.hasSuperClass(geography)) { return "geography"; }

        OntClass population = model.getOntClass("http://www.genomebridge.org/ontologies/DURPO/population");
        if(cls.hasSuperClass(population)) { return "population"; }

        return null;
    }

    private Set<String> findLabeledTypedClasses(String type, UseRestriction r) {
        Set<Named> named = findNamedClasses(new NamedTypePredicate(type), r);
        Set<String> labels = new LinkedHashSet<String>();
        for(Named n : named) { labels.add(getNamedClassLabel(n)); }
        return labels;
    }

    private Set<Named> findNamedClasses(RestrictionPredicate pred, UseRestriction r) {
        FilterVisitor visitor = new FilterVisitor(pred);
        r.visit(visitor);
        Set<Named> named = new HashSet<Named>();
        for(UseRestriction n : visitor.getMatched()) {
            named.add(((Named)n));
        }
        return named;
    }

    private boolean hasTypedClass(String type, UseRestriction r) {
        return hasClass(new NamedTypePredicate(type), r);
    }

    private boolean hasClass(RestrictionPredicate pred, UseRestriction r) {
        FindVisitor visitor = new FindVisitor(pred);
        r.visit(visitor);
        return visitor.isFound();
    }

    private class NamedTypePredicate implements RestrictionPredicate {

        private String type;
        public NamedTypePredicate(String t) { this.type = t; }

        public boolean accepts(UseRestriction r) {
            return (r instanceof Named) &&
                    getNamedClassType((Named)r).equals(type);
        }
    }

    private boolean isDiseaseClass(Named n) {
        return TextTranslationResource.isDiseaseClass(n.getName());
    }

    // TODO: remove/move this static method; we don't like statics
    // TODO: a more precise means of testing if this term is a disease or not
    public static boolean isDiseaseClass(String s) {
        return s.contains("DOID") || s.contains("SYMP");
    }

    /*
    private class DiseaseClassPredicate implements RestrictionPredicate {
        public boolean accepts(UseRestriction r) {
            return (r instanceof Named) && (isDiseaseClass((Named)r));
        }
    }
    */

    private boolean isNamedEquals(UseRestriction r, String n) {
        return (r instanceof Named) && ((Named)r).getName().equals(n);
    }

    private boolean isNonProfitStatus(UseRestriction r) {
        return isNamedEquals(r, "http://www.genomebridge.org/ontologies/DURPO/Non_profit");
    }

    /*
    private class NonProfitPredicate implements RestrictionPredicate {
        public boolean accepts(UseRestriction r) { return isNonProfitStatus(r); }
    }
    */

}

interface RestrictionPredicate {
    public boolean accepts(UseRestriction r);
}

abstract class SimpleUseRestrictionVisitor implements UseRestrictionVisitor {
    public void startChildren() {}
    public void endChildren() {}
}

class FilterVisitor extends SimpleUseRestrictionVisitor {

    private RestrictionPredicate predicate;
    private ArrayList<UseRestriction> matched;

    public FilterVisitor(RestrictionPredicate p) {
        this.predicate = p;
        matched = new ArrayList<UseRestriction>();
    }

    public boolean visit(UseRestriction r) {

        if(predicate.accepts(r)) {
            matched.add(r);
        }
        return true;
    }

    public Collection<UseRestriction> getMatched() { return matched; }

}

class FindVisitor extends SimpleUseRestrictionVisitor {

    private RestrictionPredicate predicate;
    private boolean found;

    public FindVisitor(RestrictionPredicate p) {
        this.predicate = p;
        found = false;
    }

    public boolean visit(UseRestriction r) {

        if(predicate.accepts(r)) {
            found = true;
            return false;
        }
        return true;
    }

    public boolean isFound() { return found; }
}
