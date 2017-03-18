package munch.restful.server.exceptions;

/**
 * Created by: Fuxing
 * Date: 10/12/2016
 * Time: 11:16 AM
 * Project: corpus-catalyst
 */
public final class ParamException extends StructuredException {

    /**
     * Exception should be thrown for
     * Param is Blank
     * Param is Empty
     * Param is not formatted correctly (Number, Boolean)
     * Can be thrown for QueryString, PathParams
     *
     * @param param param that is not available
     */
    public ParamException(String param) {
        super("ParamException", "Parameter " + param + " is required but not processable.");
    }

    /**
     * Exception should be thrown for
     * Param is Blank
     * Param is Empty
     * Param is not formatted correctly (Number, Boolean)
     * Can be thrown for QueryString, PathParams
     *
     * @param params list of param that is not available
     */
    public ParamException(String... params) {
        super("ParamException", "Parameter " + String.join(", ", params) + " is required but not processable.");
    }

}
