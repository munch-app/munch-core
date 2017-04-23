package munch.catalyst.data;

import com.corpus.object.GroupObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.List;
import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 23/4/2017
 * Time: 10:40 PM
 * Project: munch-core
 */
@Singleton
public class MultiConsumer extends DataConsumer {

    private final Set<DataConsumer> consumers;

    @Inject
    public MultiConsumer(Set<DataConsumer> consumers) {
        this.consumers = consumers;
    }

    @Override
    public void consume(List<GroupObject> list) {
        for (DataConsumer consumer : consumers) {
            consumer.consume(list);
        }
    }

}
