package app.munch.image;

import app.munch.model.Account;
import app.munch.model.Image;
import app.munch.model.ImageSource;
import dev.fuxing.err.UnauthorizedException;
import dev.fuxing.jpa.EntityStream;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.transport.TransportCursor;
import dev.fuxing.transport.TransportList;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Date;
import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 25/9/19
 * Time: 1:01 am
 */
@Singleton
public final class ImageQueryClient {
    private final TransactionProvider provider;

    @Inject
    ImageQueryClient(TransactionProvider provider) {
        this.provider = provider;
    }

    public TransportList query(String accountId, Set<ImageSource> sources, int size, TransportCursor cursor) {
        final Long createdAt = cursor.getLong("createdAt");
        final String uid = cursor.get("uid");

        return provider.reduce(true, entityManager -> {
            Account account = entityManager.find(Account.class, accountId);
            if (account == null) throw new UnauthorizedException();

            return EntityStream.of(() -> {
                if (createdAt != null && uid != null) {
                    if (sources.isEmpty()) {
                        return entityManager.createQuery("FROM Image " +
                                "WHERE profile.uid = :profileId " +
                                "AND (createdAt < :createdAt OR (createdAt = :createdAt AND uid < :uid)) " +
                                "ORDER BY createdAt DESC, uid DESC ", Image.class)
                                .setParameter("profileId", account.getProfile().getUid())
                                .setParameter("createdAt", new Date(createdAt))
                                .setParameter("uid", uid)
                                .setMaxResults(size)
                                .getResultList();
                    }

                    return entityManager.createQuery("FROM Image " +
                            "WHERE profile.uid = :profileId AND source IN (:sources) " +
                            "AND (createdAt < :createdAt OR (createdAt = :createdAt AND uid < :uid)) " +
                            "ORDER BY createdAt DESC, uid DESC ", Image.class)
                            .setParameter("profileId", account.getProfile().getUid())
                            .setParameter("sources", sources)
                            .setParameter("createdAt", new Date(createdAt))
                            .setParameter("uid", uid)
                            .setMaxResults(size)
                            .getResultList();
                }

                if (sources.isEmpty()) {
                    return entityManager.createQuery("FROM Image " +
                            "WHERE profile.uid = :profileId " +
                            "ORDER BY createdAt DESC, uid DESC ", Image.class)
                            .setParameter("profileId", account.getProfile().getUid())
                            .setMaxResults(size)
                            .getResultList();
                }

                return entityManager.createQuery("FROM Image " +
                        "WHERE profile.uid = :profileId AND source IN (:sources) " +
                        "ORDER BY createdAt DESC, uid DESC ", Image.class)
                        .setParameter("profileId", account.getProfile().getUid())
                        .setParameter("sources", sources)
                        .setMaxResults(size)
                        .getResultList();
            }).cursor(size, (image, builder) -> {
                builder.put("createdAt", image.getCreatedAt().getTime());
                builder.put("uid", image.getUid());
            }).asTransportList();
        });
    }
}
