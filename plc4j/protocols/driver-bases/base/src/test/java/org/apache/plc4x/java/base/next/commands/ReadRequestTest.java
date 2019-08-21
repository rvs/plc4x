package org.apache.plc4x.java.base.next.commands;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.plc4x.java.base.next.request.DataType;
import org.apache.plc4x.java.base.next.request.FieldAdress;
import org.apache.plc4x.java.base.next.request.FieldRead;
import org.apache.plc4x.java.base.next.request.OffsetFieldAdress;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;

public class ReadRequestTest {

    private Queue<Function<FieldRead, FieldRead>> queue = new LinkedBlockingQueue<>();

    @Test
    public void optimizerTests() {
        List<Function<Pair<FieldRead, FieldRead>, FieldRead>> operations = new ArrayList<>();
        List<Function<FieldRead, Pair<FieldRead, FieldRead>>> inverse = new ArrayList<>();

        operations.add(new Function<Pair<FieldRead, FieldRead>, FieldRead>() {

            @Override public FieldRead apply(Pair<FieldRead, FieldRead> fieldReadFieldReadPair) {
                final FieldAdress a1 = fieldReadFieldReadPair.getLeft().getAdress();
                final FieldAdress a2 = fieldReadFieldReadPair.getRight().getAdress();
                assert a1 instanceof OffsetFieldAdress;
                assert a2 instanceof OffsetFieldAdress;
                final int start = ((OffsetFieldAdress) a1).getOffset();

                // Register the inverse function, also
                final Function<FieldRead, Pair<FieldRead, FieldRead>> inverse = new Function<FieldRead, Pair<FieldRead, FieldRead>>() {

                    @Override public Pair<FieldRead, FieldRead> apply(FieldRead fieldRead) {
                        // Result
                        // 0, ((OffsetFieldAdress) a1).getLength();
                        // ((OffsetFieldAdress) a2).getOffset(), ((OffsetFieldAdress) a2).getLength()
                        return null;
                    }

                };

                return null;
            }

        });

        // Read request
        final ReadRequest readRequest = new ReadRequest(0, Arrays.asList(
            new FieldRead(new OffsetFieldAdress(200, 10), DataType.BOOLEAN),
            new FieldRead(new OffsetFieldAdress(100, 10), DataType.BOOLEAN)
        ));

        // First, Unroll
        final List<FieldRead> fields = readRequest.getFieldReads();
        fields.sort(null);

        // Now try to merge. If we try all combinations this is memory O(n!)
        // So we have to do it a bit different
        // First, merge 2
        // Then only keep more efficient ones and continue
        for (int i = 0; i < fields.size(); i++) {
            for (int j = i + 1; j < fields.size(); j++) {
                merge(fields, 0, 1);
            }
        }

    }

    /** Merge reads at o1 and o2 in the index */
    static List<FieldRead> merge(List<FieldRead> fields, int o1, int o2) {
        return Collections.emptyList();
    }


}