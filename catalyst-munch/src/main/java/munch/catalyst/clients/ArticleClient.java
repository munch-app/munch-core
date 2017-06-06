package munch.catalyst.clients;

import catalyst.data.CatalystLink;
import catalyst.data.CorpusData;
import catalyst.utils.FieldWrapper;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
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
    public ArticleClient(@Named("services") Config config) {
        super(config.getString("articles.url"));
    }

    public void put(CatalystLink link, Date updatedDate) {
        try {
            Article article = create(link, updatedDate);
            doPut("/places/{placeId}/articles/put")
                    .path("placeId", article.getPlaceId())
                    .body(article)
                    .hasMetaCodes(200);
        } catch (NullPointerException e) {
            logger.error("Unable to put Media into ArticleClient due to NPE", e);
        }
    }

    private Article create(CatalystLink link, Date updatedDate) {
        FieldWrapper wrapper = new FieldWrapper(link);

        Article article = new Article();
        article.setPlaceId(link.getCatalystId());
        article.setArticleId(link.getData().getCorpusKey());

        article.setBrand(wrapper.getValue("Article.brand", NullSupplier));
        article.setUrl(wrapper.getValue("Article.url", NullSupplier));

        article.setTitle(wrapper.getValue("Article.title", NullSupplier));
        article.setDescription(wrapper.getValue("Article.description", NullSupplier));

        // Collect images
        List<Article.ArticleImage> images = wrapper.getAll("Article.images").stream()
                .map(CorpusData.Field::getValue)
                .filter(StringUtils::isNotBlank)
                .map(Article.ArticleImage::new)
                .collect(Collectors.toList());
        article.setImages(images);

        article.setCreatedDate(link.getData().getCreatedDate());
        article.setUpdatedDate(updatedDate);
        return article;
    }

    public void deleteBefore(String catalystId, Date updatedDate) {
        doDelete("/places/{placeId}/articles/before/{timestamp}")
                .path("placeId", catalystId)
                .path("timestamp", updatedDate.getTime())
                .hasMetaCodes(200);
    }
}
