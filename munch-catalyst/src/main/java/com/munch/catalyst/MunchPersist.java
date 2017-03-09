package com.munch.catalyst;

import com.corpus.object.GroupObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.munch.struct.Place;
import com.munch.struct.utils.DocumentDatabase;
import com.munch.struct.utils.SearchDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
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

    private final DocumentDatabase documentDatabase;
    private final SearchDatabase searchDatabase;

    /**
     * @param documentDatabase document database
     * @param searchDatabase   search database
     */
    @Inject
    public MunchPersist(DocumentDatabase documentDatabase, SearchDatabase searchDatabase) {
        this.documentDatabase = documentDatabase;
        this.searchDatabase = searchDatabase;
    }

    /**
     * 1. Persist to core documentDatabase
     * 2. Persist to elastic search
     * 3. Persist to bucket for images?
     * 4. Persist to cache?
     *
     * @param list list to persist
     */
    public void persist(List<GroupObject> list) throws Exception {
        final List<Place> places = map(list);
        logger.info("Persisting {} group to document & search.", list.size());

        // Map to list of Place and put
        documentDatabase.put(places);
        searchDatabase.put(places);
    }

    /**
     * List of group to map to place
     *
     * @param list list of group to map
     * @return mapped list of Place
     */
    private List<Place> map(List<GroupObject> list) {
        return list.stream().map(GroupConverter::create)
                .collect(Collectors.toList());
    }
}
