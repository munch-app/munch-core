package munch.catalyst;

import com.corpus.object.GroupObject;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import munch.struct.place.Place;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 15/2/2017
 * Time: 2:56 AM
 * Project: munch-core
 */
@Singleton
public class MunchPersist {
    private static final Logger logger = LoggerFactory.getLogger(MunchPersist.class);

    private final DocumentStore document;
    private final SearchStore search;

    private final Set<String> actives;
    private final Set<String> inActives;

    /**
     * @param document document database
     * @param search   search database
     */
    @Inject
    public MunchPersist(DocumentStore document, SearchStore search) {
        this.document = document;
        this.search = search;

        Config config = ConfigFactory.load().getConfig("munch.catalyst.consumer.group");
        this.actives = ImmutableSet.copyOf(config.getStringList("active"));
        this.inActives = ImmutableSet.copyOf(config.getStringList("inActive"));
    }

    /**
     * List of group to map to place
     *
     * @param list list of group to map
     * @return mapped list of Place
     */
    private <R> List<R> map(List<GroupObject> list, Set<String> status, Function<GroupObject, R> mapper) {
        return list.stream()
                .filter(group -> status.contains(group.getStatus()))
                .map(mapper)
                .collect(Collectors.toList());
    }

    /**
     * 1. Persist to core document
     * 2. Persist to elastic search
     * 3. Persist to bucket for images?
     * 4. Persist to cache?
     *
     * @param list list to persist
     */
    public void persist(List<GroupObject> list) throws Exception {
        if (list.isEmpty()) return;
        // Delete
        final List<String> deletes = map(list, inActives, GroupObject::getGroupKey);
        logger.info("Deleting {} group to document & search.", deletes.size());
        search.delete(deletes);
        document.delete(deletes);

        // Persist
        final List<Place> puts = map(list, actives, GroupConverter::create);
        logger.info("Persisting {} group to document & search.", puts.size());
        document.put(puts);
        try {
            search.put(puts);
        } catch (Exception e) {
            logger.info("Rolling back document update because search put failed. {}", e);
            List<String> keys = puts.stream().map(Place::getId).collect(Collectors.toList());
            document.delete(keys);
            throw e;
        }
    }
}
