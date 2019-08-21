package org.apache.plc4x.java.base.next.request;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OffsetFieldAdressTest {

    @Test
    public void compareTo() {
        final OffsetFieldAdress o1 = new OffsetFieldAdress(10, 2);
        final OffsetFieldAdress o2 = new OffsetFieldAdress(20, 2);

        assertTrue(o1.compareTo(o2) < 0);
    }

    @Test
    public void sort() {
        final OffsetFieldAdress o1 = new OffsetFieldAdress(10, 2);
        final OffsetFieldAdress o2 = new OffsetFieldAdress(20, 2);

        final List<OffsetFieldAdress> list = Arrays.asList(o2, o1);
        list.sort(null);

        assertEquals(10, list.get(0).getOffset());
    }
}