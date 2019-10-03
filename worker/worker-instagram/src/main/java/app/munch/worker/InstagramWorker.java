package app.munch.worker;

import app.munch.database.DatabaseModule;
import app.munch.model.WorkerTask;
import dev.fuxing.jpa.TransactionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Date: 2/10/19
 * Time: 9:55 am
 *
 * @author Fuxing Loh
 */
@Singleton
public final class InstagramWorker implements WorkerRunner {
    private static final Logger logger = LoggerFactory.getLogger(InstagramWorker.class);

    private final TransactionProvider provider;

    @Inject
    InstagramWorker(TransactionProvider provider) {
        this.provider = provider;
    }

    @Override
    public String groupUid() {
        return "01dp6jafyrdfvhbz07fsf6ndb3";
    }

    @Override
    public void run(WorkerTask task) throws Exception {
        // TODO(fuxing): Process Each Instagram Account
        // TODO(fuxing): DynamoDB

        provider.with(entityManager -> {

        });
    }

    public static void main(String[] args) {
        WorkerRunner.start(InstagramWorker.class,
                new DatabaseModule()
        );
    }
}
