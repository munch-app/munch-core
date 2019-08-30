package app.munch.worker;

import app.munch.worker.reporting.WorkerReport;
import app.munch.worker.reporting.WorkerReportManager;
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

    private final WorkerReportManager reportManager;

    @Inject
    WorkerRunner(WorkerReportManager reportManager) {
        this.reportManager = reportManager;
    }

    /**
     * Run worker with
     * - health check
     * - reporting
     *
     * @param worker to run
     */
    public void run(Worker worker) {
        WorkerReport report = reportManager.start(worker.group());

        HealthCheckServer.startBlocking(() -> {
            try {
                worker.run(report);
                reportManager.complete(report);
            } catch (Exception e) {
                reportManager.error(report, e);
            }
        });
    }
}
