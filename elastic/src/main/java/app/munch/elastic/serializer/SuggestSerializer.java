package app.munch.elastic.serializer;

import app.munch.model.ElasticDocument;
import app.munch.model.ElasticDocumentType;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Created by: Fuxing
 * Date: 10/9/19
 * Time: 4:00 pm
 */
public interface SuggestSerializer {

    default ElasticDocument.Suggest serializeSuggest(Consumer<Builder> consumer) {
        Builder builder = new Builder();
        consumer.accept(builder);
        return builder.build();
    }

    class Builder {
        private ElasticDocumentType type;
        private String latLng;
        private Set<String> inputs = new HashSet<>();

        public Builder type(ElasticDocumentType type) {
            this.type = type;
            return this;
        }

        public Builder latLng(String latLng) {
            this.latLng = latLng;
            return this;
        }

        public Builder input(String input) {
            this.inputs.add(StringUtils.lowerCase(input));
            return this;
        }

        public Builder inputs(Set<String> inputs) {
            inputs.stream()
                    .map(StringUtils::lowerCase)
                    .forEach(s -> this.inputs.add(s));
            return this;
        }

        ElasticDocument.Suggest build() {
            Objects.requireNonNull(type);

            ElasticDocument.Suggest.Contexts contexts = new ElasticDocument.Suggest.Contexts();
            contexts.setType(type);
            contexts.setLatLng(latLng);

            ElasticDocument.Suggest suggest = new ElasticDocument.Suggest();
            suggest.setInput(inputs);
            suggest.setContexts(contexts);
            return suggest;
        }
    }
}
