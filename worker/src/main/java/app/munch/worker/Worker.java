package app.munch.worker;

import app.munch.worker.reporting.WorkerReport;

/**
 * Created by: Fuxing
 * Date: 30/8/19
 * Time: 12:24 PM
 * Project: munch-core
 */
public interface Worker {

    String group();

    void run(WorkerReport report) throws Exception;
}
