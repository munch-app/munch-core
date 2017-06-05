package munch.catalyst.clients;

import catalyst.data.CatalystLink;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import munch.restful.client.RestfulClient;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;

/**
 * Created by: Fuxing
 * Date: 31/5/2017
 * Time: 5:37 AM
 * Project: munch-core
 */
@Singleton
public class GalleryClient extends RestfulClient {

    @Inject
    public GalleryClient(@Named("services") Config config) {
        super(config.getString("gallery.url"));
    }

    public void put(CatalystLink link, Date updatedDate) {
        Media media = create(link, updatedDate);
        doPut("/places/{placeId}/gallery/{mediaId}")
                .path("placeId", media.getPlaceId())
                .path("mediaId", media.getMediaId())
                .body(media)
                .hasMetaCodes(200);
    }

    private Media create(CatalystLink link, Date updatedDate) {
        Media media = new Media();
        media.setPlaceId(link.getCatalystId());
        media.setUpdatedDate(updatedDate);

        // TODO media creation
        return media;
    }

    public void deleteBefore(String catalystId, Date updatedDate) {
        doDelete("/places/{placeId}/gallery/before/{timestamp}")
                .path("placeId", catalystId)
                .path("timestamp", updatedDate.getTime())
                .hasMetaCodes(200);
    }
}
