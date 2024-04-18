package org.broadinstitute.dsde.consent.ontology.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SchemaValidatorsConfig;
import com.networknt.schema.SpecVersion.VersionFlag;
import com.networknt.schema.ValidationMessage;
import jakarta.ws.rs.BadRequestException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.commons.io.IOUtils;
import org.broadinstitute.dsde.consent.ontology.OntologyLogger;

public class JsonSchemaUtil implements OntologyLogger {

  private final LoadingCache<String, String> cache;
  private final String dataUseSchemaV3 = "/data-use-v3.json";

  private final String dataUseSchemaV4 = "/data-use-v4.json";

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

  public String getDataUseSchemaV4() {
    try {
      return cache.get(dataUseSchemaV4);
    } catch (ExecutionException ee) {
      logError("Unable to load the data use schema V3: " + ee.getMessage());
      return null;
    }
  }

  /**
   * Loads a JsonSchema populated from the data use V3 schema
   *
   * @return Schema The Schema
   */
  private JsonSchema getDataUseV3Instance() {
    String schemaString = getDataUseSchemaV3();
    JsonSchemaFactory factory = JsonSchemaFactory.getInstance(VersionFlag.V7);
    SchemaValidatorsConfig config = new SchemaValidatorsConfig();
    config.setHandleNullableField(false);
    config.setTypeLoose(false);
    config.setFormatAssertionsEnabled(true);
    return factory.getSchema(schemaString, config);
  }

  /**
   * Loads a JsonSchema populated from the data use V3 schema
   *
   * @return Schema The Schema
   */
  private JsonSchema getDataUseV4Instance() {
    String schemaString = getDataUseSchemaV4();
    JsonSchemaFactory factory = JsonSchemaFactory.getInstance(VersionFlag.V7);
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
      JsonSchema schema = getDataUseV3Instance();
      Set<ValidationMessage> messages = schema.validate(jsonSubject);
      return messages.stream().map(ValidationMessage::getMessage).toList();
    } catch (Exception e) {
      logError("Unable to load the data use schema: " + e.getMessage());
      throw new BadRequestException("Invalid schema");
    }
  }

  /**
   * Compares an instance of a data use object to the data use V4 schema
   *
   * @param dataUseV4Instance The string instance of a data use object
   * @return List of validation messages
   */
  public List<String> validateDataUseV4Schema(String dataUseV4Instance) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      JsonNode jsonSubject = mapper.readTree(dataUseV4Instance);
      JsonSchema schema = getDataUseV4Instance();
      Set<ValidationMessage> messages = schema.validate(jsonSubject);
      List<String> validationMessages = messages.stream().map(ValidationMessage::getMessage).toList();
      // Additional validation for the required fields. One of GRU or HMB must be true or
      // there must be a non-empty Disease Restriction array. Additionally, only one of the three
      // can be true.
      JsonNode gru = jsonSubject.get("generalUse");
      JsonNode hmb = jsonSubject.get("hmbResearch");
      JsonNode diseases = jsonSubject.get("diseaseRestrictions");
      boolean gruPositive = false;
      boolean hmbPositive = false;
      boolean diseasePositive = false;
      if (gru != null && gru.asBoolean()) {
        gruPositive = true;
      }
      if (hmb != null && hmb.asBoolean()) {
        hmbPositive = true;
      }
      if (diseases != null && diseases.isArray()) {
        List<JsonNode> diseaseNodes = StreamSupport
            .stream( diseases.spliterator(), false).toList();
        if (!diseaseNodes.isEmpty()) {
          diseasePositive = true;
        }
      }
      List<Boolean> primaryPositives = Stream.of(gruPositive, hmbPositive, diseasePositive)
          .filter(p -> p)
          .toList();
      if (primaryPositives.isEmpty()) {
        // Note that error messages here are formatted to resemble the messages generated by the schema validator
        List<String> required = List.of("$: At least one of 'generalUse', 'hmbResearch' must be true or 'diseaseRestrictions' cannot be empty");
        return Stream.
            concat(validationMessages.stream(), required.stream()).
            toList();
      }
      if (primaryPositives.size() > 1) {
        List<String> tooManyPrimaries = List.of("$: At most, only one of the following conditions may be true: 'generalUse'; 'hmbResearch'; 'diseaseRestrictions' cannot be empty");
        return Stream.
            concat(validationMessages.stream(), tooManyPrimaries.stream()).
            toList();
      }
      return messages.stream().map(ValidationMessage::getMessage).toList();
    } catch (Exception e) {
      logError("Unable to load the data use schema: " + e.getMessage());
      throw new BadRequestException("Invalid schema");
    }
  }

}
