package performance;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Objects;
import performance.Stats.TestRun;

public class ResultsFormatter {

  /**
   * This class processes the results of a successful gatling test run. Future work will
   *
   * @param args CLI Arguments
   * @throws Exception The exception
   */
  public static void main(String[] args) throws Exception {
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
