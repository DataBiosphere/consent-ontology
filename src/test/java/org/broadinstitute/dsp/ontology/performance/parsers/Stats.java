package org.broadinstitute.dsp.ontology.performance.parsers;

import com.github.cliftonlabs.json_simple.JsonObject;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Stats {
  String type;
  String name;
  String path;
  String pathFormatted;
  JsonObject stats;
  LinkedHashMap<String, TestRun> contents;

  public String getType() {
    return type;
  }

  public String getName() {
    return name;
  }

  public String getPath() {
    return path;
  }

  public String getPathFormatted() {
    return pathFormatted;
  }

  public JsonObject getStats() {
    return stats;
  }

  public LinkedHashMap<String, TestRun> getContents() {
    return contents;
  }

  /**
   * Output:
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
   */
  public class TestRun {
    String name;
    RunStats stats;
    public String toString(String uuid, Timestamp timestamp) {
      String errorClass = this.stats.numberOfRequests.ko > 0 ? this.name : "";
      String failure = this.stats.numberOfRequests.ko > 0 ? Boolean.TRUE.toString() : Boolean.FALSE.toString();
      return String.join(", ",
        uuid,
        errorClass,
        "stackTrace",
        "errorMessage",
        "skipped",
        "errorMessage",
        timestamp.toString(),
        "time",
        this.name,
        this.name,
        "duos",
        failure.toLowerCase(),
        "",
        "dev");
    }
  }

  public class RunStats {
    String name;
    OKCountsInt numberOfRequests;
    OKCountsInt minResponseTime;
    OKCountsInt maxResponseTime;
    OKCountsInt meanResponseTime;
    OKCountsInt standardDeviation;
    OKCountsInt percentiles1;
    OKCountsInt percentiles2;
    OKCountsInt percentiles3;
    OKCountsInt percentiles4;
    RunCountGroup group1;
    RunCountGroup group2;
    RunCountGroup group3;
    RunCountGroup group4;
    OKCountsFloat meanNumberOfRequestsPerSecond;
  }

  public class OKCountsInt {
    int total;
    int ok;
    int ko;
  }

  public class OKCountsFloat {
    float total;
    float ok;
    float ko;
  }

  public class RunCountGroup {
    String name;
    int count;
    int percentage;
  }
}