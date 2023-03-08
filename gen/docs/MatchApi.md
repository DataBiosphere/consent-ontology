# MatchApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**matchV2Post**](MatchApi.md#matchV2Post) | **POST** /match/v2 | Match DataUse Purpose and Consent


<a name="matchV2Post"></a>
# **matchV2Post**
> DataUseMatchPairResult matchV2Post(body)

Match DataUse Purpose and Consent

Determine if a Research Purpose and Consent logically match.

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.MatchApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    MatchApi apiInstance = new MatchApi(defaultClient);
    DataUseMatchPair body = new DataUseMatchPair(); // DataUseMatchPair | Research Purpose and Dataset DataUse pair.
    try {
      DataUseMatchPairResult result = apiInstance.matchV2Post(body);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling MatchApi#matchV2Post");
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
 **body** | [**DataUseMatchPair**](DataUseMatchPair.md)| Research Purpose and Dataset DataUse pair. |

### Return type

[**DataUseMatchPairResult**](DataUseMatchPairResult.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Successful matching. |  -  |
**400** | Bad Request if either purpose or consent are not provided. |  -  |
**500** | Internal Server Error |  -  |

