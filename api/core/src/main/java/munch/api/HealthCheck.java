package munch.api;

/**
 * Created by: Fuxing
 * Date: 13/6/18
 * Time: 7:18 PM
 * Project: munch-core
 */
public abstract class HealthCheck {

    /**
     * Health check, throw exception if failed
     *
     * @throws Exception for failure
     */
    protected abstract void check() throws Exception;
}
