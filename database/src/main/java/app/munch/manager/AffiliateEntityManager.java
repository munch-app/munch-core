package app.munch.manager;

import app.munch.model.*;
import dev.fuxing.jpa.TransactionProvider;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;

/**
 * Created by: Fuxing
 * Date: 4/9/19
 * Time: 1:10 pm
 */
@Singleton
public final class AffiliateEntityManager {
    private final TransactionProvider provider;

    @Inject
    AffiliateEntityManager(TransactionProvider provider) {
        this.provider = provider;
    }

    public void ingest(ChangeGroup group, Affiliate incoming) {
        String sourceKey = incoming.getSourceKey();

        provider.with(entityManager -> {
            // Get Managed Reference brand for persisting later
            AffiliateBrand brand = entityManager.getReference(AffiliateBrand.class, incoming.getBrand().getUid());

            // Get currently persisted affiliate entity.
            Affiliate persisted = find(entityManager, sourceKey);

            // Find status it is going to be resolved to
            AffiliateStatus status = resolveStatus(persisted, incoming);

            Affiliate entity = persisted != null ? persisted : incoming;
            entity.setBrand(brand);
            entity.setStatus(status);

            switch (status) {
                case PENDING:
                case LINKED:
                case DELETED_MUNCH:
                case DELETED_SOURCE:

                    // When it reappear, it doesn't require any changes since it was deleted prior.
                case REAPPEAR:
                    break;

                case DROPPED:
                    entity.setLinked(null);
                    entity.setPlace(null);
                    break;

            }

            ChangeGroup.EntityUtils.map(entityManager, group, entity::setChangeGroup);
            entityManager.persist(entity);
        });

    }

    /**
     * @param ingestGroup change group data that should not be purged.
     * @param digestGroup change group data that mark those getting purged.
     * @param source      to focus puring on.
     */
    public void digest(ChangeGroup ingestGroup, ChangeGroup digestGroup, String source) {
        provider.with(entityManager -> {
            entityManager.createQuery("FROM Affiliate " +
                    "WHERE source = :source " +
                    "AND changeGroup.uid != :changeGroupUid", Affiliate.class)
                    .setParameter("source", source)
                    .setParameter("changeGroupUid", ingestGroup.getUid())
                    .getResultList()
                    .forEach(affiliate -> {
                        switch (affiliate.getStatus()) {
                            case DELETED_MUNCH:
                            case DELETED_SOURCE:
                                return;
                        }

                        affiliate.setStatus(AffiliateStatus.DELETED_SOURCE);
                        ChangeGroup.EntityUtils.map(entityManager, digestGroup, affiliate::setChangeGroup);
                        entityManager.persist(affiliate);
                    });
        });
    }

    private AffiliateStatus resolveStatus(Affiliate persisted, Affiliate incoming) {
        if (persisted == null) {
            return AffiliateStatus.PENDING;
        }

        switch (persisted.getStatus()) {
            case PENDING: // Pending, keep pending.
            case DELETED_MUNCH: // Once deleted, keep it deleted.
            case DROPPED: // Once dropped, keep dropped until user update it.
            default:
                return persisted.getStatus();

            case LINKED:
                // Linked -> Decide whether to drop
                if (!PlaceModel.isSpatiallySimilar(persisted.getPlaceStruct(), incoming.getPlaceStruct())) {
                    return AffiliateStatus.DROPPED;
                }

                return persisted.getStatus();

            case DELETED_SOURCE:
                // Deleted -> Decide whether to reappear
                if (!PlaceModel.isSpatiallySimilar(persisted.getPlaceStruct(), incoming.getPlaceStruct())) {
                    return AffiliateStatus.REAPPEAR;
                }
                return persisted.getStatus();
        }
    }

    private static Affiliate find(EntityManager entityManager, String sourceKey) {
        Objects.requireNonNull(sourceKey);

        List<Affiliate> list = entityManager.createQuery("FROM Affiliate " +
                "WHERE sourceKey = :sourceKey ", Affiliate.class)
                .setParameter("sourceKey", sourceKey)
                .getResultList();

        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
}
