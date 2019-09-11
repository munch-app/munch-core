package app.munch.elastic.serializer;

import app.munch.model.ElasticDocumentType;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Created by: Fuxing
 * Date: 10/9/19
 * Time: 4:23 pm
 */
public interface Serializer {

    default String createKey(Consumer<Builder> consumer) {
        Builder builder = new Builder();
        consumer.accept(builder);
        return builder.build();
    }

    class Builder {
        private ElasticDocumentType type;
        private String id;
        private String uid;

        public void type(ElasticDocumentType type) {
            this.type = type;
        }

        public void id(String id) {
            this.id = id;
        }

        public void uid(String uid) {
            this.uid = uid;
        }

        public String build() {
            Objects.requireNonNull(type);
            if (type.toString().equals("null")) {
                throw new IllegalStateException("ElasticDocumentType cannot be null.");
            }

            if (StringUtils.isNoneBlank(uid, id)) {
                return id + "_" + uid + "_" + type.toString();
            }

            if (uid != null) {
                return uid + "_" + type.toString();
            }

            if (id != null) {
                return id + "_" + type.toString();
            }

            throw new IllegalStateException("id or uid is required to create elastic key.");
        }
    }
}
