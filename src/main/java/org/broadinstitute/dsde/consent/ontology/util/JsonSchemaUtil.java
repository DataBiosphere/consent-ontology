package org.broadinstitute.dsde.consent.ontology.util;

import java.util.List;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.io.IOUtils;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.BadRequestException;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;

public class JsonSchemaUtil {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());
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
      logger.error("Unable to load the data use schema V3: " + ee.getMessage());
      return null;
    }
  }

  /**
   * Loads a Schema populated from the data use V3 schema
   * @return Schema The Schema
   * @throws ExecutionException Error reading from cache
   */
  private Schema getDataUseInstance() throws ExecutionException {
    String schemaString = getDataUseSchemaV3();
    JSONObject jsonSchema = new JSONObject(schemaString);
    return SchemaLoader
        .builder()
        .schemaJson(jsonSchema)
        .build()
        .load()
        .build();
  }

  /**
   * Compares an instance of a data use object to the data use V3 schema
   *
   * @param dataUseV3Instance The string instance of a data use object
   * @return True if the instance validates, false otherwise
   */

  public List<String> validateDataUseV3Schema(String dataUseV3Instance) {
    try {
      JSONObject jsonSubject = new JSONObject(dataUseV3Instance);
      Schema schema = getDataUseInstance();
      schema.validate(jsonSubject);
      return List.of();
    } catch (
        ExecutionException ee) {
      logger.error("Unable to load the data use schema: " + ee.getMessage());
      return List.of(ee.getMessage());
    } catch (ValidationException ve) {
      if (logger.isDebugEnabled()) {
        logger.debug("Provided instance does not validate: " + ve.getMessage());
      }
      if (logger.isTraceEnabled()) {
        for (String m : ve.getAllMessages()) {
          logger.trace("Validation error: " + m);
        }
      }
      return ve.getAllMessages();
    } catch (Exception e) {
      throw new BadRequestException("Invalid schema");
    }

  }
}
