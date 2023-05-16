package org.broadinstitute.dsde.consent.ontology.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.Objects;
import java.util.Optional;

public class Version {

  String hash;
  String version;

  public Version(String props) {
    if (props == null) {
      this.hash = "error";
      this.version = "error";
    } else {
      JsonObject jsonObject = new Gson().fromJson(props, JsonObject.class);
      String longHash = Optional
          .ofNullable(jsonObject.get("git.commit.id"))
          .orElse(new JsonPrimitive("error"))
          .getAsString();
      String shortHash = longHash.substring(0, Math.min(longHash.length(), 12));
      JsonElement buildVersion = jsonObject.get("git.build.version");
      if (Objects.nonNull(buildVersion)) {
        this.hash = shortHash;
        this.version = Optional.ofNullable(buildVersion.getAsString()).orElse("error");
      }
    }
  }

  public String getHash() {
    return hash;
  }

  public String getVersion() {
    return version;
  }
}
