package org.broadinstitute.dsde.consent.ontology.translate.DTO;

public class RecommendationDto {
    private final String title;
    private final String category;

    public RecommendationDto(String title, String category) {
        this.title = title;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

}
