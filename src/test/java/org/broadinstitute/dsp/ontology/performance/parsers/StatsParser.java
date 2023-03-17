package org.broadinstitute.dsp.ontology.performance.parsers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.UUID;
import org.broadinstitute.dsp.ontology.performance.parsers.Stats.TestRun;

public class StatsParser {

  /**
   * This class processes the results of a successful gatling test run in CSV format specific to
   * our BQ Automation Test Results table:
   * testRunUUID          INTEGER     NULLABLE
   * errorClass           STRING      NULLABLE
   * stacktrace           STRING      NULLABLE
   * errorMessage         STRING      NULLABLE
   * skipped              BOOLEAN     NULLABLE
   * errorMessage                BOOLEAN     NULLABLE
   * timestamp            TIMESTAMP   NULLABLE
   * time                 FLOAT       NULLABLE
   * name                 STRING      NULLABLE
   * classname            STRING      NULLABLE
   * service              STRING      NULLABLE
   * failure              BOOLEAN     NULLABLE
   * serviceTestRunUUID   STRING      NULLABLE
   * env                  STRING      NULLABLE
   * @param args CLI Arguments
   */
  public static void main(String[] args) {
    // Look for the stats.json file
    InputStream is = StatsParser.class.getClassLoader().getResourceAsStream("stats.json");
    if (Objects.nonNull(is)) {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
      Stats stats = gson.fromJson(reader, Stats.class);
      LinkedHashMap<String, TestRun> contents = stats.getContents();
      String uuid = UUID.randomUUID().toString();
      Timestamp timestamp = Timestamp.from(Instant.now());
      contents.values().forEach(o -> System.out.println(o.toString(uuid, timestamp)));
    } else {
      throw new RuntimeException("Unable to find a 'stats.json' file in src/test/resources.");
    }
  }
}
