package app.munch.exception;

import app.munch.model.PlaceLockingType;
import dev.fuxing.err.ExceptionParser;
import dev.fuxing.err.TransportException;

/**
 * @author Fuxing Loh
 * @since 2019-10-25 at 16:16
 */
public final class EditLockedException extends TransportException {
    static {
        ExceptionParser.register(EditLockedException.class, EditLockedException::new);
    }

    EditLockedException(TransportException e) {
        super(e);
    }

    public EditLockedException(PlaceLockingType type) {
        super(409, EditLockedException.class, "The data you are attempting to edit is locked by: " + type.name() + ".");
    }
}
