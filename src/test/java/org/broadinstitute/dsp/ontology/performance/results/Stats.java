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


