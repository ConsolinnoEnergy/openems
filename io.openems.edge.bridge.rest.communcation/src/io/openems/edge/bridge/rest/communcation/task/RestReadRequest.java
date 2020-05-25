package io.openems.edge.bridge.rest.communcation.task;

public interface RestReadRequest extends RestRequest {

    /**
     * Called by the Rest Bridge sets answer after successful REST Communication.
     *
     * @param succ   declares successful communication.
     * @param answer the REST Response from the GET Method.
     */

    void setResponse(boolean succ, String answer);
}
