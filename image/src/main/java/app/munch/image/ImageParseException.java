package app.munch.image;


import dev.fuxing.err.ExceptionParser;
import dev.fuxing.err.TransportException;

/**
 * Created by: Fuxing
 * Date: 27/7/18
 * Time: 1:44 AM
 * Project: v22-file
 */
public final class ImageParseException extends TransportException {
    static {
        ExceptionParser.register(ImageParseException.class, ImageParseException::new);
    }

    ImageParseException(TransportException e) {
        super(e);
    }

    public ImageParseException(String message) {
        super(500, ImageParseException.class, message);
    }
}
