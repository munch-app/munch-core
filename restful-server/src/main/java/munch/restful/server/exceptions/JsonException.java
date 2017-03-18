package munch.restful.server.exceptions;

/**
 * Created by: Fuxing
 * Date: 10/12/2016
 * Time: 11:26 AM
 * Project: corpus-catalyst
 */
public final class JsonException extends StructuredException {

    /**
     * @param cause throwable for actual cause of json exception
     */
    public JsonException(Throwable cause) {
        super("JsonException", cause.getMessage());
    }

}
