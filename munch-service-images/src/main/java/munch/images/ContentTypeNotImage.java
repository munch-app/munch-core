package munch.images;

import munch.restful.server.exceptions.StructuredException;

/**
 * Created by: Fuxing
 * Date: 21/4/2017
 * Time: 3:42 PM
 * Project: munch-core
 */
public class ContentTypeNotImage extends StructuredException {

    protected ContentTypeNotImage(String contentType) {
        super("ContentTypeNotImage", "Uploaded content type must be image. (" + contentType + ")", 400);
    }

}
