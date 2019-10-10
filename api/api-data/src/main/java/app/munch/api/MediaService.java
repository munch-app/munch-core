package app.munch.api;

import app.munch.model.ProfileMedia;
import app.munch.model.ProfileMediaStatus;
import dev.fuxing.err.ForbiddenException;
import dev.fuxing.jpa.HibernateUtils;
import dev.fuxing.transport.service.TransportContext;

import javax.inject.Singleton;

/**
 * @author Fuxing Loh
 * @since 2019-10-10 at 11:22 pm
 */
@Singleton
public final class MediaService extends ApiService {

    @Override
    public void route() {
        PATH("/medias/:id", () -> {
            GET("", this::get);
        });
    }

    public ProfileMedia get(TransportContext ctx) {
        final String id = ctx.pathString("id");

        return provider.reduce(entityManager -> {
            ProfileMedia media = entityManager.find(ProfileMedia.class, id);
            if (media == null) {
                return null;
            }

            if (media.getStatus() != ProfileMediaStatus.PUBLIC) {
                throw new ForbiddenException();
            }

            HibernateUtils.initialize(media.getProfile());
            HibernateUtils.initialize(media.getImages());
            return media;
        });
    }
}
