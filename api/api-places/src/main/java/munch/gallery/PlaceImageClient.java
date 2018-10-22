package munch.gallery;

import catalyst.mutation.ImageMutationClient;
import catalyst.mutation.PlaceImageMutation;
import munch.article.ArticleLinkClient;
import munch.article.data.Article;
import munch.instagram.data.InstagramMedia;
import munch.instagram.data.InstagramMediaClient;
import munch.restful.core.NextNodeList;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 9/10/18
 * Time: 3:39 AM
 * Project: munch-core
 */
@Singleton
public final class PlaceImageClient {
    private final ImageMutationClient imageMutationClient;
    private final ArticleLinkClient articleLinkClient;
    private final InstagramMediaClient instagramMediaClient;

    @Inject
    public PlaceImageClient(ImageMutationClient imageMutationClient, ArticleLinkClient articleLinkClient, InstagramMediaClient instagramMediaClient) {
        this.imageMutationClient = imageMutationClient;
        this.articleLinkClient = articleLinkClient;
        this.instagramMediaClient = instagramMediaClient;
    }

    public NextNodeList<PlaceImage> list(String placeId, @Nullable String nextSort, int size) {
        NextNodeList<PlaceImageMutation> images = imageMutationClient.list(placeId, PlaceImageMutation.Type.food, nextSort, size);

        Map<String, Article> articles = getArticles(images);
        Map<String, InstagramMedia> medias = getMedias(images);

        List<PlaceImage> placeImages = images.stream()
                .map(pim -> {
                    PlaceImage image = new PlaceImage();
                    image.setImageId(pim.getImageId());
                    image.setSort(pim.getSort());
                    image.setSizes(pim.getSizes());

                    switch (pim.getSource()) {
                        case "article.munch.api":
                            Article article = articles.get(getArticleId(pim));
                            if (article == null) return null;

                            PlaceImage.Article pArticle = new PlaceImage.Article();
                            pArticle.setArticleId(article.getArticleId());
                            pArticle.setUrl(article.getUrl());
                            pArticle.setDomainId(article.getDomainId());
                            pArticle.setDomain(article.getDomain());

                            image.setArticle(pArticle);
                            image.setTitle(article.getTitle());
                            image.setCaption(article.getContent());
                            image.setCreatedMillis(article.getCreatedMillis());
                            return image;

                        case "media.instagram.com":
                            InstagramMedia media = medias.get(pim.getId());
                            if (media == null) return null;

                            PlaceImage.Instagram pInstagram = new PlaceImage.Instagram();
                            pInstagram.setAccountId(media.getAccountId());
                            pInstagram.setLink(media.getLink());
                            pInstagram.setMediaId(media.getMediaId());
                            pInstagram.setUsername(media.getUser().getUsername());

                            image.setInstagram(pInstagram);
                            image.setTitle(null);
                            image.setCaption(media.getCaption());
                            image.setCreatedMillis(media.getCreatedMillis());
                            return image;

                        default:
                            image.setTitle(null);
                            image.setCaption(null);
                            image.setCreatedMillis(pim.getMillis());
                            return image;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new NextNodeList<>(placeImages, images.getNext());
    }

    private Map<String, Article> getArticles(List<PlaceImageMutation> images) {
        List<String> articleIds = images.stream()
                .filter(pim -> pim.getSource().equals("article.munch.api"))
                .map(PlaceImageClient::getArticleId)
                .distinct()
                .collect(Collectors.toList());

        return articleLinkClient.batchGet(articleIds);
    }

    private Map<String, InstagramMedia> getMedias(List<PlaceImageMutation> images) {
        List<@NotBlank String> mediaIds = images.stream()
                .filter(pim -> pim.getSource().equals("media.instagram.com"))
                .map(PlaceImageMutation::getId)
                .distinct()
                .collect(Collectors.toList());

        return instagramMediaClient.batchGet(mediaIds);
    }

    private static String getArticleId(PlaceImageMutation imageMutation) {
        return imageMutation.getId().substring(0, 43);
    }
}
