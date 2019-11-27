package app.munch.elastic;

import app.munch.elastic.pubsub.DocumentIndexMessage;
import app.munch.elastic.pubsub.SqsPublisher;
import app.munch.model.*;
import com.typesafe.config.ConfigFactory;
import software.amazon.awssdk.services.sqs.SqsClient;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;

/**
 * Use-case:
 * <ul>
 *     <li>Certain env does allow access to elastic client.</li>
 *     <li>A queue for indexing task.</li>
 *     <li>Lower the priority of indexing to prevent upstream bottleneck.</li>
 * </ul>
 *
 * @author Fuxing Loh
 * @since 2019-09-10 at 20:31
 */
@Singleton
public final class ElasticIndexPublisher extends SqsPublisher<DocumentIndexMessage> {

    @Inject
    public ElasticIndexPublisher(SqsClient client) {
        super(client, ConfigFactory.load().getString("services.sqs.index.url"));
    }

    public void queue(ElasticSerializable serializable) {
        queue(serializable.getElasticDocumentType(), serializable.getElasticDocumentKeys());
    }

    private void queue(ElasticDocumentType type, Map<String, String> keys) {
        DocumentIndexMessage message = new DocumentIndexMessage();
        message.setType(type);
        message.setKeys(keys);
        publish(message);
    }
}
