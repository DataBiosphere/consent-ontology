package org.broadinstitute.dsde.consent.ontology.datause.api;

import com.google.common.io.Resources;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.broadinstitute.dsde.consent.ontology.datause.models.OntologyTerm;
import org.broadinstitute.dsde.consent.ontology.datause.services.TextTranslationServiceImpl;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import org.apache.lucene.store.RAMDirectory;
import org.broadinstitute.dsde.consent.ontology.datause.ontologies.OntologyModel;

@Singleton
public class LuceneOntologyTermSearchAPI implements OntologyTermSearchAPI {

    private static final String FIELD_ID = "id";
    private static final String FIELD_LABEL = "label";
    private static final String FIELD_DEFINITION = "definition";
    private static final String FIELD_SYNONYM = "synonym";
    private static final String FIELD_DEFINITION_CLASS = "IAO_0000115";
    private IndexSearcher searcher;
    private Analyzer analyzer;
    private Map<String, OntologyTerm> nameToTerm;
    private OntologyModel ontologyList;

    public LuceneOntologyTermSearchAPI() {

    }

    private void initAPI(Directory indexDirectory, InputStream... streams) throws OWLOntologyCreationException, IOException {
        analyzer = new StandardAnalyzer(Version.LUCENE_4_9);

        try (IndexWriter indexWriter = new IndexWriter(indexDirectory,
                new IndexWriterConfig(Version.LUCENE_4_9, analyzer))) {

            nameToTerm = new HashMap<>();

            for (InputStream stream : streams) {
                OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
                OWLOntology ontology = manager.loadOntologyFromOntologyDocument(stream);
                HashMap<String, OWLAnnotationProperty> annotationProperties = new HashMap<>();

                ontology.getAnnotationPropertiesInSignature().stream().forEach((property) -> {
                    annotationProperties.put(property.getIRI().getFragment(), property);
                });

                OWLAnnotationProperty hasExactSynonym = annotationProperties.get("hasExactSynonym");
                //assert hasExactSynonym != null : "Need hasExactSynonym annotation property.";
                OWLAnnotationProperty label = annotationProperties.get("label");
                //assert label != null : "Need label annotation property";
                OWLAnnotationProperty def = annotationProperties.get(FIELD_DEFINITION_CLASS);
                //assert def != null : "Need def annotation property";
                OWLAnnotationProperty deprecated = annotationProperties.get("deprecated");
                //assert deprecated != null : "Need deprecated annotation property";

                for (OWLClass owlClass : ontology.getClassesInSignature()) {
                    OWLAnnotationValueVisitorEx<String> visitor = new OWLAnnotationValueVisitorEx<String>() {
                        @Override
                        public String visit(IRI iri) {
                            return iri.toString();
                        }

                        @Override
                        public String visit(OWLAnonymousIndividual owlAnonymousIndividual) {
                            return owlAnonymousIndividual.toStringID();
                        }

                        @Override
                        public String visit(OWLLiteral owlLiteral) {
                            return owlLiteral.getLiteral();
                        }
                    };

                    // Do not index deprecated classes.
                    if (!owlClass.getAnnotations(ontology, deprecated).isEmpty()) {
                        continue;
                    }

                    Document document = new Document();
                    owlClass.getAnnotations(ontology, hasExactSynonym).stream().forEach((synonyms) -> {
                        document.add(new TextField(FIELD_SYNONYM, synonyms.getValue().accept(visitor), Field.Store.YES));
                    });

                    Set<OWLAnnotation> labels = owlClass.getAnnotations(ontology, label);
                    assert labels.size() <= 1 : "Exactly 0 or 1 labels allowed per class";
                    if (labels.size() == 1) {
                        document.add(new TextField(FIELD_LABEL, labels.iterator().next().getValue().accept(visitor), Field.Store.YES));
                    }

                    // Do not index unless this term is a disease. We check to see if it has a field "id" and
                    // if that id matches the isDiseaseClass() method in TextTranslationServiceImpl
                    Set<OWLAnnotation> ids = owlClass.getAnnotations(ontology, annotationProperties.get("id"));
                    if (ids.size() == 1) {
                        String id = ids.iterator().next().getValue().accept(visitor);
                        if (!TextTranslationServiceImpl.isDiseaseClass(id)) {
                            continue;
                        }
                    }

                    Set<OWLAnnotation> defs = owlClass.getAnnotations(ontology, def);
                    assert defs.size() <= 1 : "Exactly 0 or 1 definitions allowed per class";
                    if (defs.size() == 1) {
                        document.add(new TextField(FIELD_DEFINITION, defs.iterator().next().getValue().accept(visitor), Field.Store.YES));
                    }

                    document.add(new TextField(FIELD_ID, owlClass.toStringID(), Field.Store.YES));
                    nameToTerm.put(document.get(FIELD_ID),
                            new OntologyTerm(
                                    document.get(FIELD_ID),
                                    document.get(FIELD_LABEL),
                                    document.get(FIELD_DEFINITION),
                                    document.getValues(FIELD_SYNONYM)));
                    indexWriter.addDocument(document);
                }
            }
        }

        this.searcher = new IndexSearcher(DirectoryReader.open(indexDirectory));
    }

