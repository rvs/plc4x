package org.apache.plc4x.java.base.next.optimizer;

import org.apache.commons.lang3.tuple.Pair;

/**
 * This is a Rule which defines how to merge twp instances of a {@link FieldRequest}
 * and returns the merged read, if possible.
 * It furthermore returns a {@link InverseTransformation} which has to be applied
 * to the response.
 *
 * @param <REQ> Request Type
 * @param <RES> Response Type
 */
public interface MergeRule<REQ extends FieldRequest, RES extends FieldResponse> {

    /** Short description */
    String description();

    /** Checks if the rule can be applied to two specific requests */
    boolean matches(REQ field1, REQ field2);

    /** Applies the transformation */
    Pair<REQ, InverseTransformation<RES>> apply(REQ field1, REQ field2);
}
