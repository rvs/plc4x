package org.apache.plc4x.java.base.next.commands;

public class ReadRequest extends Message {

    private final String query;

    public ReadRequest(int transactionId, String query) {
        super(transactionId);
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    @Override public String toString() {
        return super.toString();
    }
}
