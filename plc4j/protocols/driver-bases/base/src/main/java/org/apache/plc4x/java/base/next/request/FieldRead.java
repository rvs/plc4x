package org.apache.plc4x.java.base.next.request;


public class FieldRead extends FieldRequest {

    private final DataType dataType;

    public FieldRead(FieldAdress adress, DataType dataType) {
        super(adress);
        this.dataType = dataType;
    }
}
