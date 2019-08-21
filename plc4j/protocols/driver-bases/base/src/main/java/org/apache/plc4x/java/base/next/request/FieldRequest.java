package org.apache.plc4x.java.base.next.request;

public abstract class FieldRequest {

    private final FieldAdress adress;

    protected FieldRequest(FieldAdress adress) {
        this.adress = adress;
    }

    public FieldAdress getAdress() {
        return adress;
    }
}
