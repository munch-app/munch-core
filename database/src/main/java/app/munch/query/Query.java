package app.munch.query;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.fuxing.utils.JsonUtils;

import java.util.Base64;

/**
 * @author Fuxing Loh
 * @since 2019-11-22 at 13:14
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Query {
    Base64.Encoder ENCODER = Base64.getUrlEncoder().withoutPadding();
    Base64.Decoder DECODER = Base64.getUrlDecoder();

    static <T extends Query> T fromBase64(String base64, Class<T> clazz) {
        byte[] bytes = DECODER.decode(base64);
        return JsonUtils.toObject(bytes, clazz);
    }

    default String toBase64() {
        return toBase64(this);
    }

    static String toBase64(Query query) {
        String json = JsonUtils.toString(query);
        return ENCODER.encodeToString(json.getBytes());
    }
}
