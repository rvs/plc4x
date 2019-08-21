package org.apache.plc4x.java.base.next.optimizer;

import java.util.List;
import java.util.Queue;

/**
 * One Batch of Requests.
 */
public class RequestBatch {

    private final List<FieldRequest> requests;
    private final Queue<InverseTransformation> transformations;

    public RequestBatch(List<FieldRequest> requests, Queue<InverseTransformation> transformations) {
        this.requests = requests;
        this.transformations = transformations;
    }

    public List<FieldRequest> getRequests() {
        return requests;
    }

    public Queue<InverseTransformation> getTransformations() {
        return transformations;
    }
}
