package org.broadinstitute.dsde.consent.ontology.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
      JsonElement shortHash = jsonObject.get("git.commit.id.abbrev");
      JsonElement buildVersion = jsonObject.get("git.build.version");
      if (Objects.nonNull(shortHash) && Objects.nonNull(buildVersion)) {
        this.hash = Optional.ofNullable(shortHash.getAsString()).orElse("error");
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
