package app.munch.api;

import app.munch.model.ArticleStatus;
import app.munch.model.Profile;
import app.munch.query.ArticleQuery;
import app.munch.query.MediaQuery;
import app.munch.query.MentionQuery;
import dev.fuxing.transport.TransportCursor;
import dev.fuxing.transport.TransportList;
import dev.fuxing.transport.service.TransportContext;
import dev.fuxing.transport.service.TransportResult;
import org.hibernate.Hibernate;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 2019-08-12
 * Time: 05:46
 */
@Singleton
public final class ProfileService extends ApiService {

    private final MediaQuery mediaQuery;
    private final ArticleQuery articleQuery;
    private final MentionQuery mentionQuery;

    @Inject
    ProfileService(MediaQuery mediaQuery, ArticleQuery articleQuery, MentionQuery mentionQuery) {
        this.mediaQuery = mediaQuery;
        this.articleQuery = articleQuery;
        this.mentionQuery = mentionQuery;
    }

    @Override
    public void route() {
        PATH("/profiles/:username", () -> {
            GET("", this::get);

            PATH("/articles", () -> {
                GET("", this::articles);
            });

            PATH("/medias", () -> {
                GET("", this::medias);
            });

            PATH("/mentions", () -> {
                GET("", this::mentions);
            });
        });
    }

    public TransportResult get(TransportContext ctx) {
        String username = ctx.pathString("username");
        Set<String> fields = ctx.queryFields();

        return provider.reduce(true, entityManager -> {
            Profile profile = Profile.findByUsername(entityManager, username);
            if (profile == null) return null;

            Hibernate.initialize(profile.getLinks());
            return result(builder -> {
                builder.data(profile);

                if (fields.contains("medias")) {
                    MediaQuery.query(entityManager, TransportCursor.EMPTY, query -> {
                        query.where("profile", profile);
                    }).consume((medias, cursor) -> {
                        builder.extra("medias", medias);

                        if (cursor != null) {
                            builder.cursor("next.medias", cursor.get("next"));
                        }
                    });
                }

                if (fields.contains("articles")) {
                    ArticleQuery.query(entityManager, TransportCursor.EMPTY, ArticleStatus.PUBLISHED, query -> {
                        query.where("profile", profile);
                    }).consume((articles, cursor) -> {
                        builder.extra("articles", articles);

                        if (cursor != null) {
                            builder.cursor("next.articles", cursor.get("next"));
                        }
                    });
                }

            });
        });
    }

    public TransportList articles(TransportContext ctx) {
        return articleQuery.query(ctx.queryCursor(), ArticleStatus.PUBLISHED, entityManager -> {
            return Profile.findByUsername(entityManager, ctx.pathString("username"));
        });
    }

    public TransportList medias(TransportContext ctx) {
        final String username = ctx.pathString("username");
        return mediaQuery.query(username, ctx.queryCursor());
    }

    public TransportList mentions(TransportContext ctx) {
        final String username = ctx.pathString("username");
        return mentionQuery.queryByUsername(username, ctx.queryCursor());
    }
}
