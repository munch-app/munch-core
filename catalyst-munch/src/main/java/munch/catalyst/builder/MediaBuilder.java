package munch.catalyst.builder;

import catalyst.data.CorpusData;
import catalyst.utils.FieldWrapper;
import munch.catalyst.data.Media;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Supplier;

/**
 * Created by: Fuxing
 * Date: 27/6/2017
 * Time: 8:57 PM
 * Project: munch-core
 */
public class MediaBuilder implements DataBuilder<Media> {
    private static final Logger logger = LoggerFactory.getLogger(MediaBuilder.class);
    private static final Supplier<NullPointerException> NullSupplier = () -> new NullPointerException("Media");
    private static final String MediaCorpusName = "Global.Instagram.Media";

    private List<Media> mediaList = new ArrayList<>();

    @Override
    public void consume(CorpusData data) {
        if (!MediaCorpusName.equals(data.getCorpusName())) return;

        FieldWrapper wrapper = new FieldWrapper(data);

        Media media = new Media();
        media.setCreatedDate(data.getCreatedDate());
        media.setPlaceId(data.getCatalystId());
        media.setMediaId(wrapper.getValue("Instagram.Media.mediaId", NullSupplier));
        media.setCaption(wrapper.getValue("Instagram.Media.caption", NullSupplier));

        Media.Profile profile = new Media.Profile();
        profile.setUserId(wrapper.getValue("Instagram.Media.userId", NullSupplier));
        profile.setUsername(wrapper.getValue("Instagram.Media.username", NullSupplier));
        profile.setPictureUrl(wrapper.getValue("Instagram.Media.profilePicture", NullSupplier));
        media.setProfile(profile);

        media.setImage(new Media.Image());
        media.getImage().setImages(new HashMap<>());
        for (CorpusData.Field field : wrapper.getAll("Instagram.Media.images")) {
            int width = Integer.parseInt(field.getMetadata().get("width"));
            int height = Integer.parseInt(field.getMetadata().get("height"));

            // Put images
            Media.Image.Type type = new Media.Image.Type();
            type.setUrl(Objects.requireNonNull(field.getValue()));
            media.getImage().getImages().put(width + "x" + height, type);
        }
        if (media.getImage().getImages().size() < 3) {
            logger.error("Unable to put Media into MediaService due to images not all found: {}",
                    media.getImage().getImages().size());
        }

        // Add to List
        mediaList.add(media);
    }

    @Override
    public List<Media> collect(Date updatedDate) {
        mediaList.forEach(media -> media.setUpdatedDate(updatedDate));
        return mediaList;
    }
}
