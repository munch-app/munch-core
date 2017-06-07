package munch.api.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.Map;

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
public class ImageMeta {
    private String key;
    private String contentType;

    private Map<String, Type> images;
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
     * @return different types of images
     */
    public Map<String, Type> getImages() {
        return images;
    }

    public void setImages(Map<String, Type> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "ImageMeta{" +
                "key='" + key + '\'' +
                ", contentType='" + contentType + '\'' +
                ", images=" + images +
                ", created=" + created +
                '}';
    }

    /**
     * Image type for group
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public final static class Type {
        private String key;
        private String url;

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

        /**
         * @return public url of image content
         */
        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "Type{" +
                    "key='" + key + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }

}
