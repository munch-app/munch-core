package com.munch.utils.spark.exceptions;

/**
 * Provide structure to error for Spark Server to read and consume.
 * <p>
 * Created by: Fuxing
 * Date: 10/12/2016
 * Time: 11:31 AM
 * Project: corpus-catalyst
 */
public class StructuredException extends RuntimeException {

    private final String type;
    private final String errorMessage;
    private final int code;

    /**
     * Construct a expected error to throw and response with
     *
     * @param type    error type, short hand for consumer to identify and present their own response
     *                String should be formatted in PascalCase
     * @param message error message, in full readable english sentence for consumer to present
     *                String should be formatted in structured english.
     * @param code    integer code code to response, default to 400
     *                Integer should follow Http Status Codes
     */
    protected StructuredException(String type, String message, int code) {
        this.type = type;
        this.errorMessage = message;
        this.code = code;
    }

    /**
     * Construct a expected error to throw and response with
     * Uses code code 400 as default
     *
     * @param type    error type, short hand for consumer to identify and present their own response
     *                String should be formatted in PascalCase
     * @param message error message, in full readable english sentence for consumer to present
     *                String should be formatted in structured english.
     */
    public StructuredException(String type, String message) {
        this(type, message, 400);
    }

    /**
     * @return error type,
     */
    public String getType() {
        return type;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }

    public int getCode() {
        return code;
    }
}
