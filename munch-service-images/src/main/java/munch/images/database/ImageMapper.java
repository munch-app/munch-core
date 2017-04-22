package munch.images.database;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.munch.utils.file.AccessControl;
import com.munch.utils.file.ContentTypeError;
import com.munch.utils.file.FileEndpoint;
import com.munch.utils.file.FileMapper;
import com.squareup.pollexor.Thumbor;
import com.squareup.pollexor.ThumborUrlBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Image mapper consist of all the operations to upload and resize images
 * <p>
 * Created by: Fuxing
 * Date: 18/4/2017
 * Time: 10:38 PM
 * Project: munch-core
 */
@Singleton
public class ImageMapper {
    private static final Logger logger = LoggerFactory.getLogger(ImageMapper.class);

    private final FileMapper fileMapper;
    private final FileEndpoint fileEndpoint; // For different endpoint configure then default
    private final ImageDatabase database;

    private final Thumbor thumbor;
    private final TikaConfig tika;

    @Inject
    public ImageMapper(FileMapper fileMapper, FileEndpoint fileEndpoint, ImageDatabase database, Thumbor thumbor) {
        this.fileMapper = fileMapper;
        this.fileEndpoint = fileEndpoint;
        this.database = database;
        this.thumbor = thumbor;
        this.tika = TikaConfig.getDefaultConfig();
    }

    /**
     * Helper method for Set of ImageKind
     *
     * @see ImageMapper#upload(InputStream, long, String, ImageKind...)
     */
    public Image upload(InputStream inputStream, long length, String contentType,
                        Set<ImageKind> kinds) throws ContentTypeError, IOException {
        return upload(inputStream, length, contentType, kinds.toArray(new ImageKind[kinds.size()]));
    }


    /**
     * Note: key for image will be generated; currently length is 32
     *
     * @param inputStream input stream of file
     * @param length      length of stream
     * @param contentType content type of file
     * @param kinds       types
     * @return newly created Image object
     * @throws ContentTypeError content type cannot be parsed
     * @throws IOException      network error
     */
    public Image upload(InputStream inputStream, long length, String contentType,
                        ImageKind... kinds) throws ContentTypeError, IOException {
        String newKey = RandomStringUtils.randomAlphanumeric(32);

        // Create new empty image object
        Image image = new Image();
        image.setKey(newKey);
        image.setContentType(contentType);
        image.setCreated(new Timestamp(System.currentTimeMillis()));
        image.setKinds(new HashSet<>());

        // Add original image kind
        Image.Kind original = new Image.Kind();
        original.setKind(ImageKind.Original);
        original.setKey(newKey + getExtension(contentType));
        image.getKinds().add(original);

        // Persist Original Size
        fileMapper.put(original.getKey(), inputStream, length, contentType, AccessControl.PublicRead);
        database.put(image);

        // Appends new kinds of images
        return resize(newKey, kinds);
    }

    /**
     * Helper method for Set of ImageKind
     *
     * @see ImageMapper#resize(String, ImageKind...)
     */
    public Image resize(String key, Set<ImageKind> kinds) throws ContentTypeError, IOException {
        return resize(key, kinds.toArray(new ImageKind[kinds.size()]));
    }

    /**
     * Need thumbor instance for resizing operations
     *
     * @param key   key of image to append kinds to
     * @param kinds kinds to append
     * @return kind, null if cannot be found
     * @throws ContentTypeError content type error for resizing image kinds; should never happen
     * @throws IOException      network error
     */
    public Image resize(String key, ImageKind... kinds) throws ContentTypeError, IOException {
        Image image = database.get(key);
        if (image == null) return null;

        // No kinds to add; exit
        if (kinds.length == 0) return image;

        // Generate kinds to append
        Set<ImageKind> existingKinds = image.getKinds().stream()
                .map(Image.Kind::getKind)
                .collect(Collectors.toSet());
        Set<ImageKind> appendKinds = Arrays.stream(kinds)
                .filter(kind -> !existingKinds.contains(kind))
                .collect(Collectors.toSet());

        // No kinds to append; exit
        if (appendKinds.isEmpty()) return image;

        // Extract original for resizing operations
        @SuppressWarnings("ConstantConditions") Image.Kind original = image.getKinds().stream()
                .filter(kind -> kind.getKind() == ImageKind.Original).findFirst().get();
        String originalUrl = fileMapper.getUrl(original.getKey());

        // Iterate and save all image kind
        for (ImageKind appendKind : appendKinds) {
            Image.Kind kind = new Image.Kind(appendKind);
            String appendKey = appendKind.makeKey(key, getExtension(image.getContentType()));
            String appendUrl = thumbor.buildImage(originalUrl)
                    .resize(appendKind.getWidth(), appendKind.getHeight())
                    .fitIn(ThumborUrlBuilder.FitInStyle.FULL).toUrl();

            // Save
            File appendFile = File.createTempFile(appendKey, "");
            FileUtils.copyURLToFile(new URL(appendUrl), appendFile);
            fileMapper.put(appendKey, appendFile, image.getContentType(), AccessControl.PublicRead);

            // Add to image
            kind.setKey(appendKey);
            image.getKinds().add(kind);
        }

        // Finally save image object
        database.put(image);
        return image;
    }

    /**
     * @param key   image key
     * @param kinds kinds of images to resolve url
     * @return image with the given key
     */
    @Nullable
    public Image get(String key, Set<ImageKind> kinds) {
        Image image = database.get(key);
        if (image == null) return null;
        if (!kinds.isEmpty()) {
            image.getKinds().removeIf(kind -> !kinds.contains(kind.getKind()));
        }
        resolveUrl(image);
        return image;
    }

    /**
     * @param keys  list of image keys
     * @param kinds kinds of images to resolve url
     * @return List of images from the given keys
     */
    public List<Image> get(List<String> keys, Set<ImageKind> kinds) {
        List<Image> images = database.get(keys);
        if (!kinds.isEmpty()) {
            for (Image image : images) {
                image.getKinds().removeIf(kind -> !kinds.contains(kind.getKind()));
            }
        }
        images.forEach(this::resolveUrl);
        return images;
    }

    /**
     * @param image resolve all images from key to url
     */
    public void resolveUrl(Image image) {
        for (Image.Kind kind : image.getKinds()) {
            kind.setUrl(fileEndpoint.getUrl(kind.getKey()));
        }
    }

    /**
     * Delete all version of the image first
     * If operation incomplete, half the image deletion will be processed
     *
     * @param key image group key
     */
    public void delete(String key) throws IOException, ContentTypeError {
        Image image = get(key, Collections.emptySet());
        if (image == null) return;
        try {
            // Iterate through all type and delete image
            for (Image.Kind kind : image.getKinds()) {
                fileMapper.remove(kind.getKey());
                image.getKinds().remove(kind);
            }
            database.delete(key);
        } catch (Exception e) {
            logger.warn("Failed delete operation, saving delete progress for key: {}", key);
            database.put(image);
            throw e;
        }
    }

    /**
     * @param contentType Content-Type
     * @return to extension; e.g. png/jpg
     */
    private String getExtension(String contentType) {
        try {
            MimeType mimeType = tika.getMimeRepository().forName(contentType);
            String extension = mimeType.getExtension();
            if (StringUtils.isEmpty(extension))
                throw new RuntimeException("Extension cannot be empty.");
            return extension;
        } catch (MimeTypeException e) {
            throw new RuntimeException(e);
        }
    }
}
