package org.apache.plc4x.java.base.next.commands;

public abstract class Message {

    private final int transactionId;

    protected Message(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getTransactionId() {
        return transactionId;
    }
}
