package org.broadinstitute.dsp.ontology.performance.results;

import com.google.gson.JsonObject;
import java.util.LinkedHashMap;

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

  public class TestRun {
    String name;
    RunStats stats;

  }

  public class RunStats {
    String name;
    OKCounts numberOfRequests;
    OKCounts minResponseTime;
    OKCounts maxResponseTime;
    OKCounts meanResponseTime;
    OKCounts standardDeviation;
    OKCounts percentiles1;
    OKCounts percentiles2;
    OKCounts percentiles3;
    OKCounts percentiles4;
    RunCountGroup group1;
    RunCountGroup group2;
    RunCountGroup group3;
    RunCountGroup group4;
    OKCounts meanNumberOfRequestsPerSecond;
  }

  public class OKCounts {
    float total;
    long ok;
    long ko;
  }

  public class RunCountGroup {
    String name;
    int count;
    int percentage;
  }
}


