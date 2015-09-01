package org.broadinstitute.dsde.consent.ontology.actor;

public class Message<T> {
    private final T messageData;

    public Message(T messageData) {
        this.messageData = messageData;
    }

    public T getMessageData() {
        return messageData;
    }

}
