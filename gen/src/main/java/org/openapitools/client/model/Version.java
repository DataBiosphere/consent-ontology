/*
 * Ontology Services
 * A set of web-services related to ontology information.
 *
 * The version of the OpenAPI document: 0.0.1
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package org.openapitools.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;

/**
 * Version
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-03-08T13:37:16.800722-05:00[America/New_York]")
public class Version {
  public static final String SERIALIZED_NAME_HASH = "hash";
  @SerializedName(SERIALIZED_NAME_HASH)
  private String hash;

  public static final String SERIALIZED_NAME_VERSION = "version";
  @SerializedName(SERIALIZED_NAME_VERSION)
  private String version;


  public Version hash(String hash) {
    
    this.hash = hash;
    return this;
  }

   /**
   * Current short hash of this version
   * @return hash
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Current short hash of this version")

  public String getHash() {
    return hash;
  }


  public void setHash(String hash) {
    this.hash = hash;
  }


  public Version version(String version) {
    
    this.version = version;
    return this;
  }

   /**
   * Current version
   * @return version
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Current version")

  public String getVersion() {
    return version;
  }


  public void setVersion(String version) {
    this.version = version;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Version version = (Version) o;
    return Objects.equals(this.hash, version.hash) &&
        Objects.equals(this.version, version.version);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hash, version);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Version {\n");
    sb.append("    hash: ").append(toIndentedString(hash)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

