package munch.images.database;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Set;

/**
 * This is like a image group, a single image group has multiple types of same images
 * <p>
 * Created by: Fuxing
 * Date: 18/4/2017
 * Time: 10:10 PM
 * Project: munch-core
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Image {

    private String key;
    private Set<Kind> kinds;

    /**
     * @return unique id of the image
     */
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return types of image
     */
    public Set<Kind> getKinds() {
        return kinds;
    }

    public void setKinds(Set<Kind> kinds) {
        this.kinds = kinds;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id='" + key + '\'' +
                ", types=" + kinds +
                '}';
    }

    /**
     * Image type for group
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Kind {
        private ImageKind kind;
        private String url;
        private String key;

        public Kind() {
        }

        /**
         * @param kind kind
         */
        public Kind(ImageKind kind) {
            this.kind = kind;
        }

        /**
         * @return public url of image content
         */
        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        /**
         * @return type of image
         */
        public ImageKind getKind() {
            return kind;
        }

        public void setKind(ImageKind kind) {
            this.kind = kind;
        }

        /**
         * User don't need to know this
         *
         * @return unique actual internal key of image
         */
        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return "Type{" +
                    "type=" + kind +
                    ", url='" + url + '\'' +
                    ", key='" + key + '\'' +
                    '}';
        }
    }

}
