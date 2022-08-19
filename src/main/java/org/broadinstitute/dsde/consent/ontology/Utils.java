package org.broadinstitute.dsde.consent.ontology;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {

    public static Logger getLogger(Class clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    public static boolean isNullOrEmpty(final String s) {
        return s == null || s.isEmpty();
    }

}
