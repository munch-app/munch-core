package com.catalyst;

import com.corpus.object.GroupObject;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 15/2/2017
 * Time: 2:59 AM
 * Project: munch-core
 */
public interface GroupPersistStore {

    void persist(List<GroupObject> list);

}
