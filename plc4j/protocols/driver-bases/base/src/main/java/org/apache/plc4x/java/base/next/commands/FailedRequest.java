package org.apache.plc4x.java.base.next.commands;

public class FailedRequest extends Response{

    private final Throwable cause;

    protected FailedRequest(int transactionId, Throwable cause) {
        super(transactionId);
        this.cause = cause;
    }

    public Throwable getCause() {
        return cause;
    }
}
