package munch.articles;

import munch.restful.server.exceptions.StructuredException;

/**
 * Created By: Fuxing Loh
 * Date: 2/6/2017
 * Time: 2:39 PM
 * Project: munch-core
 */
public final class ImagePutException extends StructuredException {
    public ImagePutException(String imageUrl) {
        super("ImagePutException", "Unable to put image for url: " + imageUrl, 500);
    }
}
