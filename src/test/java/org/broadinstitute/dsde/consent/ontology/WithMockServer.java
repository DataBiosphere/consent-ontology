package org.broadinstitute.dsde.consent.ontology;

import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.utility.DockerImageName;

public interface WithMockServer {

  Logger log = Utils.getLogger(WithMockServer.class);
  DockerImageName IMAGE = DockerImageName.parse(
      "mockserver/mockserver:mockserver-" + getMockServerVersion());

  default void stop(MockServerContainer container) {
    if (Objects.nonNull(container) && container.isRunning()) {
      container.stop();
    }
  }

  static String getMockServerVersion() {
    String version = "5.11.2";
    String versionPropName = "mockserver.version";
    try (InputStream is = WithMockServer.class.getResourceAsStream("/mvn.properties")) {
      Properties p = new Properties();
      p.load(is);
      if (StringUtils.isNotEmpty(p.getProperty(versionPropName))) {
        version = p.getProperty(versionPropName);
      } else {
        log.warn(versionPropName + " is not configured correctly, defaulting to: " + version);
      }
    } catch (Exception e) {
      log.error(e.getMessage());
      log.warn("Defaulting to: " + version);
    }
    return version;
  }

}
