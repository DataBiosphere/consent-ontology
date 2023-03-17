package org.broadinstitute.dsp.ontology.performance.parsers;

import com.google.common.base.Strings;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class LogParser {

  /**
   * This class processes the results of a successful gatling test run in CSV format specific to
   * our BQ Automation Test Results table:
   * testRunUUID          INTEGER     NULLABLE
   * errorClass           STRING      NULLABLE
   * stacktrace           STRING      NULLABLE
   * errorMessage         STRING      NULLABLE
   * skipped              BOOLEAN     NULLABLE
   * error                BOOLEAN     NULLABLE
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
  public static void main(String[] args) throws Exception {
    // Look for the simulation.log file
    URL path = StatsParser.class.getClassLoader().getResource("simulation.log");
    assert path != null;
    List<String> lines = Files.readAllLines(Paths.get(path.toURI()));
    if (!lines.isEmpty()) {
      List<LogEntry> logEntries = lines
          .stream()
          .filter(l -> l.startsWith("REQUEST"))
          .map(l -> {
            String[] fields = l.split("\\t");
            return new LogEntry(
              fields[2].trim(),
              Long.valueOf(fields[3].trim()),
              Long.valueOf(fields[4].trim()),
              fields[5].trim(),
              fields[6].trim());
          })
          .toList();
      String uuid = UUID.randomUUID().toString();
      Timestamp timestamp = Timestamp.from(Instant.now());

      System.out.println(logEntries);

      logEntries.forEach(e -> {
        String errorClass = Strings.isNullOrEmpty(e.errorMessage()) ? "" : e.title();
        String failure = Strings.isNullOrEmpty(e.errorMessage()) ? "false" : "true";
        String output = String.join(", ",
          uuid,
          errorClass,
          "",
          "\"" + e.errorMessage() + "\"",
          "false",
          failure,
          timestamp.toString(),
          String.valueOf(e.finish() - e.start()),
          e.title(),
          e.title(),
          "duos",
          failure,
          "",
          "dev");
        System.out.println(output);
      });
    }
  }
}
