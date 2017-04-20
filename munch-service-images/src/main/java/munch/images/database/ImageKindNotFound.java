package munch.images.database;

import munch.restful.server.exceptions.StructuredException;

/**
 * Created By: Fuxing Loh
 * Date: 20/4/2017
 * Time: 10:50 PM
 * Project: munch-core
 */
public class ImageKindNotFound extends StructuredException {

    /**
     * Image kind not found
     *
     * @see ImageKind for all the options available
     */
    protected ImageKindNotFound(String kind) {
        super("ImageKindNotFound", "ImageKind: " + kind + " not found.", 400);
    }
}
