package app.munch.image;

import dev.fuxing.err.ExceptionParser;
import dev.fuxing.err.TransportException;

/**
 * Content type not image exception
 */
public final class InvalidFileException extends TransportException {
    static {
        ExceptionParser.register(InvalidFileException.class, InvalidFileException::new);
    }

    InvalidFileException(TransportException e) {
        super(e);
    }

    public InvalidFileException(String message) {
        super(400, InvalidFileException.class, message);
    }
}
