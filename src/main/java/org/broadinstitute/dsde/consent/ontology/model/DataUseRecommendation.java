package org.broadinstitute.dsde.consent.ontology.model;

import java.util.Map;

public record DataUseRecommendation(Map<String, Recommendation> recommendations) {}
