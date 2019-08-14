package app.munch.image;

import dev.fuxing.err.ExceptionParser;
import dev.fuxing.err.TransportException;

/**
 * Created By: Fuxing Loh
 * Date: 28/10/2016
 * Time: 8:10 PM
 * Project: v22-file
 */
public class ContentTypeException extends TransportException {
    static {
        ExceptionParser.register(ContentTypeException.class, ContentTypeException::new);
    }

    ContentTypeException(TransportException e) {
        super(e);
    }

    public ContentTypeException(Throwable cause) {
        super(500, ContentTypeException.class, cause.getMessage(), cause);
    }
}
