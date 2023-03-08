# StatusApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**livenessGet**](StatusApi.md#livenessGet) | **GET** /liveness | Liveness Endpoint
[**statusErrorGet**](StatusApi.md#statusErrorGet) | **GET** /status/error | Generate a logged error
[**statusGet**](StatusApi.md#statusGet) | **GET** /status | System Health Status
[**versionGet**](StatusApi.md#versionGet) | **GET** /version | Current application version


<a name="livenessGet"></a>
# **livenessGet**
> livenessGet()

Liveness Endpoint

Liveness endpoint for conducting quick health checks on the application.

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.StatusApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    StatusApi apiInstance = new StatusApi(defaultClient);
    try {
      apiInstance.livenessGet();
    } catch (ApiException e) {
      System.err.println("Exception when calling StatusApi#livenessGet");
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
**200** | Responds with &#39;Healthy&#39; if endpoint is reachable. |  -  |

<a name="statusErrorGet"></a>
# **statusErrorGet**
> statusErrorGet(message)

Generate a logged error

Generate a logged error

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.StatusApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    StatusApi apiInstance = new StatusApi(defaultClient);
    String message = "message_example"; // String | Message to log as an error
    try {
      apiInstance.statusErrorGet(message);
    } catch (ApiException e) {
      System.err.println("Exception when calling StatusApi#statusErrorGet");
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
 **message** | **String**| Message to log as an error | [optional]

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
**200** | Error successfully logged |  -  |
**500** | Other error |  -  |

<a name="statusGet"></a>
# **statusGet**
> statusGet()

System Health Status

A detailed description of the various subsystem statuses that Ontology relies upon.

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.StatusApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    StatusApi apiInstance = new StatusApi(defaultClient);
    try {
      apiInstance.statusGet();
    } catch (ApiException e) {
      System.err.println("Exception when calling StatusApi#statusGet");
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
**200** | All systems are OK |  -  |
**500** | Some number of subsystems are not OK. |  -  |

<a name="versionGet"></a>
# **versionGet**
> Version versionGet()

Current application version

The current short hash and version of the application.

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.StatusApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    StatusApi apiInstance = new StatusApi(defaultClient);
    try {
      Version result = apiInstance.versionGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling StatusApi#versionGet");
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

[**Version**](Version.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Successful Response |  -  |
**500** | Internal Server Error |  -  |

