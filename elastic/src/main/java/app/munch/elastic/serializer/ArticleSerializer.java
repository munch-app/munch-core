package app.munch.elastic.serializer;

import app.munch.model.Article;
import app.munch.model.ArticleStatus;
import app.munch.model.ElasticDocument;
import app.munch.model.ElasticDocumentType;

import javax.inject.Singleton;

/**
 * @author Fuxing Loh
 * @since 2019-11-25 at 15:11
 */
@Singleton
public final class ArticleSerializer implements Serializer<Article> {
    @Override
    public ElasticDocument serialize(Article article) throws DeleteException {
        ElasticDocument document = new ElasticDocument(ElasticDocumentType.ARTICLE, builder -> {
            builder.type(ElasticDocumentType.ARTICLE);
            builder.id(article.getId());
        });

        if (article.getStatus() != ArticleStatus.PUBLISHED) {
            throw new DeleteException(document);
        }

        document.setSuggest(builder -> {
            builder.type(ElasticDocumentType.ARTICLE);
            builder.input(article.getTitle());
        });

        document.setId(article.getId());

        document.setSlug(article.getSlug());
        document.setName(article.getTitle());
        document.setDescription(article.getDescription());
        document.setImage(article.getImage());
        document.setTags(article.getTags());

        document.setProfile(article.getProfile());

        document.setPublishedAt(article.getPublishedAt());
        document.setUpdatedAt(article.getUpdatedAt());
        document.setCreatedAt(article.getCreatedAt());
        return document;
    }
}
