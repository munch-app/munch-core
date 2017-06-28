package munch.articles;

import catalyst.utils.exception.PredicateRetriable;
import catalyst.utils.exception.Retriable;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.munch.hibernate.utils.TransactionProvider;
import munch.clients.ImageClient;
import munch.clients.ImageMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;
import java.util.Date;

/**
 * Created by: Fuxing
 * Date: 2/6/2017
 * Time: 7:58 AM
 * Project: munch-core
 */
@Singleton
public final class PersistMapper {
    private static final Logger logger = LoggerFactory.getLogger(PersistMapper.class);

    private final ImageClient imageClient;
    private final TransactionProvider provider;
    private final Retriable retriable = new PredicateRetriable(6, Duration.ofSeconds(2),
            t -> t.getMessage().contains("Server returned HTTP response code: 403")) {
        @Override
        public void log(Throwable exception, int executionCount) {
        }
    };

    @Inject
    public PersistMapper(ImageClient imageClient, TransactionProvider provider) {
        this.imageClient = imageClient;
        this.provider = provider;
    }

    public Article put(Article article) {
        Article saved = provider.reduce(em -> em.find(Article.class, article.getArticleId()));

        if (saved == null) {
            // Persist new entry of Article
            Article.Image thumbnail = article.getThumbnail();
            ImageMeta meta = putImage(thumbnail.getUrl());
            if (meta != null) {
                thumbnail.setUrl(thumbnail.getUrl());
                thumbnail.setKey(meta.getKey());
                thumbnail.setImages(meta.getImages());
            } else {
                article.setThumbnail(null);
            }
            provider.with(em -> em.persist(article));
            return article;
        } else {
            // Persist existing entry of Article
            saved.setPlaceId(article.getPlaceId());
            saved.setBrand(article.getBrand());
            saved.setUrl(article.getUrl());
            saved.setTitle(article.getTitle());
            saved.setDescription(article.getDescription());
            saved.setUpdatedDate(article.getUpdatedDate());

            // Filter images to add and delete
            Article.Image existing = saved.getThumbnail();
            Article.Image replacing = article.getThumbnail();

            if (existing != null && replacing == null) {
                // Delete existing
                imageClient.delete(existing.getKey());
            } else if (existing == null && replacing != null) {
                // Add replacing
                ImageMeta meta = putImage(replacing.getUrl());
                if (meta != null) {
                    replacing.setUrl(replacing.getUrl());
                    replacing.setImages(meta.getImages());
                    replacing.setKey(meta.getKey());
                    saved.setThumbnail(replacing);
                }
            } else if (existing != null) {
                // Replace existing
                if (!existing.getUrl().equals(replacing.getUrl())) {
                    // Only replace if not same
                    imageClient.delete(existing.getKey());
                    ImageMeta meta = putImage(replacing.getUrl());
                    if (meta != null) {
                        replacing.setUrl(replacing.getUrl());
                        replacing.setKey(meta.getKey());
                        replacing.setImages(meta.getImages());
                        saved.setThumbnail(replacing);
                    }
                }
            }

            // Persist changes
            provider.with(em -> em.merge(saved));
            return saved;
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
        Article.Image thumbnail = article.getThumbnail();
        if (thumbnail != null) {
            imageClient.delete(thumbnail.getKey());
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
            return retriable.loop(() -> {
                URLConnection connection = url.openConnection();
                connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
                try (InputStream inputStream = connection.getInputStream()) {
                    return imageClient.put(inputStream, url.getPath());
                }
            });
        } catch (IOException ioe) {
            logger.warn("Skip: Failed to put image for url: {}, error: {}", urlString, ioe.getMessage());
            return null;
        } catch (ImageClient.NotImageException nie) {
            logger.warn("Skip: Failed to put image for url: {}, error: {}", urlString, nie.getType());
            return null;
        }
    }
}
