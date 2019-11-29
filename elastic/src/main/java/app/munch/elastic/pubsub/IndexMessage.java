package app.munch.elastic.pubsub;


import app.munch.model.ElasticDocumentType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.fuxing.pubsub.TransportMessage;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * @author Fuxing Loh
 * @since 2019-10-25 at 17:24
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IndexMessage implements TransportMessage {

    private ElasticDocumentType type;
    private Map<String, String> keys;

    public ElasticDocumentType getType() {
        return type;
    }

    public void setType(ElasticDocumentType type) {
        this.type = type;
    }

    public Map<String, String> getKeys() {
        return keys;
    }

    public void setKeys(Map<String, String> keys) {
        this.keys = keys;
    }

    @Nullable
    @Override
    public String getVersion() {
        return "2019-10-25";
    }
}
