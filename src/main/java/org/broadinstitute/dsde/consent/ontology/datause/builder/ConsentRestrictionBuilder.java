package org.broadinstitute.dsde.consent.ontology.datause.builder;

import org.broadinstitute.dsde.consent.ontology.datause.models.*;
import org.broadinstitute.dsde.consent.ontology.resources.model.DataUse;

import java.util.ArrayList;
import java.util.List;

import static org.broadinstitute.dsde.consent.ontology.datause.builder.UseRestrictionBuilderSupport.*;

/**
 * Apply consent-specific business rules when generating use restrictions
 */
public class ConsentRestrictionBuilder implements UseRestrictionBuilder {

    public UseRestriction buildUseRestriction(DataUse dataUse) {
        List<UseRestriction> categoryRestrictions = new ArrayList<>();
        UseRestriction restriction;

        if (isPresent(dataUse.getGeneralUse()) && dataUse.getGeneralUse()) {
            return new Everything();
        }

        if (!dataUse.getDiseaseRestrictions().isEmpty()) {
            categoryRestrictions.add(
                buildORRestrictionFromClasses(dataUse.getDiseaseRestrictions())
            );
        }

        if (!dataUse.getPopulationRestrictions().isEmpty()) {
            categoryRestrictions.add(
                buildORRestrictionFromClasses(dataUse.getPopulationRestrictions())
            );
        }

        if (getOrElseFalse(dataUse.getCommercialUse())) {
            categoryRestrictions.add(new Not(new Named(NON_PROFIT)));
        }

        if (isPresent(dataUse.getGender()) &&
            getOrElseFalse(dataUse.getPediatric())) {
            if (dataUse.getGender().equalsIgnoreCase("male")) {
                categoryRestrictions.add(new Named(BOYS));
            }
            else if (dataUse.getGender().equalsIgnoreCase("female")) {
                categoryRestrictions.add(new Named(GIRLS));
            }
        } else if (isPresent(dataUse.getGender())) {
            if (dataUse.getGender().equalsIgnoreCase("male")) {
                categoryRestrictions.add(new Named(MALE));
            }
            else if (dataUse.getGender().equalsIgnoreCase("female")) {
                categoryRestrictions.add(new Named(FEMALE));
            }
        } else if (getOrElseFalse(dataUse.getPediatric())) {
            categoryRestrictions.add(new Named(PEDIATRIC));
        }

        // This builds up the basic restriction before the MR and CS are applied.
        if (categoryRestrictions.isEmpty()) {
            restriction = new Everything();
        } else if (categoryRestrictions.size() == 1) {
            restriction = categoryRestrictions.get(0);
        } else {
            restriction = new And(categoryRestrictions.toArray(new UseRestriction[categoryRestrictions.size()]));
        }

        // Apply Methods Research Logic
        if (getOrElseTrue(dataUse.getMethodsResearch())) {
            restriction = new Or(new Named(METHODS_RESEARCH), restriction);
        } else {
            restriction = new Or(
                new And(restriction, new Not(new Named(METHODS_RESEARCH))),
                restriction
            );
        }

        // Apply Control Set Logic
        if (isPresent(dataUse.getControlSetOption()) && dataUse.getControlSetOption().equalsIgnoreCase("No")) {
            restriction = new Or(
                restriction,
                new And(restriction, new Named(CONTROL))
            );
        }

        return restriction;
    }

}
