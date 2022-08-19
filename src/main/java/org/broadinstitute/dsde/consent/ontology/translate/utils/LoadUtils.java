package org.broadinstitute.dsde.consent.ontology.translate.utils;

import com.github.jsonldjava.shaded.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.broadinstitute.dsde.consent.ontology.translate.model.TermItem;
import org.parboiled.common.FileUtils;

import java.util.List;

public class LoadFromJSON {
    public static List<TermItem> loadTermsFromJson(String jsonFilePath) throws Exception {
        try {
            String searchTerms = FileUtils.readAllTextFromResource(jsonFilePath);
            return new Gson().fromJson(searchTerms, new TypeToken<List<TermItem>>() {
            }.getType());
        } catch (Exception e) {
            throw new Exception("Error loading terms from json file: " + jsonFilePath, e);
        }
    }
}
