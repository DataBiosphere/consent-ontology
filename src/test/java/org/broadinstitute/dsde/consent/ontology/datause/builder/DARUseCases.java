package org.broadinstitute.dsde.consent.ontology.datause.builder;

import org.broadinstitute.dsde.consent.ontology.datause.models.And;
import org.broadinstitute.dsde.consent.ontology.datause.models.Named;
import org.broadinstitute.dsde.consent.ontology.datause.models.Not;
import org.broadinstitute.dsde.consent.ontology.datause.models.UseRestriction;

public class DARUseCases {

    public static UseRestriction darDefaultMRPA = new And(
        new And(
            new Named(UseRestrictionBuilderSupport.METHODS_RESEARCH),
            new Not(new Named(UseRestrictionBuilderSupport.POPULATION_STRUCTURE)),
            new Not(new Named(UseRestrictionBuilderSupport.CONTROL))
        ),
        new Named(ConsentUseCases.CANCER),
        new Named(UseRestrictionBuilderSupport.NON_PROFIT)
    );

    public static UseRestriction darDefaultMRPB = new And(
        new And(
            new Not(new Named(UseRestrictionBuilderSupport.METHODS_RESEARCH)),
            new Not(new Named(UseRestrictionBuilderSupport.POPULATION_STRUCTURE)),
            new Not(new Named(UseRestrictionBuilderSupport.CONTROL))
        ),
        new Named(ConsentUseCases.CANCER),
        new Named(UseRestrictionBuilderSupport.NON_PROFIT));

    public static UseRestriction darDefaultMRPC = new And(
        new And(
            new Named(UseRestrictionBuilderSupport.METHODS_RESEARCH),
            new Not(new Named(UseRestrictionBuilderSupport.POPULATION_STRUCTURE)),
            new Not(new Named(UseRestrictionBuilderSupport.CONTROL))
        ),
        new Named(UseRestrictionBuilderSupport.NON_PROFIT));

    public static UseRestriction darDefaultCSA = new And(
        new And(
            new Not(new Named(UseRestrictionBuilderSupport.METHODS_RESEARCH)),
            new Not(new Named(UseRestrictionBuilderSupport.POPULATION_STRUCTURE)),
            new Named(UseRestrictionBuilderSupport.CONTROL)
        ),
        new Named(ConsentUseCases.CANCER),
        new Named(UseRestrictionBuilderSupport.NON_PROFIT)

    );

    public static UseRestriction darDefaultCSB = new And(
        new And(
            new Not(new Named(UseRestrictionBuilderSupport.METHODS_RESEARCH)),
            new Not(new Named(UseRestrictionBuilderSupport.POPULATION_STRUCTURE)),
            new Not(new Named(UseRestrictionBuilderSupport.CONTROL))
        ),
        new Named(ConsentUseCases.CANCER),
        new Named(UseRestrictionBuilderSupport.NON_PROFIT)
    );
    
    public static UseRestriction darDefaultCSC = new And(
        new And(
            new Not(new Named(UseRestrictionBuilderSupport.METHODS_RESEARCH)),
            new Not(new Named(UseRestrictionBuilderSupport.POPULATION_STRUCTURE)),
            new Named(UseRestrictionBuilderSupport.CONTROL)
        ),
        new Named(UseRestrictionBuilderSupport.NON_PROFIT)
    );
}
