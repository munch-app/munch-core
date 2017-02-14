package com.catalyst;

import com.catalyst.consumer.ConsumerClient;
import com.corpus.object.GroupObject;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by: Fuxing
 * Date: 15/2/2017
 * Time: 2:56 AM
 * Project: munch-core
 */
public class CatalystConsumer {

    private final GroupPersistStore persistStore;

    private final ConsumerClient client;
    private final int size;

    private Date currentAfterEqDate;
    private String currentAfterKey;

    /**
     * @param url          of client
     * @param size         size of each request
     * @param persistStore persist store to save data to
     */
    public CatalystConsumer(String url, int size, GroupPersistStore persistStore) {
        this.client = new ConsumerClient(url);
        this.size = size;
        this.persistStore = persistStore;
    }

    /**
     * @param afterEqDate starting date from local
     * @param afterKey    starting key form local
     * @return if have next
     * @throws IOException networking error
     */
    public boolean start(Date afterEqDate, String afterKey) throws IOException {
        List<GroupObject> list = client.after(afterEqDate, afterKey, size);
        prepareNext(list);
        save(list);
        // Check list has next
        return list.size() == size;
    }

    /**
     * Run next based on previous saved last
     * Must run start first
     *
     * @return if have next
     * @throws IOException networking error
     */
    private boolean next() throws IOException {
        Objects.requireNonNull(currentAfterEqDate, "Need to start first.");
        List<GroupObject> list = client.after(currentAfterEqDate, currentAfterKey, size);
        prepareNext(list);
        save(list);

        // Check list has next
        return list.size() == size;
    }

    /**
     * save next parameters to memory to prepare for run next
     *
     * @param list to extract from
     */
    private void prepareNext(List<GroupObject> list) {
        if (!list.isEmpty()) {
            GroupObject group = list.get(list.size() - 1);
            this.currentAfterEqDate = group.getUpdatedDate();
            this.currentAfterKey = group.getGroupKey();
        }
    }

    /**
     * @param list list to save to persist store
     */
    private void save(List<GroupObject> list) {
        persistStore.persist(list);
    }

}
