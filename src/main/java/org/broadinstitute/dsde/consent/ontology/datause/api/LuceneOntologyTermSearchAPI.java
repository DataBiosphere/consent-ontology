package org.broadinstitute.dsde.consent.ontology.datause.api;

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
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.broadinstitute.dsde.consent.ontology.datause.models.OntologyTerm;
import org.broadinstitute.dsde.consent.ontology.service.StoreOntologyService;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class LuceneOntologyTermSearchAPI implements OntologyTermSearchAPI {

    private static final String FIELD_ID = "id";
    private static final String FIELD_COMMENT = "comment";
    private static final String FIELD_LABEL = "label";
    private static final String FIELD_DEFINITION = "definition";
    private static final String FIELD_SYNONYM = "synonym";
    private static final String FIELD_DEFINITION_CLASS = "IAO_0000115";
    private final Logger log = LoggerFactory.getLogger(LuceneOntologyTermSearchAPI.class);
    private IndexSearcher searcher;
    private Analyzer analyzer;
    private Map<String, OntologyTerm> nameToTerm;
    private StoreOntologyService storeOntologyService;

    @Inject
    public LuceneOntologyTermSearchAPI(StoreOntologyService storeOntologyService) {
        this.storeOntologyService = storeOntologyService;
        try {
            initAPI();
        } catch (OWLOntologyCreationException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initAPI() throws OWLOntologyCreationException, IOException {

        Collection<String> resouces = storeOntologyService.retrieveConfigurationKeys();
        Directory indexDirectory = new RAMDirectory();

        analyzer = new StandardAnalyzer(Version.LUCENE_4_9);

        try (IndexWriter indexWriter = new IndexWriter(indexDirectory,
                new IndexWriterConfig(Version.LUCENE_4_9, analyzer))) {
            nameToTerm = new HashMap<>();

            for (String resource: resouces)  {
                try(InputStream stream = new URL(resource).openStream()) {
                    OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
                    OWLOntology ontology = manager.loadOntologyFromOntologyDocument(stream);
                    HashMap<String, OWLAnnotationProperty> annotationProperties = new HashMap<>();

                    ontology.annotationPropertiesInSignature().forEach((property) -> {
                        annotationProperties.put(property.getIRI().getRemainder().get (), property);
                    });

                    OWLAnnotationProperty hasExactSynonym = annotationProperties.get("hasExactSynonym");
                    OWLAnnotationProperty label = annotationProperties.get("label");
                    OWLAnnotationProperty comment = annotationProperties.get("comment");
                    OWLAnnotationProperty def = annotationProperties.get(FIELD_DEFINITION_CLASS);
                    OWLAnnotationProperty deprecated = annotationProperties.get("deprecated");

                    ontology.classesInSignature().forEach(owlClass -> {
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
                        if (!(deprecated != null && EntitySearcher.getAnnotations(owlClass, ontology, deprecated).count() > 0)) {
                            Document document = new Document();
                            if(hasExactSynonym != null){
                                EntitySearcher.getAnnotations(owlClass, ontology, hasExactSynonym).forEach((synonyms) -> {
                                    document.add(new TextField(FIELD_SYNONYM, synonyms.getValue().accept(visitor), Field.Store.YES));
                                });
                            }
                            if(label != null){
                                Stream<OWLAnnotation> labels = EntitySearcher.getAnnotations(owlClass, ontology, label);
                                ArrayList<OWLAnnotation> labelsList = labels.collect(Collectors.toCollection(ArrayList::new));
                                assert labelsList.size() <= 1 : "Exactly 0 or 1 labels allowed per class";
                                if (labelsList.size() == 1) {
                                    document.add(new TextField(FIELD_LABEL, labelsList.stream().iterator().next().getValue().accept(visitor), Field.Store.YES));
                                }
                            }
                            if(comment != null){
                                Stream<OWLAnnotation> comments = EntitySearcher.getAnnotations(owlClass, ontology, comment);
                                ArrayList<OWLAnnotation> commentsList = comments.collect(Collectors.toCollection(ArrayList::new));

                                assert commentsList.size() <= 1 : "Exactly 0 or 1 comments allowed per class";
                                if (commentsList.size() == 1) {
                                    document.add(new TextField(FIELD_COMMENT, commentsList.stream().iterator().next().getValue().accept(visitor), Field.Store.YES));
                                }
                            }
                            if(def != null){
                                Stream<OWLAnnotation> defs = EntitySearcher.getAnnotations(owlClass, ontology, def);
                                ArrayList<OWLAnnotation> defsList = defs.collect(Collectors.toCollection(ArrayList::new));
                                assert defsList.size() <= 1 : "Exactly 0 or 1 definitions allowed per class";
                                if (defsList.size() == 1) {
                                    document.add(new TextField(FIELD_DEFINITION, defsList.stream().iterator().next().getValue().accept(visitor), Field.Store.YES));
                                }
                            }
                            document.add(new TextField(FIELD_ID, owlClass.toStringID(), Field.Store.YES));
                            nameToTerm.put(document.get(FIELD_ID),
                                new OntologyTerm(
                                    document.get(FIELD_ID),
                                    document.get(FIELD_COMMENT),
                                    document.get(FIELD_LABEL),
                                    document.get(FIELD_DEFINITION),
                                    document.getValues(FIELD_SYNONYM)));


                            try {
                                indexWriter.addDocument(document);
                            }catch(IOException e){
                                throw new RuntimeException(e.getMessage());
                            }
                        }
                    });
                } catch (MalformedURLException e) {
                    log.error("Unable to parse string to url: " + e.getMessage());
                    throw new RuntimeException(e);
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
        queries.add(queryFor(FIELD_COMMENT, search, 1.5f));
        queries.add(queryFor(FIELD_LABEL, search, 1.5f));
        queries.add(queryFor(FIELD_SYNONYM, search, 0.5f));
        Query orQuery = new DisjunctionMaxQuery(queries, 1.15f);

        ArrayList<OntologyTerm> terms = new ArrayList<>(limit);
        for (ScoreDoc scoreDoc : searcher.search(orQuery, limit).scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            assert doc != null : "Document was null";

            terms.add(new OntologyTerm(
                    doc.get(FIELD_ID),
                    doc.get(FIELD_COMMENT),
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

}
