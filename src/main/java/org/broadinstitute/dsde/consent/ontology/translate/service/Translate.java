package org.broadinstitute.dsde.consent.ontology.translate.service;

import org.broadinstitute.dsde.consent.ontology.Utils;
import org.broadinstitute.dsde.consent.ontology.translate.DTO.RecommendationDTO;
import org.broadinstitute.dsde.consent.ontology.translate.model.TermItem;
import org.broadinstitute.dsde.consent.ontology.translate.utils.LoadUtils;
import org.broadinstitute.dsde.consent.ontology.translate.utils.SearchUtils;

import java.util.HashMap;
import java.util.List;

public class Translate {

    String jsonFilePath;

    public Translate(String jsonFilePath) {
        this.jsonFilePath = jsonFilePath;
    }

    public HashMap<String, RecommendationDTO> paragraph(final String paragraph) throws Exception {
        HashMap<String, RecommendationDTO> recommendations = new HashMap<>();

        if (Utils.isNullOrEmpty(paragraph) || Utils.isNullOrEmpty(jsonFilePath)) {
            return recommendations;
        }

        List<TermItem> terms = LoadUtils.termsFromLocalResourcesJSON(jsonFilePath);

        for (TermItem term : terms) {
            final String title = term.getTitle();
            final String category = term.getCategory();
            final String url = term.getUrl();
            final String[] keywords = term.getKeywords();

            for (String keyword : keywords) {
                final boolean foundMatch = SearchUtils.searchForKeyword(keyword, paragraph);

                if (foundMatch) {
                    RecommendationDTO recommendation = new RecommendationDTO();
                    recommendation.setTitle(title);
                    recommendation.setCategory(category);
                    if (!recommendations.containsKey(url)) {
                        recommendations.put(url, recommendation);
                    }
                }
            }
        }

        return recommendations;
    }

}
