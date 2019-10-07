package app.munch.worker;

import app.munch.database.DatabaseModule;
import app.munch.model.WorkerTask;
import dev.fuxing.jpa.TransactionProvider;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Fuxing Loh
 * @since 7/10/19 at 5:00 pm
 */
@Singleton
public final class MediaWorker implements WorkerRunner {

    private final TransactionProvider provider;

    @Inject
    MediaWorker(TransactionProvider provider) {
        this.provider = provider;
    }

    @Override
    public String groupUid() {
        return null;
    }

    @Override
    public void run(WorkerTask task) throws Exception {

    }

    public static void main(String[] args) {
        WorkerRunner.start(MediaWorker.class,
                new DatabaseModule()
        );
    }
}
