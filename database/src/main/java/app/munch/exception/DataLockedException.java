package app.munch.exception;

import app.munch.model.PlaceLockingType;
import dev.fuxing.err.ExceptionParser;
import dev.fuxing.err.TransportException;

/**
 * @author Fuxing Loh
 * @since 2019-10-25 at 16:16
 */
public final class DataLockedException extends TransportException {
    static {
        ExceptionParser.register(DataLockedException.class, DataLockedException::new);
    }

    DataLockedException(TransportException e) {
        super(e);
    }

    public DataLockedException(PlaceLockingType type) {
        super(409, DataLockedException.class, "The data you are attempting to edit is locked by: " + type.name() + ".");
    }
}
