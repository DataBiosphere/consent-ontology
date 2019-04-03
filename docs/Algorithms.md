# Matching Algorithm

See the [Swagger](https://consent-ontology.dsde-prod.broadinstitute.org/#/) page for detailed usage
of the Matching APIs. This document describes the different approaches to matching a Research Purpose
and a Consent on a dataset. Both Purpose and Consent are structured objects which allows for computational
matching at scale. 

## Version 1
The original version of the algorithm uses an ontology tree to match a purpose and consent. First, we 
construct a composite ontology tree from:

* [Human Disease Ontology](https://www.ebi.ac.uk/ols/ontologies/doid)
* [Data Use Ontology](https://www.ebi.ac.uk/ols/ontologies/duo)
* [Broad Data Use Ontology](https://github.com/DataBiosphere/consent-data-use/)

Next, we create ontology nodes for the consent, the purpose and add them to the composite tree. Using 
an [OWL](https://github.com/owlcs/owlapi) Reasoner, we determine if the purpose is a subclass of the 
consent, or not. A valid ontological subclass for a research purpose indicates a successful match 
between the purpose and the consent. See [Use Restriction Grammar](./UseRestrictionGrammar.md) for how
we create ontology nodes for a consent or research purpose.

## Version 2
This version of the algorithm uses a custom set of business rules to match a research purpose and consent.
This was developed for [FireCloud](https://api.firecloud.org/) and is the basis for the Data Catalog search ruleset. 
This version makes use of [Consent Codes](https://journals.plos.org/plosgenetics/article?id=10.1371/journal.pgen.1005772)
as developed for the GA4GH as well as Disease Codes (**DS-X**) from the [Human Disease Ontology](https://www.ebi.ac.uk/ols/ontologies/doid).

| If my Research Purpose has... | Related DUL question | I should see ... |
| ----------------------------- | -------------------- | ---------------- |
| Disease focused research | Future use is limited to research involving the following disease area(s) **DS** | Any dataset with **GRU**=true |
| | | Any dataset with **HMB**=true  |
| | | Any dataset tagged to this disease exactly |
| | | Any dataset tagged to a DOID ontology **Parent** of **DS-X** |
| | | |
| Methods development/Validation study | Future use for methods research (analytic/software/technology development) outside the bounds of the other specified restrictions is prohibited **NMDS** | Any dataset with **GRU**=true |
| | | Any dataset where **NMDS** is false |
| | | Any dataset where **NMDS** is true AND **DS-X** match |
| | | |
| Control set | Future use as a control set for diseases other than those specified is prohibited **NCTRL** | Any dataset where **NCTRL** is false and is (**GRU** or **HMB**) |
| | | Any **DS-X** match, if user specified a disease in the research purpose |
| | | |
| Aggregate analysis to understand variation in the general population | Future use of aggregate-level data for general research purposes is prohibited **NAGR** | Any dataset where **NAGR** is false and is (**GRU** or **HMB**) | 
| | | |
| Study population origins or ancestry | Future use is limited to research involving a specific population **POA** | Any dataset tagged with **GRU** |
| | | |
| Commercial purpose/by a commercial entity | Future commercial use is prohibited **NCU**. Future use by for-profit entities is prohibited **NPU** | Any dataset where **NPU** and **NCU** are both false |
| | | |
| Pediatric focused research | Future use is limited to pediatric research **RS-PD** | Any dataset tagged with **RS-PD** |
| | | |
| Gender focused research | Future use is limited to research involving a particular gender **RS-G** | Any dataset tagged with **RS-G:F** OR **N/A** when gender is **F** |
| | | Any dataset tagged with **RS-G:M** OR **N/A**  when gender is **M** |
| | | |








