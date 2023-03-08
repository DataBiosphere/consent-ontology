# DataUseApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**schemasDataUseGet**](DataUseApi.md#schemasDataUseGet) | **GET** /schemas/data-use | Data Use Questions and Answers
[**schemasDataUseV2Get**](DataUseApi.md#schemasDataUseV2Get) | **GET** /schemas/data-use/v2 | Data Use Questions and Answers
[**schemasDataUseV3Get**](DataUseApi.md#schemasDataUseV3Get) | **GET** /schemas/data-use/v3 | Data Use Questions and Answers Version 3
[**translatePost**](DataUseApi.md#translatePost) | **POST** /translate | Translate Data Use JSON into plain English
[**translateSummaryPost**](DataUseApi.md#translateSummaryPost) | **POST** /translate/summary | Translate Data Use JSON into Data Use Summary JSON


<a name="schemasDataUseGet"></a>
# **schemasDataUseGet**
> schemasDataUseGet()

Data Use Questions and Answers

JSON representation of data use questions + answers for use in defining structured data use restrictions and data access requests.

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.DataUseApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    DataUseApi apiInstance = new DataUseApi(defaultClient);
    try {
      apiInstance.schemasDataUseGet();
    } catch (ApiException e) {
      System.err.println("Exception when calling DataUseApi#schemasDataUseGet");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters
This endpoint does not need any parameter.

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
**200** | The json representation for all data use questions + answers |  -  |
**0** | Unexpected error |  -  |

<a name="schemasDataUseV2Get"></a>
# **schemasDataUseV2Get**
> schemasDataUseV2Get()

Data Use Questions and Answers

JSON representation of data use questions + answers for use in defining structured data use restrictions and data access requests.

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.DataUseApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    DataUseApi apiInstance = new DataUseApi(defaultClient);
    try {
      apiInstance.schemasDataUseV2Get();
    } catch (ApiException e) {
      System.err.println("Exception when calling DataUseApi#schemasDataUseV2Get");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters
This endpoint does not need any parameter.

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
**200** | The json representation for all data use questions + answers |  -  |
**0** | Unexpected error |  -  |

<a name="schemasDataUseV3Get"></a>
# **schemasDataUseV3Get**
> schemasDataUseV3Get()

Data Use Questions and Answers Version 3

JSON representation of data use questions + answers for use in defining structured data use restrictions and data access requests.

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.DataUseApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    DataUseApi apiInstance = new DataUseApi(defaultClient);
    try {
      apiInstance.schemasDataUseV3Get();
    } catch (ApiException e) {
      System.err.println("Exception when calling DataUseApi#schemasDataUseV3Get");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters
This endpoint does not need any parameter.

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
**200** | The json representation for all data use questions + answers |  -  |
**0** | Unexpected error |  -  |

<a name="translatePost"></a>
# **translatePost**
> translatePost(_for, body)

Translate Data Use JSON into plain English

The Translate service will render an English-language version of a research purpose or dataset.

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.DataUseApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    DataUseApi apiInstance = new DataUseApi(defaultClient);
    String _for = "_for_example"; // String | Either 'dataset' or 'purpose'
    Object body = null; // Object | DataUse
    try {
      apiInstance.translatePost(_for, body);
    } catch (ApiException e) {
      System.err.println("Exception when calling DataUseApi#translatePost");
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
 **_for** | **String**| Either &#39;dataset&#39; or &#39;purpose&#39; | [enum: dataset, purpose]
 **body** | **Object**| DataUse |

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
**200** | Successful translation. |  -  |
**0** | Unexpected error |  -  |

<a name="translateSummaryPost"></a>
# **translateSummaryPost**
> DataUseSummary translateSummaryPost(body)

Translate Data Use JSON into Data Use Summary JSON

The Translate service will render an structured summary with consent codes

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.DataUseApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    DataUseApi apiInstance = new DataUseApi(defaultClient);
    Object body = null; // Object | DataUse
    try {
      DataUseSummary result = apiInstance.translateSummaryPost(body);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling DataUseApi#translateSummaryPost");
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
 **body** | **Object**| DataUse |

### Return type

[**DataUseSummary**](DataUseSummary.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Successful translation. |  -  |
**0** | Unexpected error |  -  |

