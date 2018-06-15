package munch.api.core;

import munch.api.ObjectCleaner;
import munch.data.tag.Tag;

import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 14/6/18
 * Time: 12:10 AM
 * Project: munch-core
 */
@Singleton
public final class TagCleaner extends ObjectCleaner<Tag> {
    @Override
    protected Class<Tag> getClazz() {
        return Tag.class;
    }

    @Override
    protected void clean(Tag data) {
        data.setPlace(null);
        data.setSearch(null);
        data.setCounts(null);
    }
}
