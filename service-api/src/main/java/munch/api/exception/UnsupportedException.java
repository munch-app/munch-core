package munch.api.exception;

import munch.restful.core.exception.StructuredException;

/**
 * Created By: Fuxing Loh
 * Date: 23/7/2017
 * Time: 4:00 PM
 * Project: munch-core
 */
public class UnsupportedException extends StructuredException {

    public UnsupportedException() {
        super(401, "UnsupportedException", "Your application version is not supported. Please update your app.");
    }
}
