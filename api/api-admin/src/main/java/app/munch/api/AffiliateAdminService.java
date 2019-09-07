package app.munch.api;

import app.munch.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import dev.fuxing.jpa.EntityPatch;
import dev.fuxing.jpa.EntityStream;
import dev.fuxing.jpa.HibernateUtils;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.transport.TransportCursor;
import dev.fuxing.transport.TransportList;
import dev.fuxing.transport.service.TransportContext;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Created by: Fuxing
 * Date: 6/9/19
 * Time: 10:40 am
 */
@Singleton
public final class AffiliateAdminService extends AdminService {

    @Inject
    AffiliateAdminService(TransactionProvider provider) {
        super(provider);
    }

    @Override
    public void route() {
        PATH("/admin/affiliates", () -> {
            GET("", this::list);

            PATH("/:uid", () -> {
                GET("", this::get);
                PATCH("", this::patch);
            });
        });
    }

    /**
     * Affiliate list is sorting uid by ASC so that the earliest status get returned first
     */
    public TransportList list(TransportContext ctx) {
        int size = ctx.querySize(20, 50);
        AffiliateStatus status = ctx.queryEnum("status", AffiliateStatus.class, AffiliateStatus.PENDING);

        @NotNull TransportCursor cursor = ctx.queryCursor();
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

    public Affiliate get(TransportContext ctx) {
        String uid = ctx.pathString("uid");
        return provider.reduce(true, entityManager -> {
            Affiliate affiliate = entityManager.find(Affiliate.class, uid);
            HibernateUtils.initialize(affiliate.getPlace());
            HibernateUtils.initialize(affiliate.getBrand());
            HibernateUtils.initialize(affiliate.getLinked());
            return affiliate;
        });
    }

    public Affiliate patch(TransportContext ctx) {
        ApiRequest request = ctx.get(ApiRequest.class);
        @NotNull String id = request.getAccountId();

        String uid = ctx.pathString("uid");
        JsonNode body = ctx.bodyAsJson();

        return provider.reduce(entityManager -> {
            Account account = entityManager.find(Account.class, id);
            Profile profile = account.getProfile();

            Affiliate affiliate = entityManager.find(Affiliate.class, uid);

            affiliate = EntityPatch.with(entityManager, affiliate, body)
                    .lock()
                    .patch("status", AffiliateStatus.class, Affiliate::setStatus)
                    .patch("linked", (EntityPatch.NodeConsumer<Affiliate>) (entity, json) -> {
                        // Only linked.place.id is required
                        String placeId = json.path("place").path("id").asText();
                        Objects.requireNonNull(placeId);

                        PlaceAffiliate linked = new PlaceAffiliate();
                        linked.setPlace(entityManager.find(Place.class, id));
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
}
