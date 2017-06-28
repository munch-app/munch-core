package munch.medias;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import munch.medias.hibernate.PojoUserType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
import java.util.Map;

/**
 * https://www.instagram.com/developer/endpoints/media/
 * <p>
 * Created by: Fuxing
 * Date: 2/4/2017
 * Time: 7:16 PM
 * Project: munch-core
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@TypeDefs(value = {
        @TypeDef(name = "profile", typeClass = Media.UserType.User.class),
        @TypeDef(name = "image", typeClass = Media.UserType.Image.class)
})
public final class Media {
    private String placeId;
    private String mediaId;

    private Profile profile;
    private String caption;
    private Image image;

    private Date createdDate;
    private Date updatedDate;

    @Column(columnDefinition = "CHAR(36)", nullable = false, updatable = false)
    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    @Id
    @Column(nullable = false, length = 255)
    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    @Column(nullable = false)
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Column(nullable = false)
    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date putDate) {
        this.updatedDate = putDate;
    }

    @Column(nullable = false, length = 2500)
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Type(type = "image")
    @Column(nullable = false)
    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Column(nullable = false)
    @Type(type = "profile")
    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    /**
     * Owner of the media
     */
    public static class Profile {
        private String userId;
        private String username;
        private String pictureUrl;

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

        public String getPictureUrl() {
            return pictureUrl;
        }

        public void setPictureUrl(String pictureUrl) {
            this.pictureUrl = pictureUrl;
        }
    }

    /**
     * Technically this is a smaller subclass of ImageMeta in munch-images
     * with lesser fields
     */
    public static final class Image {
        private Map<String, Type> images;

        /**
         * @return images type with url
         */
        public Map<String, Type> getImages() {
            return images;
        }

        public void setImages(Map<String, Type> images) {
            this.images = images;
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public final static class Type {
            private String url;

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
                        "url='" + url + '\'' +
                        '}';
            }
        }
    }

    /**
     * Blueprint of Media object
     */
    public static class UserType {
        public static class User extends PojoUserType<User> {
            public User() {
                super(User.class);
            }
        }

        public static class Image extends PojoUserType<Image> {
            public Image() {
                super(Image.class);
            }
        }
    }
}
