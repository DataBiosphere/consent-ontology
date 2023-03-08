# AutocompleteApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**autocompleteGet**](AutocompleteApi.md#autocompleteGet) | **GET** /autocomplete | autocomplete


<a name="autocompleteGet"></a>
# **autocompleteGet**
> autocompleteGet(q, types, count)

autocomplete

Ontologies can be queried based on term; type and count. This provides an easy filtering mechanism that allows users to select ontology terms that are applicable to consents or research purposes.

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.AutocompleteApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    AutocompleteApi apiInstance = new AutocompleteApi(defaultClient);
    String q = "q_example"; // String | The query term (word fragment) which the service should try and complete.
    String types = "types_example"; // String | An optional list (comma-separated) of term types; if specified only those types will be searched for autocomplete suggestions. If left unspecified all available types will be searched. Available values for this service will initially be disease and organization.
    BigDecimal count = new BigDecimal(78); // BigDecimal | An optional limit on the number of autosuggested results that are returned. Default value is 20.
    try {
      apiInstance.autocompleteGet(q, types, count);
    } catch (ApiException e) {
      System.err.println("Exception when calling AutocompleteApi#autocompleteGet");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **q** | **String**| The query term (word fragment) which the service should try and complete. |
 **types** | **String**| An optional list (comma-separated) of term types; if specified only those types will be searched for autocomplete suggestions. If left unspecified all available types will be searched. Available values for this service will initially be disease and organization. | [optional]
 **count** | **BigDecimal**| An optional limit on the number of autosuggested results that are returned. Default value is 20. | [optional]

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Array of results. |  -  |
**0** | Unexpected error |  -  |

