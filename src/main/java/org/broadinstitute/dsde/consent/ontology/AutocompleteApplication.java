package org.broadinstitute.dsde.consent.ontology;

import com.hubspot.dropwizard.guice.GuiceBundle;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import org.broadinstitute.dsde.consent.ontology.resources.AllTermsResource;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.broadinstitute.dsde.consent.ontology.resources.TranslateResource;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import java.util.EnumSet;


/**
 * Top-level entry point to the entire application.
 *
 * See the Dropwizard docs here:
 *   https://dropwizard.github.io/dropwizard/manual/core.html
 *
 */
public class AutocompleteApplication extends Application<AutocompleteConfiguration> {

    public static void main(String[] args) throws Exception {
        new AutocompleteApplication().run(args);
    }

    public void run(AutocompleteConfiguration config, Environment env) {
        env.jersey().register(AllTermsResource.class);
        env.jersey().register(TranslateResource.class);

        // support for cross-origin ajax calls to the autocomplete service
        FilterRegistration.Dynamic corsFilter = env.servlets().addFilter("CORS", CrossOriginFilter.class);
        corsFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/autocomplete");
        corsFilter.setInitParameter("allowedOrigins", config.getCorsConfiguration().allowedDomains);
        corsFilter.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        corsFilter.setInitParameter("allowedMethods", "GET");

    }

    public void initialize(Bootstrap<AutocompleteConfiguration> bootstrap) {

        GuiceBundle<AutocompleteConfiguration> guiceBundle = GuiceBundle.<AutocompleteConfiguration>newBuilder()
                .addModule(new AutocompleteModule())
                .setConfigClass(AutocompleteConfiguration.class)
                .build();

        bootstrap.addBundle(guiceBundle);

        bootstrap.addBundle(new AssetsBundle("/assets/", "/site"));
    }
}
