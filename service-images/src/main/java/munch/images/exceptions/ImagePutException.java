package munch.images.exceptions;

import munch.restful.core.exception.StructuredException;

/**
 * Created by: Fuxing
 * Date: 14/7/2017
 * Time: 11:11 PM
 * Project: munch-core
 */
public class ImagePutException extends StructuredException {
    public ImagePutException(String message) {
        super(403, "ImagePutException", message);
    }
}
