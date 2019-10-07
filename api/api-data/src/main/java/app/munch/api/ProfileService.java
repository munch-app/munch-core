package app.munch.api;

import app.munch.controller.MentionController;
import app.munch.controller.QueryChain;
import app.munch.manager.ArticleEntityManager;
import app.munch.model.ArticleStatus;
import app.munch.model.MentionType;
import app.munch.model.Profile;
import app.munch.model.ProfileMediaStatus;
import dev.fuxing.jpa.HibernateUtils;
import dev.fuxing.transport.TransportCursor;
import dev.fuxing.transport.TransportList;
import dev.fuxing.transport.service.TransportContext;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.Tuple;
import java.util.Date;
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

    private final ArticleEntityManager articleEntityManager;
    private final MentionController mentionController;

    @Inject
    ProfileService(ArticleEntityManager articleEntityManager, MentionController mentionController) {
        this.articleEntityManager = articleEntityManager;
        this.mentionController = mentionController;
    }

    @Override
    public void route() {
        PATH("/profiles/:username", () -> {
            GET("", this::profileGet);

            PATH("/articles", () -> {
                GET("", this::articlesList);
            });

            PATH("/medias", () -> {
                GET("", this::mediasList);
            });

            PATH("/mentions", () -> {
                GET("", this::mentionsList);
            });
        });
    }

    public Profile profileGet(TransportContext ctx) {
        String username = ctx.pathString("username");

        return provider.reduce(true, entityManager -> {
            Profile profile = entityManager.createQuery("FROM Profile WHERE username = :username", Profile.class)
                    .setParameter("username", username)
                    .getSingleResult();
            if (profile == null) return null;

            HibernateUtils.initialize(profile.getLinks());
            return profile;
        });
    }

    public TransportList articlesList(TransportContext ctx) {
        final int size = ctx.querySize(20, 50);
        final String username = ctx.pathString("username");

        TransportCursor cursor = ctx.queryCursor();
        return articleEntityManager.list(ArticleStatus.PUBLISHED, entityManager -> {
            return entityManager.createQuery("FROM Profile WHERE username = :username", Profile.class)
                    .setParameter("username", username)
                    .getSingleResult();
        }, size, cursor);
    }

    public TransportList mentionsList(TransportContext ctx) {
        final int size = ctx.querySize(20, 50);
        final String username = ctx.pathString("username");

        TransportCursor cursor = ctx.queryCursor();
        Set<MentionType> types = cursor.getEnums("types", MentionType.class);
        return mentionController.queryByUsername(username, size, types, cursor);
    }

    public TransportList mediasList(TransportContext ctx) {
        final int size = ctx.querySize(20, 50);
        final String username = ctx.pathString("username");
        TransportCursor cursor = ctx.queryCursor();

        return provider.reduce(entityManager -> {
            QueryChain<Tuple> chain = QueryChain.select(entityManager,
                    "SELECT " +
                            "m.id AS id, " +
                            "m.type AS type, " +
                            "m.images AS images, " +
                            "m.content AS content," +
                            "m.updatedAt AS updatedAt, " +
                            "m.mentions AS mentions, " +
                            "m.createdAt AS createdAt " +
                            "FROM ProfileMedia m", Tuple.class);

            chain.where("m.profile.username = :username", "username", username);
            chain.where("m.status = :status", "status", ProfileMediaStatus.PUBLIC);
            chain.size(size);

            chain.orderBy("createdAt DESC");
            chain.orderBy("id DESC");

            final Long createdAt = cursor.getLong("createdAt");
            final String id = cursor.get("id");
            if (createdAt != null && id != null) {
                chain.where("(createdAt < :createdAt OR (createdAt = :createdAt AND id < :id))",
                        "id", id, "createdAt", new Date(createdAt)
                );
            }

            return chain.asStream().cursor(size, (tuple, builder) -> {
                builder.put("createdAt", tuple.get("createdAt", Date.class).getTime());
                builder.put("id", tuple.get("id"));
            }).map(tuple -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", tuple.get("id"));
                map.put("type", tuple.get("type"));
                map.put("images", tuple.get("images"));
                map.put("content", tuple.get("content"));
                map.put("mentions", tuple.get("mentions"));
                map.put("updatedAt", tuple.get("updatedAt"));
                map.put("createdAt", tuple.get("createdAt"));
                return map;
            }).asTransportList();
        });
    }
}
