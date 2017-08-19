package munch.catalyst;

import catalyst.CatalystEngine;
import catalyst.data.CatalystClient;
import catalyst.data.DataClient;
import com.google.inject.Inject;
import corpus.data.CorpusData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 31/5/2017
 * Time: 5:11 AM
 * Project: munch-core
 */
@Singleton
public final class MunchCatalyst extends CatalystEngine {
    private static final Logger logger = LoggerFactory.getLogger(MunchCatalyst.class);

    private final Set<AbstractIngress> ingressSet;

    private long cycleNo;

    @Inject
    public MunchCatalyst(DataClient munchDataClient, CatalystClient catalystClient, Set<AbstractIngress> ingressSet) {
        super(logger, munchDataClient, catalystClient);
        this.ingressSet = ingressSet;
    }

    @Override
    protected void preStart() {
        super.preStart();
        this.cycleNo = System.currentTimeMillis();
    }

    @Override
    protected void process(String catalystId) {
        List<CorpusData> collected = new ArrayList<>();
        dataClient.getLinked(catalystId).forEachRemaining(collected::add);

        for (AbstractIngress ingress : ingressSet) {
            ingress.ingress(collected, cycleNo);
        }
    }

    @Override
    protected void postCycle() {
        for (AbstractIngress ingress : ingressSet) {
            ingress.egress(cycleNo);
        }
    }
}
