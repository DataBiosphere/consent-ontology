package org.broadinstitute.dsde.consent.ontology.translate.utils;

import java.util.regex.Pattern;

public class Search {
    public static boolean searchForKeyword(String keyword, String targetText) {
        if (keyword == null || keyword.isEmpty()) {
            return false;
        }

        return Pattern.compile(keyword, Pattern.CASE_INSENSITIVE).matcher(targetText).find();
    }
}
