package munch.struct;

/**
 * Created by: Fuxing
 * Date: 26/3/2017
 * Time: 6:48 AM
 * Project: munch-core
 */
public final class Image {

    private String imageId;
    private String url;

    /**
     * @return internal url of image
     */
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
