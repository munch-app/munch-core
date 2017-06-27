package munch.catalyst.builder;

import catalyst.data.CorpusData;

import java.util.Date;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 27/6/2017
 * Time: 9:01 PM
 * Project: munch-core
 */
public interface DataBuilder<T> {

    /**
     * Consume as many data in a catalyst id
     *
     * @param data data
     */
    void consume(CorpusData data);

    /**
     * @return produced data, can be none
     */
    List<T> collect(Date updatedDate);
}
