package munch.catalyst.builder;

import catalyst.utils.FieldWrapper;
import corpus.blob.ImageMapper;
import corpus.data.CorpusData;
import munch.data.Article;
import munch.data.ImageMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

/**
 * Created by: Fuxing
 * Date: 27/6/2017
 * Time: 8:57 PM
 * Project: munch-core
 */
public class ArticleBuilder implements DataBuilder<Article> {
    private static final Logger logger = LoggerFactory.getLogger(ArticleBuilder.class);
    private static final Supplier<NullPointerException> NullSupplier = () -> new NullPointerException("Article");

    private List<Article> articleList = new ArrayList<>();

    private final ImageMapper imageMapper;

    @Inject
    public ArticleBuilder(ImageMapper imageMapper) {
        this.imageMapper = imageMapper;
    }

    @Override
    public void consume(CorpusData data) {
        if (!data.getCorpusName().equals("Global.Munch.Article")) return;

        FieldWrapper wrapper = new FieldWrapper(data);

        Article article = new Article();
        article.setUpdatedDate(data.getUpdatedDate());
        article.setCreatedDate(data.getCreatedDate());
        article.setPlaceId(data.getCatalystId());
        article.setArticleId(data.getCorpusKey());
        article.setSortKey(Long.toString(data.getCreatedDate().getTime()));

        article.setBrand(wrapper.getValue("Article.brand", NullSupplier));
        article.setUrl(wrapper.getValue("Article.url", NullSupplier));

        // Title and Description are trimmed if they are too long
        article.setTitle(wrapper.getValue("Article.title", 255, NullSupplier));
        article.setDescription(wrapper.getValue("Article.description", 2048, NullSupplier));

        // Next version of article corpus uses thumbnail
        String thumbnail = wrapper.getValue("Article.thumbnail");
        corpus.blob.ImageMeta blobMeta = imageMapper.resolveUrl(thumbnail);
        if (blobMeta != null) {
            ImageMeta imageMeta = new ImageMeta();
            imageMeta.setKey(blobMeta.getKey());
            imageMeta.setImages(blobMeta.getImagesAsSizeUrl());
            article.setThumbnail(imageMeta);
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
