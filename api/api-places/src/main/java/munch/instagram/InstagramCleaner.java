package munch.instagram;

import munch.api.ObjectCleaner;
import munch.data.tag.Tag;
import munch.instagram.data.InstagramMedia;

import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 14/6/18
 * Time: 12:10 AM
 * Project: munch-core
 */
@Singleton
public final class InstagramCleaner extends ObjectCleaner<InstagramMedia> {

    @Override
    protected Class<InstagramMedia> getClazz() {
        return InstagramMedia.class;
    }

    @Override
    protected void clean(InstagramMedia data) {
        data.setCount(null);
        data.setVision(null);
    }
}
