package org.broadinstitute.dsde.consent.ontology.actor;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.codahale.metrics.annotation.Timed;
import org.broadinstitute.dsde.consent.ontology.cache.OntModelCache;

@SuppressWarnings("WeakerAccess")
public class MatchWorkerActor extends UntypedActor {

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private OntModelCache ontModelCache = OntModelCache.INSTANCE;

    @Override
    @Timed(name = "onReceive")
    public void onReceive(Object msg) throws Exception {
        log.debug("Received Event: " + msg);
        if (msg instanceof MatchWorkerMessage) {
            Boolean match = ontModelCache.matchPurpose((MatchWorkerMessage) msg);
            log.debug("Received match result: " + match);
            getSender().tell(match, getSelf());
        } else {
            log.error("Received unknown message: " + msg);
        }

    }

}
