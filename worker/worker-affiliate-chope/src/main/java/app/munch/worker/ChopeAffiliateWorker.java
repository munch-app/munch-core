package app.munch.worker;

import app.munch.database.DatabaseModule;
import app.munch.manager.AffiliateEntityManager;
import app.munch.manager.ChangeGroupManager;
import app.munch.model.Affiliate;
import app.munch.model.Profile;
import app.munch.model.WorkerTask;
import app.munch.worker.google.GoogleSheetModule;

import javax.inject.Inject;
import java.util.Iterator;

/**
 * Created by: Fuxing
 * Date: 30/8/19
 * Time: 1:01 PM
 * Project: munch-core
 */
public final class ChopeAffiliateWorker implements WorkerRunner {
    /*
     * Email response:
     * As for the booking widget links and ChopeDeals affiliate links,
     * you can refer to this sheet which is updated on a weekly basis.
     */

    private final AffiliateEntityManager affiliateEntityManager;
    private final ChopeAffiliateFetcher fetcher;
    private final ChangeGroupManager changeGroupManager;

    @Inject
    ChopeAffiliateWorker(AffiliateEntityManager affiliateEntityManager, ChopeAffiliateFetcher fetcher, ChangeGroupManager changeGroupManager) {
        this.affiliateEntityManager = affiliateEntityManager;
        this.fetcher = fetcher;
        this.changeGroupManager = changeGroupManager;
    }

    @Override
    public String groupUid() {
        return "01dm081jszqt29x1mvmn4d3d7a";
    }

    @Override
    public void run(WorkerTask task) throws Exception {
        Iterator<Affiliate> iterator = fetcher.fetch();

        changeGroupManager.newGroup(Profile.ADMIN_ID, "Chope Affiliate Worker (Ingest)", null, ingestGroup -> {
            iterator.forEachRemaining(affiliate -> {
                affiliateEntityManager.ingest(ingestGroup, affiliate);
            });

            changeGroupManager.newGroup(Profile.ADMIN_ID, "Chope Affiliate Worker (Digest)", null, digestGroup -> {
                // TODO(fuxing): Ability to double check before digest?
                affiliateEntityManager.digest(ingestGroup, digestGroup, ChopeAffiliateFetcher.SOURCE);
            });
        });
    }

    public static void main(String[] args) {
        WorkerRunner.start(ChopeAffiliateWorker.class,
                new DatabaseModule(), new GoogleSheetModule()
        );
    }
}
