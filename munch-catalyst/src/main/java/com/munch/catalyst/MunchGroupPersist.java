package com.munch.catalyst;

import com.catalyst.client.GroupPersist;
import com.corpus.object.GroupObject;
import com.munch.hibernate.utils.TransactionProvider;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 15/2/2017
 * Time: 2:56 AM
 * Project: munch-core
 */
public class MunchGroupPersist implements GroupPersist {

    private final TransactionProvider provider;

    /**
     * @param provider core database provider
     */
    public MunchGroupPersist(TransactionProvider provider) {
        this.provider = provider;
    }

    @Override
    public void persist(List<GroupObject> list) {
        // Save for start
        // Persist last item updatedDate and group key in the on list

        // 3. Saving to core database
        // 4. Saving to elastic search
        // 5. Saving to elastic cache?
    }

}
