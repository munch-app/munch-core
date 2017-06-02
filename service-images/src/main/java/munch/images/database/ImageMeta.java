package munch.images.database;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
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
public final class ImageMeta {
    private String key;
    private String contentType;

    private Set<Type> types;
    private Date created;

    /**
     * @return unique id of the image
     */
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * @return types of image
     */
    public Set<Type> getTypes() {
        return types;
    }

    public void setTypes(Set<Type> types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id='" + key + '\'' +
                ", types=" + types +
                '}';
    }

    /**
     * Image type for group
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public final static class Type {
        private ImageType type;
        private String url;
        private String key;

        public Type() {
        }

        /**
         * @param type type
         */
        public Type(ImageType type) {
            this.type = type;
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
        public ImageType getType() {
            return type;
        }

        public void setType(ImageType type) {
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

        @Override
        public String toString() {
            return "Type{" +
                    "type=" + type +
                    ", url='" + url + '\'' +
                    ", key='" + key + '\'' +
                    '}';
        }
    }

}
