package app.munch.api;

import app.munch.model.*;
import app.munch.query.ArticleQuery;
import app.munch.query.MediaQuery;
import dev.fuxing.err.ForbiddenException;
import dev.fuxing.jpa.EntityQuery;
import dev.fuxing.transport.TransportCursor;
import dev.fuxing.transport.service.TransportContext;
import dev.fuxing.transport.service.TransportResult;

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

            MediaQuery.initialize(media);
            MediaQuery.clean(media);

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
                    MediaQuery.query(entityManager, cursor, b -> {
                        b.where("profile", media.getProfile());
                        b.whereNotEqual("id", media.getId());
                    }).consume((medias, c) -> {
                        builder.extra("profile.medias", medias);
                    });
                }

                if (fields.contains("extra.place.medias") && !media.getMentions().isEmpty()) {
                    TransportCursor cursor = TransportCursor.size(
                            ctx.queryInt("extra.place.medias.size", 4)
                    );
                    Mention mention = media.getMentions().get(0);

                    EntityQuery.select(entityManager, "SELECT m.media FROM Mention m", ProfileMedia.class)
                            .where("m.id != :mid", "mid", mention.getId())
                            .where("m.place = :place", "place", mention.getPlace())
                            .where("m.status = :status", "status", MentionStatus.PUBLIC)
                            .where("m.type = :type", "type", MentionType.MEDIA)
                            .orderBy("m.createdAt DESC, m.id DESC")
                            .size(cursor.size(4, 8))
                            .asStream()
                            .peek(m -> {
                                m.setMentions(null);
                                MediaQuery.initialize(m);
                            })
                            .consume((medias, c) -> {
                                builder.extra("place.medias", medias);
                            });
                }
            });
        });
    }
}
