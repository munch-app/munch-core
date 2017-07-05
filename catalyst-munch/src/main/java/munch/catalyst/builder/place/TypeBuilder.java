package munch.catalyst.builder.place;

import catalyst.data.CorpusData;

/**
 * Created By: Fuxing Loh
 * Date: 5/7/2017
 * Time: 4:53 PM
 * Project: munch-core
 */
public interface TypeBuilder {

    /**
     * @param data  data owner
     * @param field field in data
     */
    void add(CorpusData data, CorpusData.Field field);

    /**
     * Check if field is the type
     *
     * @param field field to match
     * @return true if field key matched
     */
    boolean match(CorpusData.Field field);

}
