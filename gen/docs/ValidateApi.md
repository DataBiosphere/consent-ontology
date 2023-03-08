# ValidateApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**validationDataUseV3Get**](ValidateApi.md#validationDataUseV3Get) | **GET** /validation/data-use/V3 | validate


<a name="validationDataUseV3Get"></a>
# **validationDataUseV3Get**
> validationDataUseV3Get(body)

validate

The Validate service will check If the posted Use Restriction has the correct format and content.

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.ValidateApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    ValidateApi apiInstance = new ValidateApi(defaultClient);
    Object body = null; // Object | Data Use V3
    try {
      apiInstance.validationDataUseV3Get(body);
    } catch (ApiException e) {
      System.err.println("Exception when calling ValidateApi#validationDataUseV3Get");
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
 **body** | **Object**| Data Use V3 |

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
**200** | Successful validation. True for valid restriction and False for invalid. |  -  |
**0** | Unexpected error |  -  |

