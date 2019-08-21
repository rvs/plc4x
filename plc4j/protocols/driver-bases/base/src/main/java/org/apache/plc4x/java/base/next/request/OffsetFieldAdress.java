package org.apache.plc4x.java.base.next.request;

import java.util.Objects;

/**
 * The simplest Implementation of an Untyped {@link FieldAdress} based on an integer offset.
 */
public class OffsetFieldAdress implements FieldAdress {

    private final int offset;
    private final int length;

    public OffsetFieldAdress(int offset, int length) {
        this.offset = offset;
        this.length = length;
    }

    public int getOffset() {
        return offset;
    }

    public int getLength() {
        return length;
    }

    @Override public int compareTo(FieldAdress o) {
        assert o instanceof OffsetFieldAdress;
        return Integer.compare(offset, ((OffsetFieldAdress) o).offset);
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OffsetFieldAdress that = (OffsetFieldAdress) o;
        return offset == that.offset;
    }

    @Override public int hashCode() {
        return Objects.hash(offset);
    }

    @Override public String toString() {
        return "OffsetFieldAdress{" +
            "offset=" + offset +
            '}';
    }
}
