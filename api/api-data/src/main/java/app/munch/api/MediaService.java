package app.munch.api;

import app.munch.model.ProfileMedia;
import app.munch.query.MediaQuery;
import dev.fuxing.transport.service.TransportContext;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Fuxing Loh
 * @since 2019-10-10 at 11:22 pm
 */
@Singleton
public final class MediaService extends ApiService {

    private final MediaQuery mediaQuery;

    @Inject
    public MediaService(MediaQuery mediaQuery) {
        this.mediaQuery = mediaQuery;
    }

    @Override
    public void route() {
        PATH("/medias/:id", () -> {
            GET("", this::get);
        });
    }

    public ProfileMedia get(TransportContext ctx) {
        final String id = ctx.pathString("id");
        return mediaQuery.find(id);
    }
}
