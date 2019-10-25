package app.munch.elastic.pubsub;


import app.munch.model.ElasticDocumentType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.fuxing.pubsub.TransportMessage;

import javax.annotation.Nullable;

/**
 * @author Fuxing Loh
 * @since 2019-10-25 at 17:24
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentIndexMessage implements TransportMessage {

    private ElasticDocumentType type;
    private String id;

    public ElasticDocumentType getType() {
        return type;
    }

    public void setType(ElasticDocumentType type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Nullable
    @Override
    public String getVersion() {
        return "2019-10-25";
    }
}
