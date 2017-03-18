package munch.restful.client.exception;

/**
 * Created By: Fuxing Loh
 * Date: 18/3/2017
 * Time: 5:51 PM
 * Project: munch-core
 */
public class StructuredException extends RestfulException {

    private final String type;

    /**
     * Structured exception from meta json body
     *
     * @param type    type
     * @param message message
     */
    public StructuredException(String type, String message) {
        super(message);
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
