package app.munch.api;

import app.munch.manager.AffiliateEntityManager;
import app.munch.model.Affiliate;
import app.munch.model.AffiliateStatus;
import app.munch.model.Profile;
import com.fasterxml.jackson.databind.JsonNode;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.transport.TransportList;
import dev.fuxing.transport.service.TransportContext;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 6/9/19
 * Time: 10:40 am
 */
@Singleton
public final class AffiliateAdminService extends AdminService {

    private final AffiliateEntityManager affiliateEntityManager;

    @Inject
    AffiliateAdminService(TransactionProvider provider, AffiliateEntityManager affiliateEntityManager) {
        super(provider);
        this.affiliateEntityManager = affiliateEntityManager;
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

        return affiliateEntityManager.list(status, size, ctx.queryCursor());
    }

    public Affiliate get(TransportContext ctx) {
        String uid = ctx.pathString("uid");
        return affiliateEntityManager.get(uid);
    }

    public Affiliate patch(TransportContext ctx) {
        ApiRequest request = ctx.get(ApiRequest.class);
        String uid = ctx.pathString("uid");
        JsonNode body = ctx.bodyAsJson();

        return affiliateEntityManager.patch(uid, body, entityManager -> {
            return entityManager.createQuery("SELECT a.profile FROM Account a " +
                    "WHERE a.id = :id", Profile.class)
                    .setParameter("id", request.getAccountId())
                    .getSingleResult();
        });
    }
}
