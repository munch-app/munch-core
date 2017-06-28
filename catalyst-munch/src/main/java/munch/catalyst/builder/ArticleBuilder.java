package munch.catalyst.builder;

import catalyst.data.CorpusData;
import catalyst.utils.FieldWrapper;
import munch.catalyst.data.Article;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 27/6/2017
 * Time: 8:57 PM
 * Project: munch-core
 */
public class ArticleBuilder implements DataBuilder<Article> {
    private static final Logger logger = LoggerFactory.getLogger(ArticleBuilder.class);
    private static final Supplier<NullPointerException> NullSupplier = () -> new NullPointerException("Media");
    public static final Pattern ArticleCorpusName = Pattern.compile("Global\\.Article\\.\\w+");

    private List<Article> articleList = new ArrayList<>();

    @Override
    public void consume(CorpusData data) {
        if (!ArticleCorpusName.matcher(data.getCorpusName()).matches()) return;

        FieldWrapper wrapper = new FieldWrapper(data);

        Article article = new Article();
        article.setCreatedDate(data.getCreatedDate());
        article.setCreatedDate(data.getCreatedDate());
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

        // Next version of article corpus uses thumbnail
        String thumbnail = wrapper.getValue("Article.thumbnail");
        if (StringUtils.isNotBlank(thumbnail)) {
            article.setThumbnail(new Article.Image(thumbnail));
        } else {
            // Deprecated backward compatibility
            List<Article.Image> images = wrapper.getAll("Article.images").stream()
                    .map(CorpusData.Field::getValue)
                    .filter(StringUtils::isNotBlank)
                    .map(Article.Image::new)
                    .collect(Collectors.toList());

            if (!images.isEmpty()) {
                article.setThumbnail(images.get(0));
            }
        }

        // Add to List
        articleList.add(article);
    }

    @Override
    public List<Article> collect(Date updatedDate) {
        articleList.forEach(article -> article.setUpdatedDate(updatedDate));
        return articleList;
    }
}
