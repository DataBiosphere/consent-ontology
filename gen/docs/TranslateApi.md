# TranslateApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**translateParagraphPost**](TranslateApi.md#translateParagraphPost) | **POST** /translate/paragraph | Translates Paragraph to ontology recommendations
[**translatePost**](TranslateApi.md#translatePost) | **POST** /translate | Translate Data Use JSON into plain English
[**translateSummaryPost**](TranslateApi.md#translateSummaryPost) | **POST** /translate/summary | Translate Data Use JSON into Data Use Summary JSON


<a name="translateParagraphPost"></a>
# **translateParagraphPost**
> Object translateParagraphPost(body)

Translates Paragraph to ontology recommendations

The Translate service for paragraphs will render an ontology recommendation for a given paragraph.

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.TranslateApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    TranslateApi apiInstance = new TranslateApi(defaultClient);
    TranslateParagraph body = new TranslateParagraph(); // TranslateParagraph | Paragraph to translate
    try {
      Object result = apiInstance.translateParagraphPost(body);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling TranslateApi#translateParagraphPost");
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
 **body** | [**TranslateParagraph**](TranslateParagraph.md)| Paragraph to translate |

### Return type

**Object**

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
import org.openapitools.client.api.TranslateApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    TranslateApi apiInstance = new TranslateApi(defaultClient);
    String _for = "_for_example"; // String | Either 'dataset' or 'purpose'
    Object body = null; // Object | DataUse
    try {
      apiInstance.translatePost(_for, body);
    } catch (ApiException e) {
      System.err.println("Exception when calling TranslateApi#translatePost");
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
import org.openapitools.client.api.TranslateApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    TranslateApi apiInstance = new TranslateApi(defaultClient);
    Object body = null; // Object | DataUse
    try {
      DataUseSummary result = apiInstance.translateSummaryPost(body);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling TranslateApi#translateSummaryPost");
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

