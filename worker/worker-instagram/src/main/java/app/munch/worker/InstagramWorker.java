package app.munch.worker;

import app.munch.database.DatabaseModule;
import app.munch.model.WorkerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    @Override
    public String groupUid() {
        // TODO(fuxing): required
        return null;
    }

    @Override
    public void run(WorkerTask task) throws Exception {
        WorkerRunner.start(InstagramWorker.class,
                new DatabaseModule()
        );
    }
}
