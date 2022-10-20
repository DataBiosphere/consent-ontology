package org.broadinstitute.dsde.consent.ontology;

import com.google.common.util.concurrent.UncaughtExceptionHandlers;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.sentry.Sentry;
import io.sentry.SentryLevel;
import java.util.EnumSet;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import org.apache.commons.lang3.StringUtils;
import org.broadinstitute.dsde.consent.ontology.cloudstore.GCSHealthCheck;
import org.broadinstitute.dsde.consent.ontology.filters.ResponseServerFilter;
import org.broadinstitute.dsde.consent.ontology.resources.AutocompleteResource;
import org.broadinstitute.dsde.consent.ontology.resources.DataUseResource;
import org.broadinstitute.dsde.consent.ontology.resources.LivenessResource;
import org.broadinstitute.dsde.consent.ontology.resources.MatchResource;
import org.broadinstitute.dsde.consent.ontology.resources.OntologySearchResource;
import org.broadinstitute.dsde.consent.ontology.resources.StatusResource;
import org.broadinstitute.dsde.consent.ontology.resources.SwaggerResource;
import org.broadinstitute.dsde.consent.ontology.resources.TranslateResource;
import org.broadinstitute.dsde.consent.ontology.resources.ValidationResource;
import org.broadinstitute.dsde.consent.ontology.resources.VersionResource;
import org.broadinstitute.dsde.consent.ontology.service.ElasticSearchHealthCheck;
import org.eclipse.jetty.servlets.CrossOriginFilter;

/**
 * Top-level entry point to the entire application.
 * <p>
 * See the Dropwizard docs here:
 * <a href="https://www.dropwizard.io/">...</a>
 */
public class OntologyApp extends Application<OntologyConfiguration> {

    public static void main(String[] args) throws Exception {
        String dsn = System.getProperties().getProperty("sentry.dsn");
        if (StringUtils.isNotBlank(dsn)) {
            Sentry.init(config -> {
              config.setDsn(dsn);
              config.setDiagnosticLevel(SentryLevel.ERROR);
              config.setServerName("Ontology");
              config.addContextTag("Ontology");
              config.addInAppInclude("org.broadinstitute");
              config.addInAppExclude("org.mindswap");
              config.addInAppExclude("org.semanticweb");
              config.addInAppExclude("com.hp");
            });
            Thread.currentThread().setUncaughtExceptionHandler(UncaughtExceptionHandlers.systemExit());
        }
        new OntologyApp().run(args);
    }

    @Override
    public void run(OntologyConfiguration config, Environment env) {

        Injector injector = Guice.createInjector(new OntologyModule(config, env));
        env.jersey().register(ResponseServerFilter.class);
        env.jersey().register(injector.getInstance(AutocompleteResource.class));
        env.jersey().register(injector.getInstance(MatchResource.class));
        env.jersey().register(injector.getInstance(TranslateResource.class));
        env.jersey().register(injector.getInstance(ValidationResource.class));
        env.jersey().register(injector.getInstance(OntologySearchResource.class));
        env.jersey().register(injector.getInstance(DataUseResource.class));
        env.jersey().register(injector.getInstance(SwaggerResource.class));
        env.jersey().register(injector.getInstance(VersionResource.class));
        env.jersey().register(new LivenessResource());

        env.healthChecks().register(ElasticSearchHealthCheck.NAME, injector.getInstance(ElasticSearchHealthCheck.class));
        env.healthChecks().register(GCSHealthCheck.NAME, injector.getInstance(GCSHealthCheck.class));
        env.jersey().register(injector.getInstance(StatusResource.class));

        FilterRegistration.Dynamic corsFilter = env.servlets().addFilter("CORS", CrossOriginFilter.class);
        corsFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, env.getApplicationContext().getContextPath() + "/autocomplete");
        corsFilter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,OPTIONS");
        corsFilter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        corsFilter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        corsFilter.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,Pragma,Cache-Control,X-App-ID");

    }

    @Override
    public void initialize(Bootstrap<OntologyConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets/", "/api-docs", "index.html"));
    }

}
