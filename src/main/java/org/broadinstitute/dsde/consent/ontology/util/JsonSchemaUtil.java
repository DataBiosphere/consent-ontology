package org.broadinstitute.dsde.consent.ontology.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import jakarta.ws.rs.BadRequestException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import org.apache.commons.io.IOUtils;
import org.broadinstitute.dsde.consent.ontology.OntologyLogger;

public class JsonSchemaUtil implements OntologyLogger {

  private final LoadingCache<String, String> cache;
  private final String dataUseSchemaV3 = "/data-use-v3.json";

  public JsonSchemaUtil() {
    CacheLoader<String, String> loader = new CacheLoader<>() {
      @Override
      public String load(String key) throws Exception {
        return IOUtils.resourceToString(key, Charset.defaultCharset());
      }
    };
    this.cache = CacheBuilder.newBuilder().build(loader);
  }

  public String getDataUseSchemaV3() {
    try {
      return cache.get(dataUseSchemaV3);
    } catch (ExecutionException ee) {
      logError("Unable to load the data use schema V3: " + ee.getMessage());
      return null;
    }
  }

  /**
   * Loads a Schema populated from the data use V3 schema
   *
   * @return Schema The Schema
   * @throws ExecutionException Error reading from cache
   */
  private JsonSchema getDataUseInstance() throws ExecutionException {
    String schemaString = getDataUseSchemaV3();
    JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
    return factory.getSchema(schemaString);
  }

  /**
   * Compares an instance of a data use object to the data use V3 schema
   *
   * @param dataUseV3Instance The string instance of a data use object
   * @return List of validation messages
   */
  public List<String> validateDataUseV3Schema(String dataUseV3Instance) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      JsonNode jsonSubject = mapper.readTree(dataUseV3Instance);
      JsonSchema schema = getDataUseInstance();
      Set<ValidationMessage> messages = schema.validate(jsonSubject);
      return messages.stream().map(ValidationMessage::getMessage).toList();
    } catch (ExecutionException ee) {
      logError("Unable to load the data use schema: " + ee.getMessage());
      return List.of(ee.getMessage());
    } catch (Exception e) {
      throw new BadRequestException("Invalid schema");
    }
  }
}
