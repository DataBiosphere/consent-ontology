# SearchApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**searchGet**](SearchApi.md#searchGet) | **GET** /search | search


<a name="searchGet"></a>
# **searchGet**
> searchGet(id)

search

Find ontologies based on the id.

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SearchApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    SearchApi apiInstance = new SearchApi(defaultClient);
    String id = "id_example"; // String | The ontology id which the service us to find it (e.g. http://purl.obolibrary.org/obo/DOID_162).
    try {
      apiInstance.searchGet(id);
    } catch (ApiException e) {
      System.err.println("Exception when calling SearchApi#searchGet");
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
 **id** | **String**| The ontology id which the service us to find it (e.g. http://purl.obolibrary.org/obo/DOID_162). |

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
**200** | The given id was found. Term result with parent information. |  -  |
**400** | Bad request. |  -  |
**404** | The given id was not found. |  -  |
**0** | Unexpected error |  -  |

