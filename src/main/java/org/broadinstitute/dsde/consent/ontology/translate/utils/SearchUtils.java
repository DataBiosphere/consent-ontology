package org.broadinstitute.dsde.consent.ontology.translate.utils;

import org.broadinstitute.dsde.consent.ontology.Utils;

import java.util.regex.Pattern;

public class SearchUtils {
    public static boolean searchForKeyword(final String keyword, final String targetText) {
        if (Utils.isNullOrEmpty(keyword) || Utils.isNullOrEmpty(targetText)) {
            return false;
        }

        return Pattern
                .compile(keyword, Pattern.CASE_INSENSITIVE)
                .matcher(targetText)
                .find();
    }
}
