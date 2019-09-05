package app.munch.worker;

import app.munch.database.DatabaseModule;
import app.munch.manager.AffiliateEntityManager;
import app.munch.manager.ChangeGroupManager;
import app.munch.model.Affiliate;
import app.munch.model.Profile;
import app.munch.worker.data.WorkerGroup;
import com.google.inject.Guice;
import com.google.inject.Injector;

import javax.inject.Inject;
import java.util.Iterator;

/**
 * Created by: Fuxing
 * Date: 30/8/19
 * Time: 1:01 PM
 * Project: munch-core
 */
public final class ChopeAffiliateWorker implements Worker {

    private final AffiliateEntityManager affiliateEntityManager;
    private final ChopeAffiliateFetcher fetcher;
    private final ChangeGroupManager changeGroupManager;

    @Inject
    ChopeAffiliateWorker(AffiliateEntityManager affiliateEntityManager, ChopeAffiliateFetcher fetcher, ChangeGroupManager changeGroupManager) {
        this.affiliateEntityManager = affiliateEntityManager;
        this.fetcher = fetcher;
        this.changeGroupManager = changeGroupManager;
    }

    /*
     * Email response:
     * As for the booking widget links and ChopeDeals affiliate links,
     * you can refer to this sheet which is updated on a weekly basis.
     */

    @Override
    public String group() {
        return "chope.co";
    }

    @Override
    public String name() {
        return "Chope Affiliate Program";
    }

    @Override
    public String description() {
        return "Synchronization of chope.co affiliate links into munch eco system.";
    }

    @Override
    public void run(WorkerGroup workerGroup) throws Exception {
        Iterator<Affiliate> iterator = fetcher.fetch();

        changeGroupManager.newGroup(Profile.ADMIN_ID, "Chope Affiliate Worker (Ingest)", null, ingestGroup -> {
            iterator.forEachRemaining(affiliate -> {
                affiliateEntityManager.ingest(ingestGroup, affiliate);
            });

            changeGroupManager.newGroup(Profile.ADMIN_ID, "Chope Affiliate Worker (Digest)", null, digestGroup -> {
                affiliateEntityManager.digest(ingestGroup, digestGroup, ChopeAffiliateFetcher.SOURCE);
            });
        });
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new WorkerModule(), new DatabaseModule());
        injector.getInstance(WorkerRunner.class).run(
                injector.getInstance(ChopeAffiliateWorker.class)
        );
    }
}
