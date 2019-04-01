package org.broadinstitute.dsde.consent.ontology;

import org.mockserver.integration.ClientAndServer;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;

public interface WithMockServer {

    default ClientAndServer startMockServer(int port) {
        // Force mock server logging
        ((ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory
                .getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME))
                .setLevel(ch.qos.logback.classic.Level.OFF);
        ((ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory
                .getLogger("org.mockserver"))
                .setLevel(ch.qos.logback.classic.Level.OFF);
        return startClientAndServer(port);
    }

}
