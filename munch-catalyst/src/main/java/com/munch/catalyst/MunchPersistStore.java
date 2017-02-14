package com.munch.catalyst;

import com.catalyst.GroupPersistStore;
import com.corpus.object.GroupObject;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 15/2/2017
 * Time: 2:56 AM
 * Project: munch-core
 */
public class MunchPersistStore implements GroupPersistStore {

    @Override
    public void persist(List<GroupObject> list) {
        // TODO Future
        // Persist last item updatedDate and group key in the on list
    }

}
