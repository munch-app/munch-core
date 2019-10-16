package app.munch.api;

import app.munch.model.*;
import dev.fuxing.jpa.EntityPatch;
import dev.fuxing.jpa.EntityQuery;
import dev.fuxing.jpa.HibernateUtils;
import dev.fuxing.transport.TransportCursor;
import dev.fuxing.transport.TransportList;
import dev.fuxing.transport.service.TransportContext;

import javax.inject.Singleton;
import java.util.Objects;

/**
 * Created by: Fuxing
 * Date: 6/9/19
 * Time: 10:40 am
 */
@Singleton
public final class AffiliateAdminService extends AdminService {

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
        final int size = ctx.querySize(20, 50);
        TransportCursor cursor = ctx.queryCursor();

        return provider.reduce(true, entityManager -> {
            return EntityQuery.select(entityManager, "FROM Affiliate", Affiliate.class)
                    .size(size)
                    .where("status = :status", "status", cursor.getEnum("status", AffiliateStatus.class, AffiliateStatus.PENDING))
                    .predicate(cursor.has("uid"), (query) -> {
                        query.where("uid > :uid", "uid", cursor.get("uid"));
                    })
                    .orderBy("uid ASC")
                    .asTransportList((affiliate, builder) -> {
                        builder.putAll(cursor);
                        builder.put("uid", affiliate.getUid());
                    });
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
        String uid = ctx.pathString("uid");

        return patch(ctx, em -> em.find(Affiliate.class, uid), (entityManager, patcher) -> {
            Profile profile = findProfile(entityManager, ctx);

            return patcher.lock()
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

                        HibernateUtils.initialize(entity.getPlace());
                        HibernateUtils.initialize(entity.getBrand());
                        HibernateUtils.initialize(entity.getLinked());
                    })
                    .persist();
        });
    }
}
