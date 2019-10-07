package app.munch.worker;

import app.munch.database.DatabaseModule;
import app.munch.model.WorkerTask;
import dev.fuxing.jpa.TransactionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Fuxing Loh
 * @since 7/10/19 at 5:00 pm
 */
@Singleton
public final class MentionWorker implements WorkerRunner {
    private static final Logger logger = LoggerFactory.getLogger(MentionWorker.class);

    private final TransactionProvider provider;

    @Inject
    MentionWorker(TransactionProvider provider) {
        this.provider = provider;
    }

    @Override
    public String groupUid() {
        return "01dpkgdhd659pbb520my5k6h8j";
    }

    @Override
    public void run(WorkerTask task) throws Exception {

    }

    public static void main(String[] args) {
        WorkerRunner.start(MentionWorker.class,
                new DatabaseModule()
        );
    }
}
