package munch.articles;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.munch.hibernate.utils.TransactionProvider;
import munch.clients.ImageClient;
import munch.clients.ImageMeta;

import java.util.Date;

/**
 * Created by: Fuxing
 * Date: 2/6/2017
 * Time: 7:58 AM
 * Project: munch-core
 */
@Singleton
public final class PersistMapper {

    // TODO persist mapping

    private final ImageClient imageClient;
    private final TransactionProvider provider;

    @Inject
    public PersistMapper(ImageClient imageClient, TransactionProvider provider) {
        this.imageClient = imageClient;
        this.provider = provider;
    }

    public void put(Article article) {
        // Update Article data
        // Update appended images
        // Delete difference images
    }

    public void delete(String articleId) {
        Article article = provider.reduce(em -> em.find(Article.class, articleId));
        delete(article);
    }

    public void deleteBefore(String placeId, Date before) {
        provider.reduce(em -> em.createQuery("FROM Article " +
                "WHERE placeId = :placeId AND updatedDate < :before", Article.class)
                .setParameter("placeId", placeId)
                .setParameter("before", before)
                .getResultList())
                .forEach(this::delete);
    }

    private void delete(Article article) {
        // Delete all the images in article
        for (Article.Image image : article.getImages()) {
            imageClient.delete(image.getKey());
        }

        // Remove Article from Database
        provider.with(em -> em.createQuery("DELETE FROM Article WHERE articleId = :articleId")
                .setParameter("articleId", article)
                .executeUpdate());
    }

    private ImageMeta addImage(String url) {

        return null;
    }
}
