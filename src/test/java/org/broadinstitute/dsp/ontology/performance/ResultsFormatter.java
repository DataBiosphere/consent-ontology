package org.broadinstitute.dsp.ontology.performance;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Objects;
import org.broadinstitute.dsp.ontology.performance.Stats.TestRun;

public class ResultsFormatter {

  /**
   * This class processes the results of a successful gatling test run. Future work will
   * upload results to BQ to build up a history of timing results over time
   *
   * @param args CLI Arguments
   */
  public static void main(String[] args) {
    // Look for the stats.json file
    InputStream is = ResultsFormatter.class.getClassLoader().getResourceAsStream("stats.json");
    assert (Objects.nonNull(is));
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
    Stats stats = gson.fromJson(reader, Stats.class);
    LinkedHashMap<String, TestRun> contents = stats.getContents();
    contents.values().forEach(o -> System.out.println(gson.toJson(o)));
  }

}
