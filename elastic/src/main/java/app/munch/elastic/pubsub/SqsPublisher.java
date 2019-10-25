package app.munch.elastic.pubsub;

import dev.fuxing.err.ValidationException;
import dev.fuxing.pubsub.TransportMessage;
import dev.fuxing.pubsub.TransportPublisher;
import dev.fuxing.utils.JsonUtils;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

/**
 * @author Fuxing Loh
 * @since 2019-10-25 at 17:19
 */
public class SqsPublisher<Message extends TransportMessage> implements TransportPublisher<Message, SendMessageRequest.Builder> {

    private final SqsClient client;
    private final String url;

    public SqsPublisher(SqsClient client, String url) {
        this.client = client;
        this.url = url;
    }

    @Override
    public SendMessageRequest.Builder createRequest(Message message) {
        ValidationException.validate(message);

        SendMessageRequest.Builder builder = SendMessageRequest.builder();
        builder.queueUrl(url);
        builder.messageBody(JsonUtils.toString(message));
        return builder;
    }

    @Override
    public void send(Message message, SendMessageRequest.Builder builder) {
        client.sendMessage(builder.build());
    }
}
