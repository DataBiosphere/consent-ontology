package org.broadinstitute.dsde.consent.ontology;

import com.hubspot.dropwizard.guice.GuiceBundle;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import java.util.EnumSet;

/**
 * Top-level entry point to the entire application.
 *
 * See the Dropwizard docs here:
 * https://dropwizard.github.io/dropwizard/manual/core.html
 *
 */
public class OntologyApp extends Application<OntologyConfiguration> {

    private GuiceBundle<OntologyConfiguration> guiceBundle;

    public static void main(String[] args) throws Exception {
        new OntologyApp().run(args);
    }

    @Override
    public void run(OntologyConfiguration config, Environment env) {
//        final ActorSystem actorSystem = ActorSystem.create("actorSystem");
//        final ActorRef ontologyMatchingActor = actorSystem.actorOf(Props.create(OntologyMatchingActor.class), "OntologyMatchingActor");
//        env.jersey().register(new MatchResource(ontologyMatchingActor));
//        env.jersey().register(AllTermsResource.class);
//        env.jersey().register(TranslateResource.class);

        // support for cross-origin ajax calls to the autocomplete service
        FilterRegistration.Dynamic corsFilter = env.servlets().addFilter("CORS", CrossOriginFilter.class);
        corsFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, env.getApplicationContext().getContextPath() + "/autocomplete");
        corsFilter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,OPTIONS");
        corsFilter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        corsFilter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        corsFilter.setInitParameter("allowedHeaders", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");

    }

    @Override
    public void initialize(Bootstrap<OntologyConfiguration> bootstrap) {

        guiceBundle = GuiceBundle.<OntologyConfiguration>newBuilder()
                .addModule(new OntologyModule())
                .enableAutoConfig(this.getClass().getPackage().getName())
                .setConfigClass(OntologyConfiguration.class)
                .build();

        bootstrap.addBundle(guiceBundle);

        bootstrap.addBundle(new AssetsBundle("/assets/", "/site"));
    }

}
