package munch.images.exceptions;

import munch.restful.core.exception.StructuredException;

/**
 * Created By: Fuxing Loh
 * Date: 16/6/2017
 * Time: 5:02 PM
 * Project: munch-core
 */
public final class ThumborNotAvailable extends StructuredException {

    public ThumborNotAvailable() {
        super(501, "ThumborNotAvailable", "Required thumbor instance not available.");
    }
}
