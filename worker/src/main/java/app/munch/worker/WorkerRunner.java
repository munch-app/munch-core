package app.munch.worker;

import app.munch.model.WorkerTask;
import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.fuxing.health.HealthCheckServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by: Fuxing
 * Date: 30/8/19
 * Time: 12:57 PM
 * Project: munch-core
 */
public interface WorkerRunner {
    Logger logger = LoggerFactory.getLogger(WorkerRunner.class);

    /**
     * @return WorkerGroup.uid, must already be created.
     */
    String groupUid();

    void run(WorkerTask task) throws Exception;


    static <T extends WorkerRunner> void start(Class<T> runnerClass, com.google.inject.Module... modules) {
        Injector injector = Guice.createInjector(modules);
        WorkerCoordinator coordinator = injector.getInstance(WorkerCoordinator.class);

        T worker = injector.getInstance(runnerClass);
        WorkerTask task = coordinator.start(worker.groupUid());

        HealthCheckServer.startBlocking(() -> {
            try {
                worker.run(task);
                coordinator.complete(task);
            } catch (Exception e) {
                coordinator.error(task, e);
                logger.error("Worker ended exceptionally", e);
            }
        });
        System.exit(1);
    }
}
