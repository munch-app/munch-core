package munch.restful.client.exception;

/**
 * Created By: Fuxing Loh
 * Date: 18/3/2017
 * Time: 5:51 PM
 * Project: munch-core
 */
public class StructuredException extends RestfulException {

    private final int code;
    private final String type;
    private final String detailed;

    /**
     * @param code    error code/status
     * @param type    type
     * @param message message
     */
    public StructuredException(String message, int code, String type) {
        this(code, type, message, null);
    }

    /**
     * Structured exception from meta json body
     *
     * @param code     error code/status
     * @param type     type
     * @param message  message
     * @param detailed detailed message, can be stack trace
     */
    public StructuredException(int code, String type, String message, String detailed) {
        super(message);
        this.code = code;
        this.type = type;
        this.detailed = detailed;
    }

    public int getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public String getDetailed() {
        return detailed;
    }
}
