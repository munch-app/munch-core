package munch.catalyst;

import corpus.data.CorpusData;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by: Fuxing
 * Date: 20/8/2017
 * Time: 5:06 AM
 * Project: munch-core
 */
public abstract class AbstractIngress {
    private final Logger logger;

    private final Map<String, Long> counters = new HashMap<>();

    protected AbstractIngress(Logger logger) {
        this.logger = logger;
    }

    /**
     * Ingress corpus data
     *
     * @param dataList data list to ingest
     * @param cycleNo  current cycleNo
     */
    public void ingress(List<CorpusData> dataList, final long cycleNo) {
        if (!validate(dataList)) return;

        // If passed validation, then put
        incrementCount("total");
        put(dataList, cycleNo);
    }

    /**
     * Egress corpus data
     *
     * @param cycleNo current cycleNo
     */
    public void egress(final long cycleNo) {
        counters.forEach((name, count) -> {
            logger.info("Counter Name: {} count: {}", name, count);
        });
        counters.clear();
        delete(cycleNo);
    }

    protected void incrementCount(String name) {
        counters.compute(name, (s, counter) -> {
            if (counter == null) return 1L;
            return counter + 1L;
        });
    }

    protected abstract boolean validate(List<CorpusData> dataList);

    protected abstract void put(List<CorpusData> dataList, final long cycleNo);

    /**
     * deletion of corpus data
     *
     * @param cycleNo current cycleNo
     */
    protected abstract void delete(final long cycleNo);

    /**
     * @param dataList   data list to check
     * @param corpusName corpus name to check
     * @return true if data list contains corpus name
     */
    protected boolean hasCorpusName(List<CorpusData> dataList, String corpusName) {
        return dataList.stream().anyMatch(l -> l.getCorpusName().equals(corpusName));
    }
}
