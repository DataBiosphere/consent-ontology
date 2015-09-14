package org.broadinstitute.dsde.consent.ontology;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.hubspot.dropwizard.guice.GuiceBundle;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import org.broadinstitute.dsde.consent.ontology.actor.OntologyMatchingActor;
import org.broadinstitute.dsde.consent.ontology.resources.AllTermsResource;
import org.broadinstitute.dsde.consent.ontology.resources.MatchResource;
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
public class OntologyApp extends Application<OntologyConfiguration> {

    public static void main(String[] args) throws Exception {
        new OntologyApp().run(args);
    }

    public void run(OntologyConfiguration config, Environment env) {
        final ActorSystem actorSystem = ActorSystem.create("actorSystem");
        final ActorRef ontologyMatchingActor =
            actorSystem.actorOf(Props.create(OntologyMatchingActor.class), "OntologyMatchingActor");
        env.jersey().register(new MatchResource(ontologyMatchingActor));
        env.jersey().register(AllTermsResource.class);
        env.jersey().register(TranslateResource.class);

        // support for cross-origin ajax calls to the autocomplete service
        FilterRegistration.Dynamic corsFilter = env.servlets().addFilter("CORS", CrossOriginFilter.class);
        corsFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/autocomplete");
//        corsFilter.setInitParameter("allowedOrigins", config.getCorsConfiguration().allowedDomains);
        corsFilter.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        corsFilter.setInitParameter("allowedMethods", "GET");

    }

    public void initialize(Bootstrap<OntologyConfiguration> bootstrap) {

        GuiceBundle<OntologyConfiguration> guiceBundle = GuiceBundle.<OntologyConfiguration>newBuilder()
                .addModule(new OntologyModule())
                .setConfigClass(OntologyConfiguration.class)
                .build();

        bootstrap.addBundle(guiceBundle);

        bootstrap.addBundle(new AssetsBundle("/assets/", "/site"));
    }
}
