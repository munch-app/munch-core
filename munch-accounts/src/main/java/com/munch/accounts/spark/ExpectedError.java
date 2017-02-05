package com.munch.accounts.spark;

/**
 * Only for service package to use
 * <p>
 * Created by: Fuxing
 * Date: 10/12/2016
 * Time: 11:31 AM
 * Project: corpus-catalyst
 */
public class ExpectedError extends RuntimeException {

    private final String message;
    private final int status;

    /**
     * Only for service package to use
     *
     * @param message message to put to response body
     * @param status  status code to response
     */
    ExpectedError(String message, int status) {
        this.message = message;
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
