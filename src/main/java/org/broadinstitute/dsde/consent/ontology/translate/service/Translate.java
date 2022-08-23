package org.broadinstitute.dsde.consent.ontology.translate.service;

import com.github.jsonldjava.shaded.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.broadinstitute.dsde.consent.ontology.translate.DTO.RecommendationDto;
import org.broadinstitute.dsde.consent.ontology.translate.model.TermItem;
import org.parboiled.common.FileUtils;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Translate {

    public HashMap<String, RecommendationDto> paragraph(final String paragraph) throws Exception {
        HashMap<String, RecommendationDto> recommendations = new HashMap<>();

        List<TermItem> terms = loadJSONFromResources();

        for (TermItem term : terms) {
            final String title = term.getTitle();
            final String category = term.getCategory();
            final String url = term.getUrl();
            final String[] keywords = term.getKeywords();

            for (String keyword : keywords) {
                final boolean foundMatch = searchForKeyword(keyword, paragraph);

                if (foundMatch) {
                    RecommendationDto recommendation = new RecommendationDto(
                            title,
                            category
                    );
                    if (!recommendations.containsKey(url)) {
                        recommendations.put(url, recommendation);
                    }
                }
            }
        }

        return recommendations;
    }

    private static boolean searchForKeyword(final String keyword, final String targetText) {
        return StringUtils.containsIgnoreCase(targetText, keyword);
    }

    private static List<TermItem> loadJSONFromResources() throws Exception {
        try {
            final String searchTerms = FileUtils.readAllTextFromResource("search-terms.json");
            return new Gson().fromJson(
                    searchTerms,
                    new TypeToken<List<TermItem>>() {
                    }.getType()
            );
        } catch (Exception e) {
            throw new Exception("Error loading terms from json file: " + "search-terms.json", e);
        }
    }

}
