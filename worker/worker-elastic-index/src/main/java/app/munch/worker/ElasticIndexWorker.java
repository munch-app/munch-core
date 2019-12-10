package app.munch.worker;

import app.munch.database.DatabaseModule;
import app.munch.elastic.ElasticIndexSubscriber;
import app.munch.elastic.ElasticModule;
import app.munch.elastic.ElasticSerializableClient;
import app.munch.elastic.pubsub.IndexMessage;
import app.munch.model.ElasticSerializable;
import app.munch.model.WorkerTask;
import dev.fuxing.err.TransportException;
import dev.fuxing.utils.SleepUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Fuxing Loh
 * @since 2019-11-30 at 05:11
 */
@Singleton
public final class ElasticIndexWorker implements WorkerRunner {
    private static final Logger logger = LoggerFactory.getLogger(ElasticIndexWorker.class);

    private final ElasticIndexSubscriber subscriber;
    private final ElasticModelResolver resolver;
    private final ElasticSerializableClient client;

    @Inject
    ElasticIndexWorker(ElasticIndexSubscriber subscriber, ElasticModelResolver resolver, ElasticSerializableClient client) {
        this.subscriber = subscriber;
        this.resolver = resolver;
        this.client = client;
    }

    @Override
    public String groupUid() {
        return "01dtxbhb2cvdwbg1320277cn64";
    }

    @Override
    public void run(WorkerTask task) throws Exception {
        do {
            subscriber.subscribe(this::index);
            SleepUtils.sleep(1000);
        } while (!Thread.currentThread().isInterrupted());
    }

    protected void index(IndexMessage message) {
        try {
            ElasticSerializable serializable = resolver.resolve(message);
            client.put(serializable);
        } catch (TransportException e) {
            logger.warn("TransportException: {}", e.getMessage());
        }
    }

    public static void main(String[] args) {
        WorkerRunner.start(ElasticIndexWorker.class,
                new DatabaseModule(), new ElasticModule()
        );
    }
}
