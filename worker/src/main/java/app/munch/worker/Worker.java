package app.munch.worker;

import app.munch.worker.data.WorkerReport;

/**
 * Created by: Fuxing
 * Date: 30/8/19
 * Time: 12:24 PM
 * Project: munch-core
 */
public interface Worker {

    /**
     * @return identity of this worker, must be unique
     */
    String group();

    /**
     * @return name of this worker, human readable.
     */
    String name();

    /**
     * @return description of this worker, human readable.
     */
    String description();

    void run(WorkerReport report) throws Exception;
}
