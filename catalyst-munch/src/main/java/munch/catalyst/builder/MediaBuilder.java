package munch.catalyst.builder;

import catalyst.data.CorpusData;
import catalyst.utils.FieldWrapper;
import munch.catalyst.data.ImageMeta;
import munch.catalyst.data.InstagramMedia;
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
public class MediaBuilder implements DataBuilder<InstagramMedia> {
    private static final Logger logger = LoggerFactory.getLogger(MediaBuilder.class);
    private static final Supplier<NullPointerException> NullSupplier = () -> new NullPointerException("Media");
    private static final String CorpusName = "Global.Instagram.Media";

    private List<InstagramMedia> mediaList = new ArrayList<>();

    @Override
    public void consume(CorpusData data) {
        if (!CorpusName.equals(data.getCorpusName())) return;

        FieldWrapper wrapper = new FieldWrapper(data);

        InstagramMedia media = new InstagramMedia();
        media.setCreatedDate(data.getCreatedDate());
        media.setPlaceId(data.getCatalystId());
        media.setMediaId(wrapper.getValue("Instagram.Media.mediaId", NullSupplier));
        media.setCaption(wrapper.getValue("Instagram.Media.caption", NullSupplier));

        InstagramMedia.Profile profile = new InstagramMedia.Profile();
        profile.setUserId(wrapper.getValue("Instagram.Media.userId", NullSupplier));
        profile.setUsername(wrapper.getValue("Instagram.Media.username", NullSupplier));
        profile.setPictureUrl(wrapper.getValue("Instagram.Media.profilePicture", NullSupplier));
        media.setProfile(profile);

        media.setImage(new ImageMeta());
        media.getImage().setImages(new HashMap<>());
        for (CorpusData.Field field : wrapper.getAll("Instagram.Media.images")) {
            int width = Integer.parseInt(field.getMetadata().get("width"));
            int height = Integer.parseInt(field.getMetadata().get("height"));

            // Put images
            ImageMeta.Type type = new ImageMeta.Type();
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
    public List<InstagramMedia> collect(Date updatedDate) {
        mediaList.forEach(media -> media.setUpdatedDate(updatedDate));
        return mediaList;
    }
}
