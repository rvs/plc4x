package org.apache.plc4x.java.base.next;

import org.apache.plc4x.java.base.next.commands.Message;

import java.util.concurrent.CompletableFuture;

/**
 * This is a Container object which holds information for in-flight requests.
 * It also tracks the lifecycle of each request.
 */
public class RequestInformation {

    private final int transactionId;
    private final CompletableFuture<PlcResponse> future;
    private Status status;
    private Message request;
    private Message response;
    private Throwable cause;

    public RequestInformation(int transactionId) {
        this.transactionId = transactionId;
        future = new CompletableFuture<>();
    }

    public void setRequest(Message request) {
        this.request = request;
    }

    public void setResponse(Message response) {
        this.response = response;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    public Message getRequest() {
        return request;
    }

    public Message getResponse() {
        return response;
    }

    public Throwable getCause() {
        return cause;
    }

    public CompletableFuture<PlcResponse> getFuture() {
        return future;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    enum Status {
        PREPARING,
        WAIT_TO_SEND,
        SENT,
        RESPONDED,
        FINISHED,
        FAILED
    }
}
