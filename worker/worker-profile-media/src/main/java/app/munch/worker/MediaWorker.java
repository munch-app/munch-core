package app.munch.worker;

import app.munch.database.DatabaseModule;
import app.munch.image.ImageModule;
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
public final class MediaWorker implements WorkerRunner {
    private static final Logger logger = LoggerFactory.getLogger(MediaWorker.class);

    private final TransactionProvider provider;

    @Inject
    MediaWorker(TransactionProvider provider) {
        this.provider = provider;
    }

    @Override
    public String groupUid() {
        return "01dpkgbwqqmrk9ht5bzhw2jthh";
    }

    @Override
    public void run(WorkerTask task) {

    }

    public static void main(String[] args) {
        WorkerRunner.start(MediaWorker.class,
                new DatabaseModule(), new ImageModule()
        );
    }
}
