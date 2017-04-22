package munch.places.menu;

import munch.restful.server.exceptions.StructuredException;

/**
 * Created by: Fuxing
 * Date: 21/4/2017
 * Time: 3:42 PM
 * Project: munch-core
 */
public class ContentTypeNotMatchException extends StructuredException {

    protected ContentTypeNotMatchException(String contentType) {
        super("ContentTypeNotMatchException", "Uploaded content type must be image. (" + contentType + ")", 400);
    }

}
