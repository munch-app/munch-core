package app.munch.exception;

import app.munch.model.PlaceLockingType;
import dev.fuxing.err.ExceptionParser;
import dev.fuxing.err.TransportException;

/**
 * Created by: Fuxing
 * Date: 2/9/19
 * Time: 12:37 pm
 */
public final class PlaceLockedException extends TransportException {
    static {
        ExceptionParser.register(PlaceLockedException.class, PlaceLockedException::new);
    }

    PlaceLockedException(TransportException e) {
        super(e);
    }

    public PlaceLockedException(PlaceLockingType type) {
        super(409, PlaceLockedException.class, "The place you are currently editing is locked by: " + type.name() + ".");
    }
}
