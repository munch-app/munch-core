package munch.api.struct;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Set;

/**
 * Public facing munch image for restful communication
 * Created by: Fuxing
 * Date: 21/4/2017
 * Time: 4:45 PM
 * Project: munch-core
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public class Image {
    private String key;
    private Set<Kind> kinds;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Set<Kind> getKinds() {
        return kinds;
    }

    public void setKinds(Set<Kind> kinds) {
        this.kinds = kinds;
    }

    public static class Kind {
        private String kind;
        private String url;

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

}
