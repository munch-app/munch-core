package app.munch.elastic;

import app.munch.elastic.pubsub.DocumentIndexMessage;
import app.munch.elastic.pubsub.SqsSubscriber;
import com.typesafe.config.ConfigFactory;
import software.amazon.awssdk.services.sqs.SqsClient;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Fuxing Loh
 * @since 2019-10-25 at 17:39
 */
@Singleton
public final class ElasticIndexSubscriber extends SqsSubscriber<DocumentIndexMessage> {

    @Inject
    public ElasticIndexSubscriber(SqsClient client) {
        super(client, ConfigFactory.load().getString("services.sqs.index.url"), DocumentIndexMessage.class);
    }
}
