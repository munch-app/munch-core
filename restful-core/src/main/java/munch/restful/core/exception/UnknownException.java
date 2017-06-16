package munch.restful.core.exception;

/**
 * Created by: Fuxing
 * Date: 16/6/2017
 * Time: 1:27 PM
 * Project: munch-core
 */
public class UnknownException extends StructuredException {
    // TODO

    public UnknownException(int code, String type, String message, Throwable throwable) {
        super(code, type, message, throwable);
    }
}
