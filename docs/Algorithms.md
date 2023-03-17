# Matching Algorithm

See the [Swagger](https://consent-ontology.dsde-prod.broadinstitute.org/#/) page for detailed usage
of the Matching APIs. This document describes the different approaches to matching a Research Purpose
and a Consent on a dataset. Both Purpose and Consent are structured objects which allows for computational
matching at scale. 

## Version 1
### Deprecated & Removed
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
This version of the algorithm uses a custom set of business rules to match a research purpose and consented dataset. 
In determining a postive match between research purpose and consented dataset, we make sure that the consented
dataset matches **ALL** conditions specified in the research purpose.  

This was originally developed for [FireCloud](https://api.firecloud.org/) and is the basis for the Data Catalog search ruleset. 
This version makes use of [Consent Codes](https://journals.plos.org/plosgenetics/article?id=10.1371/journal.pgen.1005772)
as developed for the GA4GH as well as Disease Codes (**DS-X**) from the [Human Disease Ontology](https://www.ebi.ac.uk/ols/ontologies/doid).

<table>
	<thead>
		<tr>
			<th>If my Research Purpose has...</th>
			<th>What datasets should I see?</th>
			<th>Related DUL question</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td>Disease focused research (i.e. <strong>DS-X</strong>)</td>
			<td>
				<ul>
					<li>Any dataset with <strong>GRU</strong>=true</li>
					<li>Any dataset with <strong>HMB</strong>=true</li>
					<li>Any dataset tagged to this disease (<strong>DS-X</strong>) exactly or a parent disease of <strong>DS-X</strong></li>
				</ul>	
			</td>	
			<td>
				<ul>
					<li>Data is available for future general research use</li>
					<li>Future use is limited for health/medical/biomedical research</li>
					<li>Future use is limited to research involving the following disease area(s) <strong>DS-X</strong></li>					
				</ul>
			</td>
		</tr>
		<tr>
			<td>Methods development/Validation study</td>
			<td>
				<ul>
					<li>Any dataset with <strong>GRU</strong>=true</li>
					<li>Any dataset where <strong>NMDS</strong> is false</li>
					<li>Any dataset where <strong>NMDS</strong> is true AND <strong>DS-X</strong> match</li>
				</ul>	
			</td>	
			<td>
				<ul>
					<li>Future use for methods research (analytic/software/technology development) outside the bounds of the other specified restrictions is prohibited <strong>NMDS</strong></li>
				</ul>						
			</td>
		</tr>
		<tr>
			<td>Control Set</td>
			<td>
				<ul>
					<li>Any dataset where <strong>NCTRL</strong> is false and is (<strong>GRU</strong> or <strong>HMB</strong>)</li>
					<li>Any <strong>DS-X</strong> match, if user specified a disease in the research purpose</li>
				</ul>	
			</td>	
			<td>
				<ul>
					<li>Future use as a control set for diseases other than those specified is prohibited <strong>NCTRL</strong></li>
					<li>Future use is limited to research involving the following disease area(s) <strong>DS-X</strong></li>
				</ul>	
			</td>
		</tr>
		<tr>
			<td>Aggregate analysis to understand variation in the general population</td>
			<td>
				<ul>
					<li>Any dataset where <strong>NAGR</strong> is false and is (<strong>GRU</strong> or <strong>HMB</strong>)</li>
				</ul>
			</td>
			<td>
				<ul>
					<li>Future use of aggregate-level data for general research purposes is prohibited <strong>NAGR</strong></li>
				</ul>
			</td>
		</tr>
		<tr>
			<td>Study population origins or ancestry</td>
			<td>
				<ul>
					<li>Any dataset tagged with <strong>GRU</strong></li>
				</ul>
			</td>
			<td>
				<ul>
					<li>Future use is limited to research involving a specific population POA</li>
				</ul>
			</td>
		</tr>
		<tr>
			<td>Commercial purpose/by a commercial entity</td>
			<td>
				<ul>
					<li>Any dataset where <strong>NPU</strong> and <strong>NCU</strong> are both false</li>
				</ul>			
			</td>
			<td>
				<ul>
					<li>Future commercial use is prohibited <strong>NCU</strong>. Future use by for-profit entities is prohibited <strong>NPU</strong></li>
				</ul>			
			</td>
		</tr>
		<tr>
			<td>Pediatric focused research</td>
			<td>
				<ul>
					<li>Any dataset tagged with <strong>RS-PD</strong></li>
				</ul>
			</td>
			<td>
				<ul>
					<li>Future use is limited to pediatric research <strong>RS-PD</strong></li>
				</ul>
			</td>
		</tr>
		<tr>
			<td>Gender focused research</td>
			<td>
				<ul>
					<li>Any dataset tagged with <strong><strong>RS-G</strong>:F</strong> OR N/A when gender is F</li>
					<li>Any dataset tagged with <strong><strong>RS-G</strong>:M</strong> OR N/A when gender is M</li>
				</ul>			
			</td>
			<td>
				<ul>
					<li>Future use is limited to research involving a particular gender <strong>RS-G</strong></li>
				</ul>
			</td>
		</tr>	
	</tbody>
</table>

## Version 3
This version of the algorithm uses a custom set of business rules to match a research purpose and consented dataset. 
In determining a postive match between research purpose and consented dataset, we make sure that the consented
dataset matches **ALL** conditions specified in the research purpose.

<table>
	<thead>
		<tr>
			<th>If my Research Purpose has...</th>
			<th>What datasets should I see?</th>
			<th>Logical Rationale</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td>Disease focused research (i.e. <strong>DS-X</strong>)</td>
			<td>
				<ul>
					<li>Any dataset tagged with <strong>GRU</strong>=true</li>
          <li>Any dataset tagged with <strong>HMB</strong>=true</li>
					<li>Any dataset tagged to this disease (<strong>DS-X</strong>) exactly or a parent disease of <strong>DS-X</strong></li>
				</ul>	
			</td>	
			<td>
				<ul>
          <li>If the dataset's Primary DUO terms is DS-, and the DAR's Primary DUO term (aka Permission) is the DS- or a subclass then <strong>Approve</strong></li>
					<li>If the dataset's Primary DUO terms is DS-, and the DAR's Primary DUO term (aka Permission) is GRU, HMB, POA then <strong>Deny</strong></li>
					<li>If the dataset's Primary DUO terms is DS-, and the DAR's Primary DUO term (aka Permission) is NOT the DS- or a subclass then <strong>Deny</strong></li>			
				</ul>
			</td>
		</tr>
		<tr>
			<td>Methods development (i.e. <strong>MDS</strong>)</td>
			<td>
				<ul>
					<li>Any dataset tagged with <strong>GRU</strong>=true</li>
          <li>Any dataset tagged with <strong>DS-X</strong>=true</li>
					<li>Any dataset tagged with <strong>POA</strong>=true</li>
					<li>Any dataset tagged with <strong>HMB</strong>=true</li>			
				</ul>	
			</td>	
			<td>
				<ul>
					<li>If the dataset's Primary DUO terms is GRU, DS, HMB, POA, and the DAR's Primary DUO term (aka Permission) is MDS then <strong>Approve</strong></li>
          <li>If the dataset's Primary DUO terms is NMDS, and the DAR's Primary DUO term (aka Permission) is MDS then <strong>Deny</strong></li>
				</ul>						
			</td>
		</tr>
		<tr>
			<td>Study population origins or ancestry (i.e. <strong>POA</strong>)</td>
			<td>
				<ul>
					<li>Any dataset tagged with <strong>GRU</strong>=true</li>
          <li>Any dataset tagged with <strong>POA</strong>=true</li>
				</ul>
			</td>
			<td>
				<ul>
					<li>If the dataset's Primary DUO terms is GRU, DS, HMB, POA, and the DAR's Primary DUO term (aka Permission) is MDS then <strong>Approve</strong></li>
				</ul>
			</td>
		</tr>
		<tr>
			<td>Commercial purpose/by a commercial entity</td>
			<td>
				<ul>
					<li>Any dataset where <strong>NPU</strong> and <strong>NCU</strong> are both false</li>
				</ul>			
			</td>
			<td>
				<ul>
					<li>If the DAR proports Commercial use, and the dataset is limited to Non-profit use, then <strong>Deny</strong></li>
				</ul>			
			</td>
		</tr>
		<tr>
			<td>Use of data is limited to health/medical/biomedical purposes, not including population origins or ancestry (i.e. <strong>HMB</strong>)</td>
			<td>
				<ul>
					<li>Any dataset tagged with <strong>GRU</strong>=true</li>
					<li>Any dataset tagged with <strong>HMB</strong>=true</li>
				</ul>
			</td>
			<td>
				<ul>
					<li>If the dataset's Primary DUO terms is HMB, and the DAR's Primary DUO term (aka Permission) is HMB, MDS then <strong>Approve</strong></li>
				</ul>
			</td>
		</tr>
	</tbody>
</table>

### Abstain from Decision

Describes the involvement of studies that <strong>will not</strong> result in a decision from the DUOS algorithm.

<table>
	<thead>
		<tr>
			<th>If the DAR aims to involve...</th>
			<th>Abstain due to</th>
      <th>Requirements</th>
		</tr>
	</thead>
	<tbody>
    <tr>
			<td>
				<ul>
          <li>Other</li>
        </ul>	
			</td>
      <td>
        <li>DAR cannot be described in DUO</li>
      </td>
      <td>
        <li>NA</li>
      </td>
    </tr>
    <tr>
			<td>
				<ul>
          <li>GS-</li>
          <li>MOR</li>
        </ul>	
			</td>
      <td>
        <li>GS-, MOR are poorly defined and infrequently (never). More detail/data needed</li>
      </td>
      <td>
        <li>No requirement in DAR or Algorithm</li>
      </td>
    </tr>
    <tr>
			<td>
				<ul>
          <li>GSO</li>
          <li>PUB</li>
        </ul>	
			</td>
      <td>
        <li>Not used in algorithmic decision-making</li>
      </td>
      <td>
        <li>Assent in the DAR form</li>
      </td>
    </tr>
    <tr>
			<td>
				<ul>
          <li>COL</li>
          <li>IRB</li>
        </ul>	
			</td>
      <td>
        <li>DAR includes uploaded documents</li>
        <li>Upload in the DAR form results in trigger a bypass of the algorithm</li>
      </td>
      <td>
        <li>Manual review of documents</li>
      </td>
    </tr>
    <tr>
			<td>
				<ul>
					<li>Limitation to one gender</li>
          <li>Restricted to a pediatric population (under the age of 18)</li>
					<li>Illegal behaviors (violence, domestic abuse, prostitution, sexual victimization)</li>
          <li>Alcohol or drug abuse, or abuse of other addictive products</li>
          <li>Sexual preferences or sexually transmitted diseases</li>
          <li>Any stigmatizing illnesses</li>
          <li>Vulnerable populations as defined in 456 CFR (children, prisoners, pregnant women, mentally disabled persons, or ["SIGNIFICANTLY"] economically or educationally disadvantaged persons)</li>
          <li>Population Origins/Migration patterns? (Answer is pre-determined based on selections made in Step 2)</li>
          <li>Psychological traits, including intelligence, attention, emotion</li>
          <li>Ethnicity, race, or gender with genotypic or other phenotypic variables, for purposes beyond biomedical or health-related research, or in ways that are not easily related to Health?</li>
				</ul>	
			</td>
      <td>
        <li>Ethical considerations</li>
      </td>
      <td>
        <li>NA</li>
      </td>
    </tr>
  <tbody>
<table>
