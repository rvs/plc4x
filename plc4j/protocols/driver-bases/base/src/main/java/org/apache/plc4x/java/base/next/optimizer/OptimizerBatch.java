package org.apache.plc4x.java.base.next.optimizer;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/** This is a batch of the optimization process */
class OptimizerBatch<REQ extends FieldRequest, RES extends FieldResponse> {

    private static final Logger logger = LoggerFactory.getLogger(OptimizerBatch.class);

    private final List<REQ> requests;
    private final Queue<InverseTransformation<RES>> transformations;

    OptimizerBatch(List<REQ> requests, Queue<InverseTransformation<RES>> transformations) {
        this.requests = requests;
        this.transformations = transformations;
    }

    public List<REQ> getRequests() {
        return requests;
    }

    public Queue<InverseTransformation<RES>> getTransformations() {
        return transformations;
    }

    /** Applies a Rule to two items of this Batch and returns a new Optimizer Batch */
    OptimizerBatch<REQ, RES> applyRule(MergeRule<REQ, RES> rule, int index1, int index2) {
        assert index1 < requests.size();
        assert index2 < requests.size();

        final ArrayList<REQ> tmp = new ArrayList<>(requests);

        final REQ first = tmp.get(index1);
        final REQ second = tmp.get(index2);

        tmp.remove(first);
        tmp.remove(second);

        final Pair<REQ, InverseTransformation<RES>> res
            = rule.apply(first, second);

        logger.debug("Result of merging {} and {} is {}", first, second, res.getLeft());

        tmp.add(res.getLeft());

        // Create a new Batch
        final Queue<InverseTransformation<RES>> queue
            = new LinkedBlockingQueue<>(getTransformations());
        queue.add(res.getRight());

        // Add Batches
        return new OptimizerBatch<>(tmp, queue);
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OptimizerBatch<?, ?> that = (OptimizerBatch<?, ?>) o;
        return Objects.equals(requests, that.requests);
    }

    @Override public int hashCode() {
        return Objects.hash(requests);
    }

    @Override public String toString() {
        return "OptimizerBatch{" +
            "requests=" + requests +
            '}';
    }
}
