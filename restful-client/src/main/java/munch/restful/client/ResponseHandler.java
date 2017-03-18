package munch.restful.client;

/**
 * Created By: Fuxing Loh
 * Date: 18/3/2017
 * Time: 7:17 PM
 * Project: munch-core
 */
@FunctionalInterface
public interface ResponseHandler<E extends Exception> {

    /**
     * Handle exception with a lambda
     *
     * @param response restful response
     * @throws E exception that is handled
     */
    void handle(RestfulResponse response) throws E;

}
