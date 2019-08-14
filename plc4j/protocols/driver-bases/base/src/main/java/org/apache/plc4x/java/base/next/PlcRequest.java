package org.apache.plc4x.java.base.next;

import java.util.List;

public interface PlcRequest {

    List<String> getRequestItems();

    ResponseCode getResponseCode();

}
