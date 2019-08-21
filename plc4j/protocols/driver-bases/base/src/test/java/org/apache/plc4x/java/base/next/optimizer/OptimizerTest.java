package org.apache.plc4x.java.base.next.optimizer;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class OptimizerTest {

    /**
     * 10 / 10
     * 20 / 5
     * 15 / 5
     * --
     * 10 / 10
     * 20 / 5
     * --
     * 10 / 10
     * 15 / 10
     */
    @Test
    public void optimizerTest() {
        final List<MergeRule<TestRequest, FieldResponse>> rules = Arrays.asList(new TestMergeRule());
        final List<TestRequest> requests = Arrays.asList(
            new TestRequest(0, 1),
            new TestRequest(1, 1),
            new TestRequest(2, 1),
            new TestRequest(3, 1),
            new TestRequest(4, 1),
            new TestRequest(5, 1),
            new TestRequest(6, 1),
            new TestRequest(7, 1)
//            new TestRequest(16, 16),
//            new TestRequest(32, 16)
        );
        final int bytesTotal = calculateSize(requests);
        final Optimizer<TestRequest, FieldResponse> optimizer
            = new Optimizer<>(rules);
        final Set<OptimizerBatch<TestRequest, FieldResponse>> batches = optimizer.optimize(requests);

        System.out.println(batches.size() + " Results have been found:");
        final ArrayList<OptimizerBatch<TestRequest, FieldResponse>> printBatches = new ArrayList<>(batches);
        printBatches.sort(Comparator.comparingInt(b -> calculateSize(b.getRequests())));
        for (OptimizerBatch<TestRequest, FieldResponse> batch : printBatches) {
            final int bytesOptimized = calculateSize(batch.getRequests());
            System.out.println(String.format(" - %.2f - %d - %s", (double)bytesOptimized/bytesTotal, batch.getRequests().size(), batch.getRequests()));
        }
    }

    private static int calculateSize(List<TestRequest> requests) {
        return requests.stream().map(req -> Math.max(req.length, 8)).mapToInt(value -> value).sum();
    }


    public static class TestRequest extends FieldRequest {

        private final int offset;
        private final int length;

        public TestRequest(int offset, int length) {
            this.offset = offset;
            this.length = length;
        }

        public int getOffset() {
            return offset;
        }

        public int getLength() {
            return length;
        }

        @Override public int compareTo(FieldRequest o) {
            assert o instanceof TestRequest;
            // return Integer.compare(offset, ((TestRequest) o).offset);
            return 0;
        }

        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestRequest that = (TestRequest) o;
            return offset == that.offset &&
                length == that.length;
        }

        @Override public int hashCode() {
            return Objects.hash(offset, length);
        }

        @Override public String toString() {
            return "TestRequest{" +
                "offset=" + offset +
                ", length=" + length +
                '}';
        }
    }

    private static class TestMergeRule implements MergeRule<TestRequest, FieldResponse> {

        @Override public String description() {
            return "simple-merge";
        }

        @Override public boolean matches(TestRequest field1, TestRequest field2) {
            return field1.getOffset() <= field2.getOffset();
        }

        @Override public Pair<TestRequest, InverseTransformation<FieldResponse>> apply(TestRequest field1, TestRequest field2) {
            final int offset = field1.offset;
            final int length = Math.max(field2.offset + field2.length, field1.offset + field1.length) - offset;
            final TestRequest request = new TestRequest(offset, length);
            final InverseTransformation transformation = response -> {
                // TODO
                return Pair.of(null, null);
            };
            return Pair.of(request, transformation);
        }
    }
}