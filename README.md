# Consent Ontology Services

[![Build Status](https://travis-ci.com/broadinstitute/consent-ontology.svg?token=3ve6QNemvC5zpJzsoKzf&branch=develop)](https://travis-ci.com/broadinstitute/consent-ontology)

This repository contains all ontology-related services

## Building and Running

```
mvn clean test
mvn package
java -jar target/consent-ontology.jar server src/test/resources/config.yml
```

## Autocomplete Service

Ontologies can be queried based on term, type, and count. This provides an easy filtering mechanism that allows users to select ontology terms that are applicable to consents or research purposes.

###Endpoint:

```
GET /autocomplete
```

### Query Parameters:
| Parameter | Description |
| --------- | ----------- |
| q	        | The query term (word fragment) which the service should try and complete |
| types	    | An optional list (comma-separated) of term types; if specified, only those types will be searched for autocomplete suggestions. If left unspecified, all available types will be searched. Available values for this service will initially be "disease" and "organization." |
| count	    | An optional limit on the number of autosuggested results that are returned. Default value is 20. |

### Request Parameters:
Request Header: Accept application/json 

### Response Messages:
| HTTP Status | Reason |
| ----------- | ------ |
| 200	      | The consent association representation is present in the body of the response |
| 500	      | Internal Server Error |

### Response Body:
An array of responses:
```
    [{
        "id" : <string>,
        "label" : <string>,
        "definition" : <string>,
        "synonyms": [ <string>* ]
    }]
```

### Example:

```
curl -v -k -X GET -H 'Accept: application/json' https://consent-ontology.dsde-consent-dev.broadinstitute.org/autocomplete?q=heart%20disease
```

### Response:
```
> GET /autocomplete?q=heart%20disease HTTP/1.1
> User-Agent: curl/7.30.0
> Host: consent-ci.broadinstitute.org
> Accept: application/json
>
< HTTP/1.1 200 OK
* Server nginx/1.4.6 (Ubuntu) is not blacklisted
< Server: nginx/1.4.6 (Ubuntu)
< Date: Wed, 06 May 2015 16:23:30 GMT
< Content-Type: application/json
< Transfer-Encoding: chunked
< Connection: keep-alive
< Vary: Accept-Encoding
<
* Connection #0 to host consent-ci.broadinstitute.org left intact
[
    {"id":"http://purl.obolibrary.org/obo/DOID_114","label":"heart disease","definition":"A cardiovascular system disease that involves the heart or blood vessels (arteries and veins).","synonyms":[]},
    {"id":"http://purl.obolibrary.org/obo/DOID_4079","label":"heart valve disease","definition":"A heart disease involving one or more of the valves of the heart (the aortic and mitral valves on the left and the pulmonary and tricuspid valves on the right).","synonyms":["Valvular heart disease (disorder)"]},
    {"id":"http://purl.obolibrary.org/obo/DOID_12325","label":"kyphoscoliotic heart disease","definition":null,"synonyms":["Kyphoscoliotic heart disease (disorder)"]},
    {"id":"http://purl.obolibrary.org/obo/DOID_11516","label":"hypertensive heart disease","definition":"A heart disease that is caused by high blood presure.","synonyms":[]},  
    {"id":"http://purl.obolibrary.org/obo/DOID_3393","label":"coronary heart disease","definition":null,"synonyms":["Coronary disease","CHD (coronary heart disease)","CHD - Coronary heart disease"]},
    {"id":"http://purl.obolibrary.org/obo/DOID_8515","label":"cor pulmonale","definition":"A congestive heart failure that involves a failure of the right side of the heart and is characterized by an enlargement of the right ventricle of the heart as a response to increased resistance or high blood pressure in the lungs.","synonyms":["pulmonary heart disease","cardiopulmonary disease"]},
    {"id":"http://purl.obolibrary.org/obo/DOID_3394","label":"ischemic heart disease","definition":"An extrinsic cardiomyopathy that is characterized by reduced blood supply to the cardiac muscles.","synonyms":["myocardial ischemia","chronic myocardial ischaemia"]}, 
    {"id":"http://purl.obolibrary.org/obo/DOID_1954","label":"benign hypertensive heart disease","definition":null,"synonyms":["benign hypertensive heart disease (disorder)","benign hypertensive heart disease NOS (disorder)"]},
    {"id":"http://purl.obolibrary.org/obo/DOID_548","label":"malignant hypertensive heart disease","definition":null,"synonyms":["malignant hypertensive heart disease (disorder)","malignant hypertensive heart disease NOS (disorder)"]},
    {"id":"http://purl.obolibrary.org/obo/DOID_9814","label":"rheumatic heart disease","definition":null,"synonyms":["Rheumatic carditis"]},
    {"id":"http://purl.obolibrary.org/obo/DOID_8514","label":"acute pulmonary heart disease","definition":null,"synonyms":["acute pulmonary heart disease (disorder)","acute pulmonary heart disease NOS (disorder)"]},
    {"id":"http://purl.obolibrary.org/obo/DOID_12326","label":"chronic pulmonary heart disease","definition":null,"synonyms":[]},
    {"id":"http://purl.obolibrary.org/obo/DOID_6000","label":"congestive heart failure","definition":"A heart disease that is characterized by any structural or functional cardiac disorder that impairs the ability of the heart to fill with or pump a sufficient amount of blood throughout the body.","synonyms":["Weak heart","Cardiac Failure Congestive","CHF","Congestive heart disease"]}
]
```

## Translate Service

The Translate service will render an English-language version of a consent or research purpose.

### Endpoint:
```
POST /translate
```

### Request Parameters:

Request Header: Accept application/json

Request Header: Content-type application/json

Request Body: Use Restriction Resource in json format

### Query Parameters:
| Parameter | Options   | Description |
| --------- | --------- | ----------- |
| for	    | purpose   | Translates a research purpose |
|           | sampleset | Translates a consent |

### Response Messages:
| HTTP Status | Reason |
| ----------- | ------ |
| 200         | Successful translation |
| 400         | Missing query parameter |
| 500         | Internal Server Error or malformed purpose/consent |

### Response Body:
Plain text response

### Example (Sampleset):
```
curl -v -k -X POST -H 'Content-type: application/json' -H 'Accept: application/json' https://consent-ontology.dsde-consent-dev.broadinstitute.org/translate?for=sampleset -d '{"type":"named","name": "http://purl.obolibrary.org/obo/DOID_1240"}'
```

### Response:
```
> POST /translate?for=sampleset HTTP/1.1
> User-Agent: curl/7.30.0
> Host: consent-ci.broadinstitute.org
> Content-type: application/json
> Accept: application/json
> Content-Length: 67
>
* upload completely sent off: 67 out of 67 bytes
< HTTP/1.1 200 OK
* Server nginx/1.4.6 (Ubuntu) is not blacklisted
< Server: nginx/1.4.6 (Ubuntu)
< Date: Thu, 07 May 2015 18:56:46 GMT
< Content-Type: application/json
< Transfer-Encoding: chunked
< Connection: keep-alive
<
* Connection #0 to host consent-ci.broadinstitute.org left intact
Samples may only be used for the purpose of studying leukemia.
```
### Example (Purpose):
```
curl -v -k -X POST -H 'Content-type: application/json' -H 'Accept: application/json' https://consent-ontology.dsde-consent-dev.broadinstitute.org/translate?for=purpose -d '{"type":"named","name": "http://purl.obolibrary.org/obo/DOID_1240"}'
```
### Response:
```
> POST /translate?for=purpose HTTP/1.1
> User-Agent: curl/7.30.0
> Host: consent-ci.broadinstitute.org
> Content-type: application/json
> Accept: application/json
> Content-Length: 67
>
* upload completely sent off: 67 out of 67 bytes
< HTTP/1.1 200 OK
* Server nginx/1.4.6 (Ubuntu) is not blacklisted
< Server: nginx/1.4.6 (Ubuntu)
< Date: Thu, 07 May 2015 19:09:23 GMT
< Content-Type: application/json
< Transfer-Encoding: chunked
< Connection: keep-alive
<
* Connection #0 to host consent-ci.broadinstitute.org left intact
Any sample which can be used for the purpose of studying leukemia. In addition, those samples can be used for commercial purposes.
```

## Match Service

The Match service will render a response of **true** or **false** between a Consent and a Research Purpose.

### Endpoint:
```
POST /match
```

### Request Parameters:

Request Header: Accept application/json

Request Header: Content-type application/json

Request Body: Consent and Purpose Use Restrictions in json format


### Response Messages:
| HTTP Status | Reason |
| ----------- | ------ |
| 200         | Successful matching |
| 500         | Internal Server Error or malformed purpose/consent |

### Response Body:
Json response

### Example:
```
curl -v -k -X POST -H 'Content-type: application/json' -H 'Accept:application/json' https://consent-ontology.dsde-consent-dev.broadinstitute.org/match -d '{"purpose":{ "type":"and", "operands":[ {   "type":"named", "name":"http://www.broadinstitute.org/ontologies/DUOS/methods_research"  }, { "type":"named", "name":"http://purl.obolibrary.org/obo/DOID162" }, {   "type":"named",    "name":"http://www.broadinstitute.org/ontologies/DUOS/Non_profit"   }  ] }, "consent":{  "type":"and",  "operands":[ {   "type":"named",  "name":"http://www.broadinstitute.org/ontologies/DUOSNon_profit"  } ] }}'
```

### Response:
```
> POST /match HTTP/1.1
> User-Agent: curl/7.35.0
> Host: consent-ontology.dsde-consent-dev.broadinstitute.org
> Content-type: application/json
> Accept:application/json
> Content-Length: 441
> 
* upload completely sent off: 441 out of 441 bytes
< HTTP/1.1 200 OK
< Date: Wed, 09 Dec 2015 17:19:37 GMT
* Server Apache/2.4.7 (Ubuntu) is not blacklisted
< Server: Apache/2.4.7 (Ubuntu)
< Content-Type: application/json
< Content-Length: 428
< Access-Control-Allow-Origin: *
< Access-Control-Allow-Headers: authorization, content-type, accept, origin
< Access-Control-Allow-Methods: GET,POST,PUT,PATCH,DELETE,OPTIONS,HEAD
< 
{"result":false,"matchPair":{"purpose":{"type":"and","operands":[{"type":"named","name":"http://www.broadinstitute.org/ontologies/DUOS/methods_research"},{"type":"named","name":"http://purl.obolibrary.org/obo/DOID162"},{"type":"named","name":"http://www.broadinstitute.org/ontologies/DUOS/Non_profit"}]},"consent":{"type":"and","operands":[{"type":"named","name":"http://www.broadinstitute.org/ontologies/DUOSNon_profit"}]}}}
```

## Configuration

```
server:
  adminConnectors:
    - type: http
      port: 8181
  applicationConnectors:
    - type: http
      port: 8180
elasticSearch:
  servers: ["127.0.0.1:9300"]
  index: ontology
```
