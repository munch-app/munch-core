package munch.catalyst.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

/**
 * Created by: Fuxing
 * Date: 2/6/2017
 * Time: 7:52 AM
 * Project: munch-core
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageMeta {
    private String key;
    private Map<String, Type> images;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Map<String, Type> getImages() {
        return images;
    }

    public void setImages(Map<String, Type> images) {
        this.images = images;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public final static class Type {
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
