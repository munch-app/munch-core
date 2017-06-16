package munch.restful.core.exception;

import munch.restful.core.RestfulMeta;
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


    /**
     * @param code    code for error
     * @param type    error type name
     * @param message error message
     */
    protected StructuredException(int code, String type, String message) {
        this(code, type, message, null);
    }

    /**
     * @param code      code for error
     * @param type      error type name
     * @param message   error message
     * @param throwable error stacktrace
     */
    protected StructuredException(int code, String type, String message, Throwable throwable) {
        super(type + ": " + message);
        this.code = code;
        this.type = type;
        this.message = message;

        this.stackTrace = throwable != null ? ExceptionUtils.getStackTrace(throwable) : null;
    }

    public int getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    /**
     * Convert Structured exception to meta to restful use
     *
     * @return RestfulMeta
     */
    public RestfulMeta toMeta() {
        RestfulMeta meta = new RestfulMeta();
        meta.setCode(code);

        RestfulMeta.Error error = new RestfulMeta.Error();
        error.setType(type);
        error.setMessage(message);
        error.setStacktrace(stackTrace);
        meta.setError(error);
        return meta;
    }
}
