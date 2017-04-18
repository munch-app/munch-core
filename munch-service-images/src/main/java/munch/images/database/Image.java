package munch.images.database;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

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
    // TODO origin id

    private String id;
    private List<Type> types;

    /**
     * @return unique id of the image
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return types of image
     */
    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
    }

    /**
     * Image type for group
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Type {
        // TODO Allow dynamic image generation?
        private TypeDescription type;
        private String url;
        private String key;

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
        public TypeDescription getType() {
            return type;
        }

        public void setType(TypeDescription type) {
            this.type = type;
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
    }

}