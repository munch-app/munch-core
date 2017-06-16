package munch.catalyst.clients;

import catalyst.data.CorpusData;
import catalyst.utils.FieldWrapper;
import com.google.inject.Singleton;
import munch.catalyst.data.Article;
import munch.restful.client.RestfulClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 31/5/2017
 * Time: 5:36 AM
 * Project: munch-core
 */
@Singleton
public class ArticleClient extends RestfulClient {
    private static final Logger logger = LoggerFactory.getLogger(ArticleClient.class);
    private static final Supplier<NullPointerException> NullSupplier = () -> new NullPointerException("Article");

    @Inject
    public ArticleClient(@Named("services.articles.url") String url) {
        super(url);
    }

    public void put(CorpusData data, Date updatedDate) {
        try {
            Article article = create(data, updatedDate);
            doPost("/places/{placeId}/articles/put")
                    .path("placeId", article.getPlaceId())
                    .body(article)
                    .asResponse()
                    .hasCode(200);
        } catch (NullPointerException e) {
            logger.error("Unable to put Media into ArticleClient due to NPE", e);
        }
    }

    private Article create(CorpusData data, Date updatedDate) {
        FieldWrapper wrapper = new FieldWrapper(data);

        Article article = new Article();
        article.setPlaceId(data.getCatalystId());
        article.setArticleId(data.getCorpusKey());

        article.setBrand(wrapper.getValue("Article.brand", NullSupplier));
        article.setUrl(wrapper.getValue("Article.url", NullSupplier));

        // Title and Description are trimmed if they are too long
        article.setTitle(wrapper.getValue("Article.title", NullSupplier));
        if (article.getTitle().length() > 255)
            article.setTitle(article.getTitle().substring(0, 255));

        article.setDescription(wrapper.getValue("Article.description", NullSupplier));
        if (article.getDescription().length() > 2048)
            article.setDescription(article.getDescription().substring(0, 2048));

        // Collect images
        List<Article.ArticleImage> images = wrapper.getAll("Article.images").stream()
                .map(CorpusData.Field::getValue)
                .filter(StringUtils::isNotBlank)
                .map(Article.ArticleImage::new)
                .collect(Collectors.toList());
        article.setImages(images);

        article.setCreatedDate(data.getCreatedDate());
        article.setUpdatedDate(updatedDate);
        return article;
    }

    public void deleteBefore(String catalystId, Date updatedDate) {
        doDelete("/places/{placeId}/articles/before/{timestamp}")
                .path("placeId", catalystId)
                .path("timestamp", updatedDate.getTime())
                .asResponse()
                .hasCode(200);
    }
}
