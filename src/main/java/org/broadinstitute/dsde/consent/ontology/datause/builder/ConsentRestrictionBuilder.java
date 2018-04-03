package org.broadinstitute.dsde.consent.ontology.datause.builder;

import org.broadinstitute.dsde.consent.ontology.datause.models.*;
import org.broadinstitute.dsde.consent.ontology.resources.model.DataUse;

import java.util.ArrayList;
import java.util.List;

import static org.broadinstitute.dsde.consent.ontology.datause.builder.UseRestrictionBuilderSupport.*;

/**
 * Apply consent-specific business rules when generating use restrictions
 *
 * There are several consent-related Data Use conditions, but the only ones
 * that translate to a Structured Use Restriction are:
 *  - General Use
 *  - Disease Restrictions
 *  - Population Restrictions (requires manual review)
 *  - Commercial Use
 *  - Gender
 *  - Pediatric
 *  - Methods Research
 *  - Control Set
 *
 *  Other consent-related conditions include, but are currently not translated into an SDUR:
 *  - HMB - Health/Medical/Biomedical Research
 *  - POA - Population Origins Ancestry
 *  - Population Structure
 *  - Date Restriction (requires manual review)
 *  - Aggregate Research (requires manual review)
 *  - Recontacting Data Subjects
 *  - Recontacting May
 *  - Recontacting Must
 *  - Genotypic Phenotypic data
 *  - Cloud Storage
 *  - Other and Other Restrictions (requires manual review)
 *  - Ethics Approval Required
 *  - Geographical Restrictions
 *
 *  Future expansions to DUOS could make use of these fields
 *
 */
public class ConsentRestrictionBuilder implements UseRestrictionBuilder {

    public UseRestriction buildUseRestriction(DataUse dataUse) {
        List<UseRestriction> categoryRestrictions = new ArrayList<>();
        UseRestriction restriction;

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

        // See GAWB-3210: In the case where GRU is sent in combination with other sub-conditions,
        // ignore GRU and apply those other restrictions instead.
        // TODO: Work with Moran to see if we need to check for METHODS_RESEARCH/CONTROL here.
        if ((isPresent(dataUse.getGeneralUse()) && dataUse.getGeneralUse())
                && categoryRestrictions.isEmpty()) {
            return new Everything();
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
        if (isPresent(dataUse.getControlSetOption()) && dataUse.getControlSetOption().equalsIgnoreCase("Yes")) {
            restriction = new Or(
                restriction,
                new And(restriction, new Named(CONTROL))
            );
        }

        return restriction;
    }

}
