package munch.struct.place;

/**
 * Created by: Fuxing
 * Date: 2/4/2017
 * Time: 7:16 PM
 * Project: munch-core
 */
public class Graphic {

    // Internal munch id
    private String id;

    // Instagram media id
    private String mediaId;
    private String imageUrl;

    /**
     * @return unique id of graphic in munch
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return instagram media id
     */
    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    /**
     * @return image url of graphic
     */
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
