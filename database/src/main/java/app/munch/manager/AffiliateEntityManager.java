package app.munch.manager;

import app.munch.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import dev.fuxing.jpa.EntityPatch;
import dev.fuxing.jpa.EntityStream;
import dev.fuxing.jpa.HibernateUtils;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.transport.TransportCursor;
import dev.fuxing.transport.TransportList;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

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

    public Affiliate get(String uid) {
        return provider.reduce(true, entityManager -> {
            Affiliate affiliate = entityManager.find(Affiliate.class, uid);
            HibernateUtils.initialize(affiliate.getPlace());
            HibernateUtils.initialize(affiliate.getBrand());
            HibernateUtils.initialize(affiliate.getLinked());
            return affiliate;
        });
    }

    public Affiliate patch(String uid, JsonNode body, Function<EntityManager, Profile> profileSupplier) {
        return provider.reduce(entityManager -> {
            Profile profile = profileSupplier.apply(entityManager);

            Affiliate affiliate = entityManager.find(Affiliate.class, uid);
            affiliate = EntityPatch.with(entityManager, affiliate, body)
                    .lock()
                    .patch("status", AffiliateStatus.class, Affiliate::setStatus)
                    .patch("linked", (EntityPatch.NodeConsumer<Affiliate>) (entity, json) -> {
                        // Only linked.place.id is required
                        String placeId = json.path("place").path("id").asText();
                        Objects.requireNonNull(placeId);

                        PlaceAffiliate linked = new PlaceAffiliate();
                        linked.setAffiliate(entity);
                        linked.setPlace(entityManager.find(Place.class, placeId));
                        entity.setLinked(linked);
                    })
                    .peek(entity -> {
                        entity.setEditedBy(profile);
                    })
                    .persist();

            HibernateUtils.initialize(affiliate.getPlace());
            HibernateUtils.initialize(affiliate.getBrand());
            HibernateUtils.initialize(affiliate.getLinked());
            return affiliate;
        });
    }

    public TransportList list(AffiliateStatus status, int size, TransportCursor cursor) {
        String uid = cursor.get("uid");

        return provider.reduce(true, entityManager -> {
            return EntityStream.of(() -> {
                if (uid != null) {
                    return entityManager.createQuery("FROM Affiliate " +
                            "WHERE status = :status " +
                            "AND uid > :uid " +
                            "ORDER BY uid ASC", Affiliate.class)
                            .setParameter("uid", uid)
                            .setParameter("status", status)
                            .setMaxResults(size)
                            .getResultList();
                }

                return entityManager.createQuery("FROM Affiliate " +
                        "WHERE status = :status " +
                        "ORDER BY uid ASC", Affiliate.class)
                        .setParameter("status", status)
                        .setMaxResults(size)
                        .getResultList();
            }).peek(HibernateUtils::clean)
                    .cursor(size, (affiliate, builder) -> {
                        builder.put("uid", affiliate.getUid());
                    })
                    .asTransportList();
        });
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
                case DELETED_MUNCH:
                case DELETED_SOURCE:

                    // When it reappear, it doesn't require any changes since it was deleted prior.
                case REAPPEAR:
                    break;

                case LINKED:
                    if (entity.getLinked() == null && entity.getPlace() != null) {
                        PlaceAffiliate linked = new PlaceAffiliate();
                        linked.setPlace(entity.getPlace());
                        linked.setAffiliate(entity);
                        entity.setLinked(linked);
                    }
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

    private static AffiliateStatus resolveStatus(Affiliate persisted, Affiliate incoming) {
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
                if (PlaceModel.isSpatiallySimilar(persisted.getPlaceStruct(), incoming.getPlaceStruct())) {
                    return AffiliateStatus.LINKED;
                } else {
                    return AffiliateStatus.REAPPEAR;
                }
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
