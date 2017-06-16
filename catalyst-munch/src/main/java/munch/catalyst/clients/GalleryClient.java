package munch.catalyst.clients;

import catalyst.data.CorpusData;
import catalyst.utils.FieldWrapper;
import com.google.inject.Singleton;
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
    public GalleryClient(@Named("services.gallery.url") String url) {
        super(url);
    }

    public void put(CorpusData data, Date updatedDate) {
        try {
            Media media = create(data, updatedDate);
            doPut("/places/{placeId}/gallery/{mediaId}")
                    .path("placeId", media.getPlaceId())
                    .path("mediaId", media.getMediaId())
                    .body(media)
                    .asResponse()
                    .hasCode(200);
        } catch (NullPointerException e) {
            logger.error("Unable to put Media into GalleryService due to NPE", e);
        }
    }

    private Media create(CorpusData data, Date updatedDate) {
        FieldWrapper wrapper = new FieldWrapper(data);

        Media media = new Media();
        media.setPlaceId(data.getCatalystId());
        media.setMediaId(wrapper.getValue("Instagram.Media.mediaId", NullSupplier));
        media.setCaption(wrapper.getValue("Instagram.Media.caption", NullSupplier));

        Media.Profile profile = new Media.Profile();
        profile.setUserId(wrapper.getValue("Instagram.Media.userId", NullSupplier));
        profile.setUsername(wrapper.getValue("Instagram.Media.username", NullSupplier));
        profile.setPictureUrl(wrapper.getValue("Instagram.Media.profilePicture", NullSupplier));
        media.setProfile(profile);

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

        media.setCreatedDate(data.getCreatedDate());
        media.setUpdatedDate(updatedDate);
        return media;
    }

    public void deleteBefore(String catalystId, Date updatedDate) {
        doDelete("/places/{placeId}/gallery/before/{timestamp}")
                .path("placeId", catalystId)
                .path("timestamp", updatedDate.getTime())
                .asResponse()
                .hasCode(200);
    }
}
