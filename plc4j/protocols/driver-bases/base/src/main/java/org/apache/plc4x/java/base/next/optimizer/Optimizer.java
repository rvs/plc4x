package org.apache.plc4x.java.base.next.optimizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Generic Optimizer.
 *
 * @param <REQ> Type of Requests
 * @param <RES> Type of Results
 */
public class Optimizer<REQ extends FieldRequest, RES extends FieldResponse> {

    private static final Logger logger = LoggerFactory.getLogger(Optimizer.class);

    private final List<MergeRule<REQ, RES>> rules;

    public Optimizer(List<MergeRule<REQ, RES>> rules) {
        this.rules = rules;
    }

    /**
     * Split the Field Requests into one or more Batch Requests.
     */
    public Set<OptimizerBatch<REQ, RES>> optimize(List<REQ> requests) {
        // Iterate and merge
        final Queue<InverseTransformation<RES>> transformations
            = new LinkedBlockingQueue<>();

        // Initial Batch
        final OptimizerBatch<REQ, RES> initialBatch = new OptimizerBatch<>(requests, transformations);

        // Create the initial set
        Set<OptimizerBatch<REQ, RES>> batches = new HashSet<>();
        batches.add(initialBatch);

        // Process all Batches
        int sizeBefore;
        do {
            sizeBefore = batches.size();
            iterate(batches);
        } while (sizeBefore != batches.size());
        return batches;
    }

    private void iterate(Set<OptimizerBatch<REQ, RES>> batches) {
        logger.debug("Starting iteration with {} batches", batches.size());
        // Defensive copy to not mess with iteration
        final Set<OptimizerBatch<REQ, RES>> initial = new HashSet<>(batches);
        for (OptimizerBatch<REQ, RES> batch : initial) {
            final List requestList = batch.getRequests();
            // Order the requests
            for (int i = 0; i < requestList.size(); i++) {
                for (int j = 0; j < requestList.size(); j++) {
                    if (i == j) {
                        continue;
                    }
                    // Create a new Batch
                    for (MergeRule<REQ, RES> rule : rules) {
                        logger.debug("Trying to merge {} and {} with rule '{}'", i, j, rule.description());
                        // Apply the Rule
                        final boolean matches = rule.matches(
                            batch.getRequests().get(i),
                            batch.getRequests().get(j)
                        );
                        if (matches) {
                            logger.debug("Rule {} matches for {} and {}", rule.description(), requestList.get(i), requestList.get(j));
                            batches.add(batch.applyRule(rule, i, j));
                        } else {
                            logger.debug("Rule {} does not match for {} and {}", rule.description(), requestList.get(i), requestList.get(j));
                        }
                    }
                }
            }
        }
        logger.debug("Finishing iteration with {} batches", batches.size());
    }


}
