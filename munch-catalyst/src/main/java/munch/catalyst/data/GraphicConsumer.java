package munch.catalyst.data;

import com.corpus.object.GroupObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 23/4/2017
 * Time: 9:56 PM
 * Project: munch-core
 */
@Singleton
public class GraphicConsumer extends DataConsumer {
    private static final Logger logger = LoggerFactory.getLogger(GraphicConsumer.class);

    @Override
    public void consume(List<GroupObject> list) {
        // TODO once done
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Graphic {
    }
}
