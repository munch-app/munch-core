package app.munch.elastic;

import app.munch.elastic.pubsub.DocumentIndexMessage;
import app.munch.elastic.pubsub.SqsPublisher;
import app.munch.model.*;
import com.typesafe.config.ConfigFactory;
import software.amazon.awssdk.services.sqs.SqsClient;

import javax.inject.Inject;
import javax.inject.Singleton;

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

    // TODO(fuxing): queue elastic serializable

    public void queue(Place place) {
        queue(ElasticDocumentType.PLACE, place.getId());
    }

    public void queue(Tag tag) {
        queue(ElasticDocumentType.TAG, tag.getId());
    }

    public void queue(Article article) {
        queue(ElasticDocumentType.ARTICLE, article.getId());
    }

    public void queue(Mention mention) {
        queue(ElasticDocumentType.MENTION, mention.getId());
    }

    public void queue(Publication publication) {
        queue(ElasticDocumentType.PUBLICATION, publication.getId());
    }

    private void queue(ElasticDocumentType type, String id) {
        // TODO(fuxing): Add back when enabled
//        DocumentIndexMessage message = new DocumentIndexMessage();
//        message.setType(type);
//        message.setId(id);
//        publish(message);
    }
}
