package app.munch.worker;

import app.munch.image.ImageUploadClient;
import app.munch.model.*;
import com.instagram.err.InstagramException;
import com.instagram.model.InstagramFile;
import com.instagram.model.InstagramMedia;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Date: 2/10/19
 * Time: 9:57 am
 *
 * @author Fuxing Loh
 */
@Singleton
public final class InstagramDataMapper {

    private final ImageUploadClient client;

    @Inject
    InstagramDataMapper(ImageUploadClient client) {
        this.client = client;
    }

    public String mapEid(InstagramMedia media) {
        return media.getId();
    }

    public ProfileMediaType mapType(InstagramMedia instagram) {
        switch (instagram.getType()) {
            case "carousel":
                return ProfileMediaType.INSTAGRAM_ALBUM;

            case "image":
                return ProfileMediaType.INSTAGRAM_PHOTO;

            case "video":
                return ProfileMediaType.INSTAGRAM_VIDEO;

            default:
                throw new IllegalStateException("type: '" + instagram.getType() + "' does not exist.");
        }
    }

    public List<ProfileMedia.Node> mapContent(InstagramMedia instagram) {
        if (instagram.getCaption() == null) {
            return List.of();
        }

        String text = instagram.getCaption().getText();
        if (StringUtils.isBlank(text)) {
            return List.of();
        }

        ProfileMedia.TextNode textNode = new ProfileMedia.TextNode();
        textNode.setText(text);
        return List.of(textNode);
    }

    public ProfileMedia.Metric mapMetric(InstagramMedia instagram) {
        ProfileMedia.Metric metric = new ProfileMedia.Metric();
        metric.setUp(Long.valueOf(instagram.getLikes().getCount()));
        return metric;
    }

    public Date mapCreatedAt(InstagramMedia instagram) {
        long createdAt = instagram.getCreatedTime();
        return new Date(createdAt * 1000);
    }

    public List<Image> mapImages(Profile profile, InstagramMedia instagram) {
        String url = findLargestImage(instagram);
        File file = null;
        try {
            file = downloadFile(url);
            Image image = client.upload(file, ImageSource.INSTAGRAM, entityManager -> {
                return entityManager.find(Profile.class, profile.getUid());
            }, i -> i.setCreatedAt(mapCreatedAt(instagram)));
            return List.of(image);
        } catch (IOException e) {
            throw new InstagramException(e);
        } finally {
            FileUtils.deleteQuietly(file);
        }
    }

    private File downloadFile(String url) throws IOException {
        File file = File.createTempFile(RandomStringUtils.randomAlphanumeric(30),
                "." + FilenameUtils.getExtension(url));
        FileUtils.copyURLToFile(new URL(url), file);
        return file;
    }

    private String findLargestImage(InstagramMedia instagram) {
        Map<String, InstagramFile> files = instagram.getImages();
        return files.values().stream()
                .max(Comparator.comparingInt(InstagramFile::getHeight))
                .map(InstagramFile::getUrl)
                .orElseThrow(() -> {
                    throw new IllegalStateException("findLargestImage unable to find image.");
                });
    }
}
