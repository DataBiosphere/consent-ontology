package org.broadinstitute.dsp.ontology.performance.parsers;

public record LogEntry(String title, Long start, Long finish, String result, String errorMessage) {}
