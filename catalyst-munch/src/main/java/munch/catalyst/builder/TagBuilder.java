package munch.catalyst.builder;

import corpus.data.CorpusData;
import corpus.utils.FieldUtils;
import munch.data.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

/**
 * Created by: Fuxing
 * Date: 9/7/2017
 * Time: 4:36 AM
 * Project: munch-core
 */
public class TagBuilder implements DataBuilder<Tag> {
    private static final Logger logger = LoggerFactory.getLogger(TagBuilder.class);
    private static final Supplier<NullPointerException> NullSupplier = () -> new NullPointerException("Sg.Munch.Tags");
    private static final String CorpusName = "Sg.Munch.Tags";

    private List<Tag> tags = new ArrayList<>();

    @Override
    public void consume(CorpusData data) {
        if (!CorpusName.equals(data.getCorpusName())) return;
        String type = FieldUtils.getValue(data, "type");

        if (type == null || !type.equals("Sg.Munch.Tags")) return;

        Tag tag = new Tag();
        // For Tag, id is not very important since tag should be identified by name instead
        tag.setId(data.getCorpusKey());
        tag.setType(FieldUtils.getValue(data, "Sg.Munch.Tags.type"));
        tag.setName(FieldUtils.getValue(data, "Sg.Munch.Tags.name"));
        tags.add(tag);
    }

    @Override
    public List<Tag> collect(Date updatedDate) {
        return tags;
    }
}
