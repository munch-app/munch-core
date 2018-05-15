package munch.api.services.search.cards;

import munch.corpus.instagram.InstagramMedia;
import munch.data.structure.Place;

import java.util.List;
import java.util.Map;

/**
 * Created by: Fuxing
 * Date: 1/5/18
 * Time: 10:26 AM
 * Project: munch-core
 */
public final class SearchPartnerInstagramCard implements SearchCard {

    private String userId;
    private String username;
    private String title;
    private List<MediaContent> contents;

    @Override
    public String getCardId() {
        return "injected_PartnerInstagram_20180505";
    }

    @Override
    public String getUniqueId() {
        return getCardId();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<MediaContent> getContents() {
        return contents;
    }

    public void setContents(List<MediaContent> contents) {
        this.contents = contents;
    }

    public static class MediaContent {
        private Place place;

        private String caption;
        private String mediaId;
        private String locationId;
        private Map<String, String> images;

        public MediaContent(Place place, InstagramMedia media) {
            this.place = place;

            this.caption = media.getCaption();
            this.mediaId = media.getMediaId();
            this.locationId = media.getLocationId();
            this.images = media.getImages();
        }

        public Place getPlace() {
            return place;
        }

        public void setPlace(Place place) {
            this.place = place;
        }

        public String getCaption() {
            return caption;
        }

        public void setCaption(String caption) {
            this.caption = caption;
        }

        public String getMediaId() {
            return mediaId;
        }

        public void setMediaId(String mediaId) {
            this.mediaId = mediaId;
        }

        public String getLocationId() {
            return locationId;
        }

        public void setLocationId(String locationId) {
            this.locationId = locationId;
        }

        public Map<String, String> getImages() {
            return images;
        }

        public void setImages(Map<String, String> images) {
            this.images = images;
        }
    }
}
