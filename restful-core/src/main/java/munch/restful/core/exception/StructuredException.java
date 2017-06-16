package munch.restful.core.exception;

import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * Created by: Fuxing
 * Date: 16/6/2017
 * Time: 12:42 PM
 * Project: munch-core
 */
public class StructuredException extends RuntimeException {

    private final int code;
    private final String type;
    private final String message;

    private final String stackTrace;

    public StructuredException(int code, String type, String message, Throwable throwable) {
        super(type + ": " + message);
        this.code = code;
        this.type = type;
        this.message = message;

        this.stackTrace = throwable != null ? ExceptionUtils.getStackTrace(throwable) : null;
    }

    public StructuredException(int code, String type, String message) {
        this(code, type, message, null);
    }
}
