package org.broadinstitute.dsde.consent.ontology.util;

import org.broadinstitute.dsde.consent.ontology.service.AutocompleteService;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public final class DataUseUtil {

  public void setAutocompleteService(AutocompleteService autocompleteService) {
    this.autocompleteService = autocompleteService;
  }

  private AutocompleteService autocompleteService;

  // Get a map of disease term to list of parent term ids (which also includes disease term id)
  public Map<String, List<String>> generatePurposeDiseaseIdMap(List<String> diseaseRestrictions) throws IOException {
    Map<String, List<String>> map = new HashMap<>();
    for (String r : diseaseRestrictions) {
      map.put(r, getParentTermIds(r));
    }
    return map;
  }

  // Get a list of term ids that represent a disease term + all parent ids
  public List<String> getParentTermIds(String purposeDiseaseId) throws IOException {
    List<String> purposeTermIdList = autocompleteService.lookupById(purposeDiseaseId)
        .stream()
        .filter(Objects::nonNull)
        .filter(t -> Objects.nonNull(t.getParents()) && !t.getParents().isEmpty())
        .flatMap(t -> t.parents.stream())
        .map(p -> p.id)
        .collect(Collectors.toList());
    purposeTermIdList.add(purposeDiseaseId);
    return purposeTermIdList;
  }

  public static List<String> sanitize(List<String> strings) {
    if (strings != null) {
      for(int i = 0; i < strings.size(); ++i) {
        strings.set(i, strings.get(i) == null ? null : (strings.get(i)).trim());
      }
    }

    return strings;
  }

}
