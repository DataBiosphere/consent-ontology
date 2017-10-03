package org.broadinstitute.dsde.consent.ontology.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.Appender;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.logging.AppenderFactory;
import io.dropwizard.logging.async.AsyncAppenderFactory;
import io.dropwizard.logging.filter.LevelFilterFactory;
import io.dropwizard.logging.layout.LayoutFactory;
import io.sentry.logback.SentryAppender;

import javax.validation.constraints.NotNull;

@JsonTypeName("sentry")
public class SentryLogger implements AppenderFactory {

    private String appenderName = "sentry";

    @NotNull
    protected Level threshold = Level.WARN;

    @JsonProperty
    public String getName() {
        return this.appenderName;
    }

    @JsonProperty
    public void setName(String name) {
        this.appenderName = name;
    }

    @JsonProperty
    public Level getThreshold() {
        return this.threshold;
    }

    @JsonProperty
    public void setThreshold(Level threshold) {
        this.threshold = threshold;
    }


    @Override
    public Appender build(LoggerContext context, String applicationName, LayoutFactory layoutFactory, LevelFilterFactory levelFilterFactory, AsyncAppenderFactory asyncAppenderFactory) {
        final SentryAppender appender = new SentryAppender();
        appender.setName(getName());
        appender.setContext(context);
        return appender;
    }

}