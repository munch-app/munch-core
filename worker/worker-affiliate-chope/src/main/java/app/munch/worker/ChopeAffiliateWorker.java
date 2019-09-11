package app.munch.worker;

import app.munch.database.DatabaseModule;
import app.munch.manager.AffiliateEntityManager;
import app.munch.manager.ChangeGroupProvider;
import app.munch.model.Affiliate;
import app.munch.model.Profile;
import app.munch.model.WorkerTask;
import dev.fuxing.err.NotFoundException;
import dev.fuxing.utils.SleepUtils;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by: Fuxing
 * Date: 30/8/19
 * Time: 1:01 PM
 * Project: munch-core
 */
public final class ChopeAffiliateWorker implements WorkerRunner {
    private static final Logger logger = LoggerFactory.getLogger(ChopeAffiliateWorker.class);

    /*
     * Email response:
     * As for the booking widget links and ChopeDeals affiliate links,
     * you can refer to this sheet which is updated on a weekly basis.
     */

    private final ChopeWebBrowser browser;
    private final ChopeAffiliateParser parser;
    private final AffiliateEntityManager affiliateEntityManager;

    private final ChangeGroupProvider changeProvider;

    @Inject
    ChopeAffiliateWorker(AffiliateEntityManager affiliateEntityManager, ChangeGroupProvider changeProvider, ChopeWebBrowser browser, ChopeAffiliateParser parser) {
        this.affiliateEntityManager = affiliateEntityManager;
        this.changeProvider = changeProvider;
        this.browser = browser;
        this.parser = parser;
    }

    @Override
    public String groupUid() {
        return "01dm081jszqt29x1mvmn4d3d7a";
    }

    @Override
    public void run(WorkerTask task) throws Exception {
        Iterator<String> iterator = browser.iterator();

        changeProvider.newGroup(Profile.ADMIN_ID, "Chope Affiliate Worker (Ingest)", null, ingestGroup -> {
            iterator.forEachRemaining(url -> {
                Affiliate affiliate = getAffiliate(url);
                if (affiliate == null) return;

                affiliateEntityManager.ingest(ingestGroup, affiliate);
            });

            changeProvider.newGroup(Profile.ADMIN_ID, "Chope Affiliate Worker (Digest)", null, digestGroup -> {
                affiliateEntityManager.digest(ingestGroup, digestGroup, ChopeAffiliateParser.SOURCE);
            });
        });
    }

    private Affiliate getAffiliate(String url) {
        try {
            SleepUtils.sleep(2000);
            logger.info("Parsing URL: {}", url);

            Document document = browser.get(url);
            return parser.parse(document);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NotFoundException e) {
            logger.error("Not Found URL: {}", url, e);
            return null;
        }
    }

    public static void main(String[] args) {
        WorkerRunner.start(ChopeAffiliateWorker.class,
                new DatabaseModule()
        );
    }
}
