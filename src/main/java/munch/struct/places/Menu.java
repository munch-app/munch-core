package munch.struct.places;

/**
 * Created by: Fuxing
 * Date: 26/3/2017
 * Time: 6:49 AM
 * Project: munch-core
 */
public final class Menu {

    public static final int TYPE_WEBSITE = 10;
    public static final int TYPE_IMAGE = 20;
    public static final int TYPE_PDF = 30;

    private int type;
    private String thumbUrl;
    private String url;

    /**
     * @return type in int
     * @see Menu#TYPE_WEBSITE
     * @see Menu#TYPE_IMAGE
     * @see Menu#TYPE_PDF
     */
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * @return thumbnail url of the image
     */
    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    /**
     * @return internal munch url of resource
     */
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
