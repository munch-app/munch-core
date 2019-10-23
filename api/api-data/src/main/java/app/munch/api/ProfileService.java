package app.munch.api;

import app.munch.model.ArticleStatus;
import app.munch.model.Profile;
import app.munch.model.ProfileMediaStatus;
import app.munch.query.ArticleQuery;
import app.munch.query.MediaQuery;
import app.munch.query.MentionQuery;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.fuxing.jpa.HibernateUtils;
import dev.fuxing.transport.TransportCursor;
import dev.fuxing.transport.TransportList;
import dev.fuxing.transport.service.TransportContext;
import dev.fuxing.transport.service.TransportResult;
import dev.fuxing.utils.JsonUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
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

            HibernateUtils.initialize(profile.getLinks());
            ObjectNode node = JsonUtils.valueToTree(profile);
            Map<String, String> cursor = new HashMap<>();

            if (fields.contains("medias")) {
                MediaQuery.query(entityManager, TransportCursor.EMPTY, query -> {
                    query.where("profile", profile);
                }).cursor((media, builder) -> {
                    builder.put("status", ProfileMediaStatus.PUBLIC);
                    builder.put("createdAt", media.getCreatedAt().getTime());
                    builder.put("id", media.getId());
                }).consume((medias, mediasCursor) -> {
                    node.set("medias", JsonUtils.valueToTree(medias));
                    if (mediasCursor != null) {
                        cursor.put("next.medias", mediasCursor.get("next"));
                    }
                });
            }

            if (fields.contains("articles")) {
                ArticleQuery.query(entityManager, TransportCursor.EMPTY, ArticleStatus.PUBLISHED, query -> {
                    query.where("profile", profile);
                }).cursor((article, builder) -> {
                    builder.put("status", ArticleStatus.PUBLISHED);
                    builder.put("publishedAt", article.getPublishedAt().getTime());
                    builder.put("id", article.getId());
                }).consume((articles, articleCursor) -> {
                    node.set("articles", JsonUtils.valueToTree(articles));
                    if (articleCursor != null) {
                        cursor.put("next.articles", articleCursor.get("next"));
                    }
                });
            }

            return TransportResult.builder()
                    .data(node)
                    .cursor(cursor)
                    .build();
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
