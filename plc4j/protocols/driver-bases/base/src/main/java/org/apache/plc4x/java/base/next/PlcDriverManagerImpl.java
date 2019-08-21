package org.apache.plc4x.java.base.next;

import org.apache.plc4x.java.api.exceptions.PlcConnectionException;
import org.apache.plc4x.java.api.exceptions.PlcRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.Future;

public class PlcDriverManagerImpl implements PlcDriverManager {

    private static final Logger logger = LoggerFactory.getLogger(PlcDriverManagerImpl.class);
    
    private final Collection<PlcDriver> regisgtered;

    public PlcDriverManagerImpl() {
        // TODO Implement this
        regisgtered = null;
    }

    /** For testing **/
    PlcDriverManagerImpl(Collection<PlcDriver> regisgtered) {
        this.regisgtered = regisgtered;
    }

    @Override public Future<PlcConnection> connect(String connection) {
        return getDriver(connection)
            .map(drv -> {
                try {
                    return drv.connect(connection);
                } catch (PlcConnectionException e) {
                    throw new PlcRuntimeException(e);
                }
            })
            .orElseThrow(() -> new RuntimeException("No suitable driver found for connection ‘" + connection + "‘"));
    }

    @Override public Optional<PlcDriver> getDriver(String connection) {
        for (PlcDriver driver : regisgtered) {
            if (driver.accepts(connection)) {
                return Optional.of(driver);
            }
        }
        return Optional.empty();
    }
}
