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
import java.util.Set;
import java.util.stream.Collectors;

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

    @Inject
    public PersistMapper(ImageClient imageClient, TransactionProvider provider) {
        this.imageClient = imageClient;
        this.provider = provider;
    }

    public void put(Article article) {
        Article saved = provider.reduce(em -> em.find(Article.class, article.getArticleId()));

        if (saved == null) {
            // Persist new entry of Article
            for (Article.ArticleImage image : article.getImages()) {
                ImageMeta meta = putImage(image.getUrl());
                if (meta == null) continue;
                image.setKey(meta.getKey());
                image.setImages(meta.getImages());
            }
            provider.with(em -> em.persist(article));
        } else {
            // Persist existing entry of Article
            saved.setPlaceId(article.getPlaceId());
            saved.setBrand(article.getBrand());
            saved.setUrl(article.getUrl());
            saved.setTitle(article.getTitle());
            saved.setDescription(article.getDescription());
            saved.setPutDate(article.getPutDate());

            // Filter images to add and delete
            Set<String> existingUrls = Arrays.stream(saved.getImages())
                    .map(Article.ArticleImage::getUrl)
                    .collect(Collectors.toSet());
            Set<String> newUrls = Arrays.stream(article.getImages())
                    .map(Article.ArticleImage::getUrl)
                    .collect(Collectors.toSet());

            // Remove and add into to Set
            Set<Article.ArticleImage> images = Arrays.stream(saved.getImages()).collect(Collectors.toSet());

            // Urls to remove
            images.stream().filter(i -> !newUrls.contains(i.getUrl())).forEach(image -> {
                images.remove(image);
                imageClient.delete(image.getKey());
            });

            // Urls to add
            newUrls.stream().filter(url -> !existingUrls.contains(url)).forEach(url -> {
                ImageMeta meta = putImage(url);
                if (meta != null) {
                    Article.ArticleImage image = new Article.ArticleImage();
                    image.setUrl(url);
                    image.setKey(meta.getKey());
                    image.setImages(meta.getImages());
                    images.add(image);
                }
            });

            // Persist changes
            saved.setImages(images.toArray(new Article.ArticleImage[images.size()]));
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
                "WHERE placeId = :placeId AND putDate < :before", Article.class)
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
        for (Article.ArticleImage image : article.getImages()) {
            imageClient.delete(image.getKey());
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
            logger.error("Skip: Failed to put image for url: {}", urlString, ioe);
            return null;
        }
    }
}
