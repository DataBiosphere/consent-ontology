# Use Restriction Grammar

A Consent or Research Purpose is a rendering of an OWL class, into JSON, 
using the following grammar:

```
Consent := {
  "restriction": UseRestriction
}
 
ResearchPurpose := {
  "restriction": UseRestriction
}
```

A use restriction expression represents a consent attached to a single sample 
or collection of samples or a research purpose. The "restriction" field 
contains the structured use restriction.

```
UseRestriction := 
    OrExpression
  | AndExpression
  | NotExpression
  | NamedExpression
  | EverythingExpression
  | NothingExpression

OrExpression := { 
  "type": "or", 
  "operands": [ UseRestriction* ]
}

AndExpression := { 
  "type": "and", 
  "operands": [ UseRestriction* ]
}

NotExpression := { 
  "type": "not", 
  "operand": UseRestriction
}

NamedExpression := { 
  "type": "named", 
  "name": <string>
}

EverythingExpression := { 
  "type": "everything"
}

NothingExpression := { 
  "type": "nothing"
}
```

## Common Examples

### Named Conditions
Intermediate forms of use restriction are usually represented using the 
NamedExpression and the boolean operators (And, Or, Not). For example, 
a sample which was only available for use in cancer research might be 
annotated with the expression:

```
// Consent restriction only for Cancer
{
  "restriction": {
    "type": "named",
    "name": "http://purl.obolibrary.org/obo/DOID_162"
  }
}
```

### Or Conditions

If a study were restricted to cancer (DOID:162) OR heart disease (DOID:114), 
we could represent that with the following expression:

```
// Consent restriction for Cancer OR Heart Disease
{
  "restriction": {
    "type": "or",
    "operands": [ 
      {
        "type": "named",
        "name": "http://purl.obolibrary.org/obo/DOID_162"
      },
      {
        "type": "named",
        "name": "http://purl.obolibrary.org/obo/DOID_114"
      }
    ]
  }
}
```

### And Conditions
And conditions typically incorporate different categories of information. 
Studies that restrict data usage only to research for pediatric cancer 
are a good example of that. Such an expression might be expressed this way:

```
// Consent restriction for Pediatric Cancer
{
  "restriction": {
    "type": "and",
    "operands": [ 
      {
        "type": "named",
        "name": "http://purl.obolibrary.org/obo/DOID_162"
      },
      {
        "type": "named",
        "name": "http://www.broadinstitute.org/ontologies/DUOS/pediatric"
      }
    ]
  }
}
```

