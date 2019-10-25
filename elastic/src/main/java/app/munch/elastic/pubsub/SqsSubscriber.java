package app.munch.elastic.pubsub;

import dev.fuxing.pubsub.TransportMessage;
import dev.fuxing.pubsub.TransportSubscriber;
import dev.fuxing.utils.JsonUtils;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageBatchRequestEntry;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fuxing Loh
 * @since 2019-10-25 at 17:21
 */
public class SqsSubscriber<Message extends TransportMessage> implements TransportSubscriber<Message, software.amazon.awssdk.services.sqs.model.Message> {

    private final SqsClient client;
    private final String url;
    private final Class<Message> clazz;

    public SqsSubscriber(SqsClient client, String url, Class<Message> clazz) {
        this.client = client;
        this.url = url;
        this.clazz = clazz;
    }

    @Override
    public List<software.amazon.awssdk.services.sqs.model.Message> getResponses() {
        ReceiveMessageResponse response = client.receiveMessage(builder -> builder
                .queueUrl(url)
                .maxNumberOfMessages(10)
        );

        return response.messages();
    }

    @Override
    public Message mapMessage(software.amazon.awssdk.services.sqs.model.Message messages) {
        return JsonUtils.toObject(messages.body(), clazz);
    }

    @Override
    public void delete(List<software.amazon.awssdk.services.sqs.model.Message> messages) {
        List<DeleteMessageBatchRequestEntry> entries = messages.stream().map(message -> {
            return DeleteMessageBatchRequestEntry.builder()
                    .id(message.messageId())
                    .receiptHandle(message.receiptHandle())
                    .build();
        }).collect(Collectors.toList());

        client.deleteMessageBatch(builder -> {
            builder.queueUrl(url);
            builder.entries(entries);
        });
    }
}
