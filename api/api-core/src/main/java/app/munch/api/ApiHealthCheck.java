package app.munch.api;

/**
 * Created by: Fuxing
 * Date: 13/6/18
 * Time: 7:18 PM
 * Project: munch-core
 */
public abstract class ApiHealthCheck {

    /**
     * Health check, implement checking logic and
     * throw an exception if failed
     *
     * @throws Exception for failure
     */
    public abstract void check() throws Exception;
}
