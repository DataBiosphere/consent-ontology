package org.broadinstitute.dsde.consent.ontology.datause.ontologies;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelChangedListener;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import java.util.List;
import org.apache.log4j.Logger;

public class OntologyListener implements ModelChangedListener {

    private static final Logger LOG = Logger.getLogger(OntologyListener.class);

    @Override
    public void addedStatement(Statement stmnt) {
        LOG.debug("addedStatement(" + stmnt.asTriple().toString() + ")");
    }

    @Override
    public void addedStatements(Statement[] stmnts) {
        LOG.debug("addedStatements(" + stmnts.toString() + ")");
    }

    @Override
    public void addedStatements(List<Statement> list) {
        list.stream().forEach(s -> LOG.debug("addedStatements(" + s + ")"));
    }

    @Override
    public void addedStatements(StmtIterator si) {
        si.toList().stream().forEach(s -> LOG.debug("addedStatements(" + s + ")"));
    }

    @Override
    public void addedStatements(Model model) {
        LOG.debug("addedStatements(" + model.toString() + ")");
    }

    @Override
    public void removedStatement(Statement stmnt) {
        LOG.debug("removedStatement(" + stmnt.asTriple().toString() + ")");
    }

    @Override
    public void removedStatements(Statement[] stmnts) {
        LOG.debug("removedStatements(" + stmnts.toString() + ")");
    }

    @Override
    public void removedStatements(List<Statement> list) {
        list.stream().forEach(s -> LOG.debug("removedStatements(" + s + ")"));
    }

    @Override
    public void removedStatements(StmtIterator si) {
        si.toList().stream().forEach(s -> LOG.debug("removedStatements(" + s + ")"));
    }

    @Override
    public void removedStatements(Model model) {
        LOG.debug("removedStatements(" + model.toString() + ")");
    }

    @Override
    public void notifyEvent(Model model, Object o) {
        LOG.debug("notifyEvent(" + model.toString() + " - " + o.toString() + ")");
    }

}
