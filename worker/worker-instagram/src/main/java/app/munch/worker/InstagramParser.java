package app.munch.worker;

import app.munch.model.Image;
import app.munch.model.ProfileMedia;
import app.munch.model.ProfileMediaType;
import com.instagram.model.InstagramMedia;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Singleton;
import java.util.Date;
import java.util.List;

/**
 * Date: 2/10/19
 * Time: 9:57 am
 *
 * @author Fuxing Loh
 */
@Singleton
public final class InstagramParser {

    public ProfileMedia parse(InstagramMedia instagram) {
        ProfileMedia media = new ProfileMedia();
        media.setEid(instagram.getId());
        media.setType(parseType(instagram));
        media.setContent(parseContent(instagram));
        media.setImages(parseImages(instagram));
        media.setMetric(parseMetric(instagram));
        media.setCreatedAt(new Date(instagram.getCreatedTime() * 1000));
        return media;
    }

    private ProfileMediaType parseType(InstagramMedia instagram) {
        switch (instagram.getType()) {
            case "image":
                return ProfileMediaType.INSTAGRAM_PHOTO;

            case "video":
                return ProfileMediaType.INSTAGRAM_VIDEO;

            default:
                throw new IllegalStateException("type: '" + instagram.getType() + "' does not exist.");
        }
    }

    private List<ProfileMedia.Node> parseContent(InstagramMedia instagram) {
        String text = instagram.getCaption().getText();
        if (StringUtils.isBlank(text)) {
            return List.of();
        }

        ProfileMedia.TextNode textNode = new ProfileMedia.TextNode();
        textNode.setText(text);
        return List.of(textNode);
    }

    private List<Image> parseImages(InstagramMedia instagram) {
        // TODO(fuxing):
        return List.of();
    }

    private ProfileMedia.Metric parseMetric(InstagramMedia instagram) {
        ProfileMedia.Metric metric = new ProfileMedia.Metric();
        metric.setUp(Long.valueOf(instagram.getLikes().getCount()));
        return metric;
    }
}
