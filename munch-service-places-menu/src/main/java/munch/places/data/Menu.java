package munch.places.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

/**
 * Created by: Fuxing
 * Date: 26/3/2017
 * Time: 6:49 AM
 * Project: munch-core
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Menu {

    private String placeId;
    private String menuId;

    private Date createdDate;

    private Pdf pdf;
    private Image image;
    private Website website;

    public static class Pdf {
        private String key;
        // TODO structure
    }

    public static class Image {
        private String imageKey;

        /**
         * @return image key, stored in munch-images service
         */
        public String getImageKey() {
            return imageKey;
        }

        public void setImageKey(String imageKey) {
            this.imageKey = imageKey;
        }
    }

    public static class Website {
        private String url;
    }
}
