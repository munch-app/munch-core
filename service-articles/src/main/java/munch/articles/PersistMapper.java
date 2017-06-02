package munch.articles;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.munch.hibernate.utils.TransactionProvider;
import munch.clients.ImageClient;
import munch.clients.ImageMeta;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

/**
 * Created by: Fuxing
 * Date: 2/6/2017
 * Time: 7:58 AM
 * Project: munch-core
 */
@Singleton
public final class PersistMapper {
    private static final Logger logger = LoggerFactory.getLogger(PersistMapper.class);

    // TODO persist mapping

    private final ImageClient imageClient;
    private final TransactionProvider provider;

    @Inject
    public PersistMapper(ImageClient imageClient, TransactionProvider provider) {
        this.imageClient = imageClient;
        this.provider = provider;
    }

    public void put(Article article) {
        Article saved = provider.reduce(em -> em.find(Article.class, article.getArticleId()));

        // Delete existing images if size different

        if (saved != null && saved.getImages()) delete(saved);

        if (saved == null) {
            // Persist new entry of Article
            ImageMeta[] images = Arrays.stream(article.getImageUrls())
                    .map(this::putImage)
                    .filter(Objects::nonNull)
                    .toArray(ImageMeta[]::new);
            article.setImages(images);
            provider.with(em -> em.persist(article));
        } else {
            // Persist existing entry of Article

            delete(saved);
            ImageMeta[] images = Arrays.stream(article.getImageUrls())
                    .map(this::putImage)
                    .filter(Objects::nonNull)
                    .toArray(ImageMeta[]::new);
            for (String urlString : request.getImageUrls()) {
//                reduced.getImages()
                putImage(urlString);
            }

            PutRequest.merge(saved, request);
            saved.getImages()
            saved.get
            // Update existing entry
            // Update Article data
            // Update appended images
            // Delete difference images
            provider.with(em -> em.persist(article));
        }
    }

    /**
     * Delete all article with placeId before given date
     *
     * @param placeId placeId
     * @param before  date before
     */
    public void deleteBefore(String placeId, Date before) {
        provider.reduce(em -> em.createQuery("FROM Article " +
                "WHERE placeId = :placeId AND updatedDate < :before", Article.class)
                .setParameter("placeId", placeId)
                .setParameter("before", before)
                .getResultList())
                .forEach(this::delete);
    }

    /**
     * @param article article to delete
     */
    public void delete(Article article) {
        // Delete all the images in article
        for (ImageMeta meta : article.getImages()) {
            imageClient.delete(meta.getKey());
        }

        // Remove Article from Database
        provider.with(em -> em.createQuery("DELETE FROM Article WHERE articleId = :articleId")
                .setParameter("articleId", article)
                .executeUpdate());
    }

    /**
     * @param urlString urlString of Image
     * @return ImageMeta Created, null if failed
     */
    private ImageMeta putImage(String urlString) {
        try {
            URL url = new URL(urlString);
            // Open connect download and return
            URLConnection connection = url.openConnection();
            String contentType = connection.getContentType();
            try (InputStream inputStream = connection.getInputStream()) {
                return imageClient.put(inputStream, ContentType.create(contentType), url.getPath());
            }
        } catch (IOException ioe) {
            logger.error("Skip: Failed to put image for url: {}", urlString, new ImagePutException(urlString));
            return null;
        }
    }
}
