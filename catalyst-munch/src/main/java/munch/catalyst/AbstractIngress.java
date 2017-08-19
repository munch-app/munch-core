package munch.catalyst;

import corpus.data.CorpusData;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 20/8/2017
 * Time: 5:06 AM
 * Project: munch-core
 */
public abstract class AbstractIngress {

    /**
     * Ingress corpus data
     *
     * @param dataList data list to ingest
     * @param cycleNo  current cycleNo
     */
    public void ingress(List<CorpusData> dataList, final long cycleNo) {
        if (!validate(dataList)) return;

        // If passed validation, then put
        put(dataList, cycleNo);
    }

    /**
     * Egress corpus data
     *
     * @param cycleNo current cycleNo
     */
    protected abstract void egress(final long cycleNo);

    protected abstract boolean validate(List<CorpusData> dataList);

    protected abstract void put(List<CorpusData> dataList, final long cycleNo);

    /**
     * @param dataList   data list to check
     * @param corpusName corpus name to check
     * @return true if data list contains corpus name
     */
    protected boolean hasCorpusName(List<CorpusData> dataList, String corpusName) {
        return dataList.stream().anyMatch(l -> l.getCorpusName().equals(corpusName));
    }
}
