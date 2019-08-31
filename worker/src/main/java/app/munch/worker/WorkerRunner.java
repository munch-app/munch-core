package app.munch.worker;

import app.munch.worker.data.WorkerReport;
import app.munch.worker.data.WorkerGroupManager;
import dev.fuxing.health.HealthCheckServer;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 30/8/19
 * Time: 12:57 PM
 * Project: munch-core
 */
@Singleton
public final class WorkerRunner {

    private final WorkerGroupManager groupManager;

    @Inject
    WorkerRunner(WorkerGroupManager groupManager) {
        this.groupManager = groupManager;
    }

    /**
     * Run worker with
     * - health check
     * - reporting
     *
     * @param worker to run
     */
    public void run(Worker worker) {
        WorkerReport report = groupManager.start(worker);

        HealthCheckServer.startBlocking(() -> {
            try {
                worker.run(report);
                groupManager.complete(report);
            } catch (Exception e) {
                groupManager.error(report, e);
            }
        });
    }
}
