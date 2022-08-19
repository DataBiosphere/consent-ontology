package org.broadinstitute.dsde.consent.ontology.translate.service;

import com.github.jsonldjava.shaded.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.broadinstitute.dsde.consent.ontology.translate.DTO.RecommendationDTO;
import org.broadinstitute.dsde.consent.ontology.translate.model.TermItem;
import org.parboiled.common.FileUtils;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class TranslateParagraphService {

    String jsonFilePath;

    public TranslateParagraphService(String jsonFilePath) {
        this.jsonFilePath = jsonFilePath;
    }

    public HashMap<String, RecommendationDTO> translateParagraph(String paragraph) throws Exception {
        List<TermItem> terms = loadTermsFromJson();
        HashMap<String, RecommendationDTO> recommendations = new HashMap<>();

        for (TermItem term : terms) {
            String title = term.getTitle();
            String category = term.getCategory();
            String url = term.getUrl();
            String[] keywords = term.getKeywords();

            for (String keyword :  keywords) {
                boolean foundMatch = searchForKeyword(keyword, paragraph);

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

    private boolean searchForKeyword(String keyword, String paragraph) {
        if (keyword == null || keyword.isEmpty()) {
            return false;
        }

        return Pattern.compile(keyword, Pattern.CASE_INSENSITIVE).matcher(paragraph).find();
    }

    private List<TermItem> loadTermsFromJson() throws Exception {
        try {
            String searchTerms = FileUtils.readAllTextFromResource(this.jsonFilePath);
            return new Gson().fromJson(searchTerms, new TypeToken<List<TermItem>>() {
            }.getType());
        } catch (Exception e) {
            throw new Exception("Error loading terms from json file: " + jsonFilePath);
        }
    }
}
