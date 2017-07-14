package munch.images.exceptions;

import munch.restful.core.exception.StructuredException;

/**
 * Content type not image exception
 */
public final class NotImageException extends StructuredException {
    public NotImageException(String contentType) {
        super(400, "NotImageException", "Uploaded content type must be image. (" + contentType + ")");
    }
}
