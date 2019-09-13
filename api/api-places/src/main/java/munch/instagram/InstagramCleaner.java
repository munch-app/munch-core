package munch.instagram;

import munch.api.ObjectCleaner;
import munch.instagram.data.InstagramMedia;

import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 14/6/18
 * Time: 12:10 AM
 * Project: munch-core
 */
@Singleton
@Deprecated
public final class InstagramCleaner extends ObjectCleaner<InstagramMedia> {

    @Override
    protected Class<InstagramMedia> getClazz() {
        return InstagramMedia.class;
    }

    @Override
    protected void clean(InstagramMedia data) {
        data.setCount(null);
        data.setLocation(null);
        data.setVision(null);

        data.setFilter(null);
    }
}
