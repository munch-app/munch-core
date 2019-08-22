package app.munch.api;

import app.munch.manager.ArticleEntityManager;
import app.munch.model.ArticleStatus;
import app.munch.model.Profile;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.transport.TransportCursor;
import dev.fuxing.transport.TransportList;
import dev.fuxing.transport.service.TransportContext;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 2019-08-12
 * Time: 05:46
 */
@Singleton
public final class ProfileService extends DataService {

    private final ArticleEntityManager articleEntityManager;

    @Inject
    ProfileService(TransactionProvider provider, ArticleEntityManager articleEntityManager) {
        super(provider);
        this.articleEntityManager = articleEntityManager;
    }

    @Override
    public void route() {
        PATH("/profiles/:username", () -> {
            GET("", this::profileGet);

            PATH("/articles", () -> {
                GET("", this::articlesList);
            });
        });
    }

    public Profile profileGet(TransportContext ctx) {
        String username = ctx.pathString("username");

        return provider.reduce(true, entityManager -> {
            return entityManager.createQuery("FROM Profile WHERE username = :username", Profile.class)
                    .setParameter("username", username)
                    .getSingleResult();
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
}