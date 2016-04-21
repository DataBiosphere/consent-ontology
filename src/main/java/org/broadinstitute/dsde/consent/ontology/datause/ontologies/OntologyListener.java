package org.broadinstitute.dsde.consent.ontology.datause.ontologies;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelChangedListener;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import java.util.List;

public class OntologyListener implements ModelChangedListener {

    @Override
    public void addedStatement(Statement stmnt) {
        System.out.println("addedStatement(" + stmnt.asTriple().toString() + ")");
    }

    @Override
    public void addedStatements(Statement[] stmnts) {
        System.out.println("addedStatements(" + stmnts.toString() + ")");
    }

    @Override
    public void addedStatements(List<Statement> list) {
       list.stream().forEach(s -> System.out.println("addedStatements(" + s + ")"));
    }

    @Override
    public void addedStatements(StmtIterator si) {
       si.toList().stream().forEach(s -> System.out.println("addedStatements(" + s + ")"));
    }

    @Override
    public void addedStatements(Model model) {
        System.out.println("addedStatements(" + model.toString()+ ")");
    }

    @Override
    public void removedStatement(Statement stmnt) {
        System.out.println("removedStatement(" + stmnt.asTriple().toString() + ")");
    }

    @Override
    public void removedStatements(Statement[] stmnts) {
        System.out.println("removedStatements(" + stmnts.toString() + ")");
    }

    @Override
    public void removedStatements(List<Statement> list) {
       list.stream().forEach(s -> System.out.println("removedStatements(" + s + ")"));
    }

    @Override
    public void removedStatements(StmtIterator si) {
       si.toList().stream().forEach(s -> System.out.println("removedStatements(" + s + ")"));
    }

    @Override
    public void removedStatements(Model model) {
        System.out.println("removedStatements(" + model.toString()+ ")");
    }

    @Override
    public void notifyEvent(Model model, Object o) {
        System.out.println("notifyEvent(" + model.toString() + " - " + o.toString() + ")");
    }
    
}
