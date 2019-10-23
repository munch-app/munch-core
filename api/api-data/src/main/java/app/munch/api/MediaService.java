package app.munch.api;

import app.munch.model.ArticleStatus;
import app.munch.model.Mention;
import app.munch.model.ProfileMedia;
import app.munch.model.ProfileMediaStatus;
import app.munch.query.ArticleQuery;
import app.munch.query.MediaQuery;
import app.munch.query.MentionQuery;
import dev.fuxing.err.ForbiddenException;
import dev.fuxing.transport.TransportCursor;
import dev.fuxing.transport.service.TransportContext;
import dev.fuxing.transport.service.TransportResult;
import org.hibernate.Hibernate;

import javax.inject.Singleton;
import java.util.Set;

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

    public TransportResult get(TransportContext ctx) {
        final String id = ctx.pathString("id");
        Set<String> fields = ctx.queryFields();

        return provider.reduce(true, entityManager -> {
            ProfileMedia media = entityManager.find(ProfileMedia.class, id);
            if (media == null) {
                return null;
            }

            if (media.getStatus() != ProfileMediaStatus.PUBLIC) {
                throw new ForbiddenException();
            }

            Hibernate.initialize(media.getProfile());
            Hibernate.initialize(media.getImages());
            Hibernate.initialize(media.getMentions());
            MediaQuery.peek(media);

            return result(builder -> {
                builder.data(media);

                if (fields.contains("extra.profile.articles")) {
                    TransportCursor cursor = TransportCursor.size(
                            ctx.queryInt("extra.profile.articles.size", 5)
                    );
                    ArticleQuery.query(entityManager, cursor, ArticleStatus.PUBLISHED, query -> {
                        query.where("profile", media.getProfile());
                    }).consume((articles, c) -> {
                        builder.extra("profile.articles", articles);
                    });
                }

                if (fields.contains("extra.profile.medias")) {
                    TransportCursor cursor = TransportCursor.size(
                            ctx.queryInt("extra.profile.medias.size", 5)
                    );
                    MediaQuery.query(entityManager, cursor, query -> {
                        query.where("profile", media.getProfile());
                        query.where("id != :mid", "mid", media.getId());
                    }).consume((medias, c) -> {
                        builder.extra("profile.medias", medias);
                    });
                }

                if (fields.contains("extra.mention.place.mentions") && !media.getMentions().isEmpty()) {
                    Mention mention = media.getMentions().get(0);
                    TransportCursor cursor = TransportCursor.size(
                            ctx.queryInt("extra.mention.place.mentions", 4)
                    );
                    MentionQuery.query(entityManager, TransportCursor.EMPTY, query -> {
                        query.where("place", mention.getPlace());
                        query.where("id != :mid", "mid", mention.getId());
                    }).consume((mentions, c) -> {
                        builder.extra("mention.place.mentions", mentions);
                    });
                }
            });
        });
    }
}
