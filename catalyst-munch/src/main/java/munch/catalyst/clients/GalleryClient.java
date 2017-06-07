package munch.catalyst.clients;

import catalyst.data.CatalystLink;
import catalyst.data.CorpusData;
import catalyst.utils.FieldWrapper;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import munch.catalyst.data.Media;
import munch.restful.client.RestfulClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Created by: Fuxing
 * Date: 31/5/2017
 * Time: 5:37 AM
 * Project: munch-core
 */
@Singleton
public class GalleryClient extends RestfulClient {
    private static final Logger logger = LoggerFactory.getLogger(GalleryClient.class);
    private static final Supplier<NullPointerException> NullSupplier = () -> new NullPointerException("Media");

    @Inject
    public GalleryClient(@Named("services") Config config) {
        super(config.getString("gallery.url"));
    }

    public void put(CatalystLink link, Date updatedDate) {
        try {
            Media media = create(link, updatedDate);
            doPut("/places/{placeId}/gallery/{mediaId}")
                    .path("placeId", media.getPlaceId())
                    .path("mediaId", media.getMediaId())
                    .body(media)
                    .hasMetaCodes(200);
        } catch (NullPointerException e) {
            logger.error("Unable to put Media into GalleryService due to NPE", e);
        }
    }

    private Media create(CatalystLink link, Date updatedDate) {
        FieldWrapper wrapper = new FieldWrapper(link);

        Media media = new Media();
        media.setPlaceId(link.getCatalystId());
        media.setMediaId(wrapper.getValue("Instagram.Media.mediaId", NullSupplier));
        media.setCaption(wrapper.getValue("Instagram.Media.caption", NullSupplier));

        Media.User user = new Media.User();
        user.setUserId(wrapper.getValue("Instagram.Media.userId", NullSupplier));
        user.setUsername(wrapper.getValue("Instagram.Media.username", NullSupplier));
        user.setPictureUrl(wrapper.getValue("Instagram.Media.profilePicture", NullSupplier));
        media.setUser(user);

        Map<String, Media.Image> images = new HashMap<>();
        for (CorpusData.Field field : wrapper.getAll("Instagram.Media.images")) {
            Media.Image image = new Media.Image();
            image.setUrl(Objects.requireNonNull(field.getValue()));
            image.setWidth(Integer.parseInt(field.getMetadata().get("width")));
            image.setHeight(Integer.parseInt(field.getMetadata().get("height")));
            images.put(Objects.requireNonNull(field.getMetadata().get("type")), image);
        }
        media.setImages(images);
        if (images.size() < 3) throw new NullPointerException("Images not all found.");

        media.setCreatedDate(link.getData().getCreatedDate());
        media.setUpdatedDate(updatedDate);
        return media;
    }

    public void deleteBefore(String catalystId, Date updatedDate) {
        doDelete("/places/{placeId}/gallery/before/{timestamp}")
                .path("placeId", catalystId)
                .path("timestamp", updatedDate.getTime())
                .hasMetaCodes(200);
    }
}