    private Query queryFor(String field, String search, float boost) throws IOException {
        BooleanQuery prefixQuery = new BooleanQuery();
        BooleanQuery termQuery = new BooleanQuery();

        try (TokenStream stream = analyzer.tokenStream(field, search)) {
            CharTermAttribute cattr = stream.addAttribute(CharTermAttribute.class);

            stream.reset();
            while (stream.incrementToken()) {
                Term term = new Term(field, cattr.toString());
                prefixQuery.add(new PrefixQuery(term), BooleanClause.Occur.MUST);
                termQuery.add(new TermQuery(term), BooleanClause.Occur.MUST);
            }
            stream.end();
        }

        termQuery.setBoost(boost * 2.0f);
        prefixQuery.setBoost(boost);
        List<Query> queries = new ArrayList<>();
        queries.add(termQuery);
        queries.add(prefixQuery);
        return new DisjunctionMaxQuery(queries, 1.15f);
    }

    @Override
    public List<OntologyTerm> searchSimilarTerms(String search, int limit) throws IOException {
        List<Query> queries = new ArrayList<>();
        queries.add(queryFor(FIELD_ID, search, 2.0f));
        queries.add(queryFor(FIELD_LABEL, search, 1.5f));
        queries.add(queryFor(FIELD_SYNONYM, search, 0.5f));
        Query orQuery = new DisjunctionMaxQuery(queries, 1.15f);

        ArrayList<OntologyTerm> terms = new ArrayList<>(limit);
        for (ScoreDoc scoreDoc : searcher.search(orQuery, limit).scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            assert doc != null : "Document was null";

            terms.add(new OntologyTerm(
                    doc.get(FIELD_ID),
                    doc.get(FIELD_LABEL),
                    doc.get(FIELD_DEFINITION),
                    doc.getValues(FIELD_SYNONYM)));
        }
        return terms;
    }

    @Override
    public OntologyTerm findById(String id) throws IOException {
        return nameToTerm.get(id);
    }

    @Inject
    public void setOntologyList(OntologyModel ontologyList) {
        this.ontologyList = ontologyList;

        try {
            Collection<String> resources = ontologyList.getResources();
            Iterator<String> resourceIter = resources.iterator();
            InputStream[] ontologies = new InputStream[resources.size()];
            for (int i = 0; i < resources.size() && resourceIter.hasNext(); i++) {
                ontologies[i] = Resources.getResource(resourceIter.next()).openStream();
            }
            initAPI(new RAMDirectory(), ontologies);
        } catch (OWLOntologyCreationException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
