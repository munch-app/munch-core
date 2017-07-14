package munch.images.database;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.munch.utils.file.AccessControl;
import com.munch.utils.file.ContentTypeError;
import com.munch.utils.file.FileEndpoint;
import com.munch.utils.file.FileMapper;
import com.squareup.pollexor.Thumbor;
import com.squareup.pollexor.ThumborUrlBuilder;
import munch.restful.core.exception.StructuredException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
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
    private static final TikaConfig TIKA = TikaConfig.getDefaultConfig();
    private static final Logger logger = LoggerFactory.getLogger(ImageMapper.class);

    private final FileMapper fileMapper;
    private final FileEndpoint fileEndpoint;
    private final MetaDatabase database;

    private final Thumbor thumbor;

    /**
     * @param fileMapper   file where actual object is stored
     * @param fileEndpoint For different endpoint configure then default
     * @param database     database for image store
     * @param thumbor      thumbor instance for resize
     */
    @Inject
    public ImageMapper(FileMapper fileMapper, FileEndpoint fileEndpoint, MetaDatabase database, Thumbor thumbor) {
        this.fileMapper = fileMapper;
        this.fileEndpoint = fileEndpoint;
        this.database = database;
        this.thumbor = thumbor;
    }

    /**
     * Note: key for image will be generated; currently length is 32
     *
     * @param inputStream input stream to persist
     * @param length      length of stream
     * @param contentType content type of file
     * @param kinds       types of image
     * @return newly created Image object
     * @throws ContentTypeError content type cannot be parsed
     * @throws IOException      network error
     */
    public ImageMeta upload(InputStream inputStream, long length, String contentType,
                            Set<ImageType> kinds) throws ContentTypeError, IOException {
        if (kinds.isEmpty()) throw new ImageKindsEmptyException();
        String newKey = RandomStringUtils.randomAlphanumeric(32);

        // Create new empty image object
        ImageMeta imageMeta = new ImageMeta();
        imageMeta.setKey(newKey);
        imageMeta.setContentType(contentType);
        imageMeta.setImages(new HashMap<>());
        imageMeta.setCreated(new Timestamp(System.currentTimeMillis()));

        // Add original image kind
        ImageMeta.Type original = new ImageMeta.Type();
        original.setKey(newKey + getExtension(contentType));
        original.setUrl(fileEndpoint.getUrl(original.getKey()));
        imageMeta.getImages().put(ImageType.Original, original);
        fileMapper.put(original.getKey(), inputStream, length, contentType, AccessControl.PublicRead);

        // Persist resize different kinds of images
        resize(imageMeta, kinds);

        // Delete Original image if need to (image, storage)
        if (!kinds.contains(ImageType.Original)) {
            imageMeta.getImages().remove(ImageType.Original);
            fileMapper.remove(original.getKey());
        }

        // Finally persist image object to data store
        database.put(imageMeta);
        return imageMeta;
    }

    /**
     * Note: key for image will be generated; currently length is 32
     *
     * @param file        file to persist
     * @param contentType content type of file
     * @param kinds       types of image
     * @return newly created Image object
     * @throws ContentTypeError content type cannot be parsed
     * @throws IOException      network error
     */
    public ImageMeta upload(File file, String contentType,
                            Set<ImageType> kinds) throws ContentTypeError, IOException {
        if (kinds.isEmpty()) throw new ImageKindsEmptyException();
        String newKey = RandomStringUtils.randomAlphanumeric(32);

        // Create new empty image object
        ImageMeta imageMeta = new ImageMeta();
        imageMeta.setKey(newKey);
        imageMeta.setContentType(contentType);
        imageMeta.setImages(new HashMap<>());
        imageMeta.setCreated(new Timestamp(System.currentTimeMillis()));

        // Add original image kind
        ImageMeta.Type original = new ImageMeta.Type();
        original.setKey(newKey + getExtension(contentType));
        original.setUrl(fileEndpoint.getUrl(original.getKey()));
        imageMeta.getImages().put(ImageType.Original, original);
        fileMapper.put(original.getKey(), file, contentType, AccessControl.PublicRead);

        // Persist resize different kinds of images
        resize(imageMeta, kinds);

        // Delete Original image if need to (image, storage)
        if (!kinds.contains(ImageType.Original)) {
            imageMeta.getImages().remove(ImageType.Original);
            fileMapper.remove(original.getKey());
        }

        // Finally persist image object to data store
        database.put(imageMeta);
        return imageMeta;
    }

    /**
     * It's a internal method now
     * - Need thumbor instance for resizing operations
     * - If kind already exist, it won't be generated
     * - Image resize is done with the original image, if original image is deleted, it cannot be done again
     * <p>
     * All resizing will be done on object passed in
     *
     * @param imageMeta image object to resize
     * @param kinds     kinds to append
     * @throws ContentTypeError content type error for resizing image kinds; should never happen
     * @throws IOException      network error
     */
    private void resize(ImageMeta imageMeta, Set<ImageType> kinds) throws ContentTypeError, IOException {
        // No kinds to add; exit
        if (kinds.isEmpty()) return;

        // Generate kinds to append
        Set<ImageType> appendKinds = kinds.stream()
                .filter(kind -> !imageMeta.getImages().containsKey(kind))
                .collect(Collectors.toSet());

        // No kinds to append; exit
        if (appendKinds.isEmpty()) return;

        // Extract original url for resizing operations
        String originalUrl = Optional.ofNullable(imageMeta.getImages().get(ImageType.Original))
                .map(o -> fileMapper.getUrl(o.getKey()))
                .orElseThrow(OriginalNotFoundException::new);

        // Iterate and save all image kind
        for (ImageType appendKind : appendKinds) {
            String appendKey = appendKind.makeKey(imageMeta.getKey(), getExtension(imageMeta.getContentType()));
            String appendUrl = thumbor.buildImage(originalUrl)
                    .resize(appendKind.getWidth(), appendKind.getHeight())
                    .fitIn(ThumborUrlBuilder.FitInStyle.FULL).toUrl();

            // Download new size
            File appendFile = File.createTempFile(appendKey, "");
            FileUtils.copyURLToFile(new URL(appendUrl), appendFile);

            // Save image to meta
            ImageMeta.Type type = new ImageMeta.Type();
            type.setKey(appendKey);
            type.setUrl(fileEndpoint.getUrl(type.getKey()));
            imageMeta.getImages().put(appendKind, type);
            fileMapper.put(appendKey, appendFile, imageMeta.getContentType(), AccessControl.PublicRead);
            FileUtils.deleteQuietly(appendFile);
        }
    }


    /**
     * Delete all version of the image first
     * If operation incomplete, processed kinds will still be deleted
     *
     * @param key image group key
     */
    public void delete(String key) throws IOException, ContentTypeError {
        ImageMeta imageMeta = database.get(key);
        if (imageMeta == null) return;

        // Iterate through all type and delete image
        imageMeta.getImages().forEach((type, image) -> {
            fileMapper.remove(image.getKey());
            imageMeta.getImages().remove(type);
        });
        database.delete(key);
    }

    private static class OriginalNotFoundException extends StructuredException {
        private OriginalNotFoundException() {
            super(500, "OriginalNotFoundException", "Required original image deleted.");
        }
    }

    private static class ImageKindsEmptyException extends StructuredException {
        private ImageKindsEmptyException() {
            super(500, "ImageKindsEmptyException", "Image kinds is not given before upload. Server error.");
        }
    }

    /**
     * @param contentType Content-Type
     * @return to extension; e.g. png/jpg
     */
    private static String getExtension(String contentType) {
        try {
            MimeType mimeType = TIKA.getMimeRepository().forName(contentType);
            String extension = mimeType.getExtension();
            if (StringUtils.isEmpty(extension)) throw new RuntimeException("Extension cannot be empty.");
            return extension;
        } catch (MimeTypeException e) {
            throw new RuntimeException(e);
        }
    }
}
