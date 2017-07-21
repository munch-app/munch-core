package munch.catalyst.builder.place;

import catalyst.data.CorpusData;
import com.google.common.collect.ImmutableSet;

import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 21/7/2017
 * Time: 3:39 PM
 * Project: munch-core
 */
public class PriorityBuilder extends ValueBuilder {

    private final Set<String> priorityCorpus;

    /**
     * @param corpusNames corpus names to allow for ValueBuilder
     */
    public PriorityBuilder(String... corpusNames) {
        this.priorityCorpus = ImmutableSet.copyOf(corpusNames);
    }

    @Override
    public void add(CorpusData data, CorpusData.Field field) {
        if (priorityCorpus.contains(data.getCorpusName())) {
            super.add(data, field);
        }
    }
}
