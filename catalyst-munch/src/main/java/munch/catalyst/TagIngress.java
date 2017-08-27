package munch.catalyst;

import corpus.data.CorpusData;
import munch.catalyst.builder.TagBuilder;
import munch.catalyst.clients.SearchClient;
import munch.data.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 20/8/2017
 * Time: 5:09 AM
 * Project: munch-core
 */
@Singleton
public final class TagIngress extends AbstractIngress {
    private static final Logger logger = LoggerFactory.getLogger(TagIngress.class);

    private final SearchClient searchClient;

    @Inject
    public TagIngress(SearchClient searchClient) {
        super(logger);
        this.searchClient = searchClient;
    }

    /**
     * Validate a collection of links to make sure it can be a munch place
     * Validated with conditions:
     * 1. NEA Licence
     * 2. Blogger mentions
     *
     * @param dataList links
     * @return true = validated, false = cannot be a munch place
     */
    @Override
    protected boolean validate(List<CorpusData> dataList) {
        // Must have corpus: Sg.Munch.Tags
        return hasCorpusName(dataList, "Sg.Munch.Tags");
    }

    @Override
    protected void put(List<CorpusData> dataList, final long cycleNo) {
        // Else validate = success: put new place
        TagBuilder tagBuilder = new TagBuilder();
        dataList.forEach(tagBuilder::consume);

        // Collect locations
        Date updatedDate = new Timestamp(System.currentTimeMillis());
        for (Tag tag : tagBuilder.collect(updatedDate)) {
            logger.info("Putting tag id: {} name: {}", tag.getId(), tag.getName());
            searchClient.put(tag, cycleNo);
        }
    }

    @Override
    protected void delete(long cycleNo) {
        searchClient.deleteTags(cycleNo);
    }
}
