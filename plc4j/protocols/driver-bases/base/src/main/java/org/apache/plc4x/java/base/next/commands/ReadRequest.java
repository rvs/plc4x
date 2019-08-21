package org.apache.plc4x.java.base.next.commands;

import org.apache.plc4x.java.base.next.request.FieldRead;

import java.util.List;

public class ReadRequest extends Message {

    private final List<FieldRead> fieldReads;

    public ReadRequest(int transactionId, List<FieldRead> fieldReads) {
        super(transactionId);
        this.fieldReads = fieldReads;
    }

    public List<FieldRead> getFieldReads() {
        return fieldReads;
    }

    @Override public String toString() {
        return super.toString();
    }
}
