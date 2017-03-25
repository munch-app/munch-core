package munch.struct;

/**
 * Created by: Fuxing
 * Date: 26/3/2017
 * Time: 6:48 AM
 * Project: munch-core
 */
public final class Media {

    public static final int TYPE_IMAGE = 100;

    public static final int VIEW_BANNER = 1000;

    private int type;
    private int view;
    private String url;

    /**
     * Can be video in the future
     *
     * @return media type
     * @see Media#TYPE_IMAGE
     */
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * Use different image for different image view type
     *
     * @return view location
     * @see Media#VIEW_BANNER
     */
    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

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
