package munch.data.database.hibernate;

import munch.data.ImageMeta;

/**
 * Created by: Fuxing
 * Date: 18/8/2017
 * Time: 1:54 AM
 * Project: munch-core
 */
public final class ImageMetaUserType extends PojoUserType<ImageMeta> {
    public ImageMetaUserType() {
        super(ImageMeta.class);
    }
}
