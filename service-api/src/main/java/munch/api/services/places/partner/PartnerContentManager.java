package munch.api.services.places.partner;

import munch.article.clients.Article;
import munch.article.clients.ArticleClient;
import munch.corpus.instagram.InstagramMedia;
import munch.corpus.instagram.InstagramMediaClient;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 5/4/18
 * Time: 3:49 PM
 * Project: munch-core
 */
@Singleton
@Deprecated
public final class PartnerContentManager {
    private static final Pattern PATTERN_DESCRIPTION_CLEAN = Pattern.compile(" *(\\\\n|\\\\r|\\\\n\\\\r|\\r)+ *");

    private final InstagramMediaClient mediaClient;
    private final ArticleClient articleClient;

    @Inject
    public PartnerContentManager(InstagramMediaClient mediaClient, ArticleClient articleClient) {
        this.mediaClient = mediaClient;
        this.articleClient = articleClient;
    }

    public PartnerContentResult query(String placeId) {
        return query(placeId, null, null);
    }

    /**
     * @param placeId        place id
     * @param articleMaxSort article place max sort
     * @param mediaMaxSort   media place max sort
     * @return Page of data
     */
    public PartnerContentResult query(String placeId, @Nullable String articleMaxSort, @Nullable String mediaMaxSort) {
        List<InstagramMedia> mediaList = mediaMaxSort == null && articleMaxSort != null ? List.of() : mediaClient.listByPlace(placeId, null, mediaMaxSort, 30);
        List<Article> articleList = articleMaxSort == null && mediaMaxSort != null ? List.of() : articleClient.list(placeId, null, articleMaxSort, 30);

        return new PartnerContentResult(
                capture(mediaList, articleList),
                mediaList.size() == 30 ? mediaList.get(29).getPlaceSort() : null,
                articleList.size() == 30 ? articleList.get(29).getPlaceSort() : null
        );
    }

    @SuppressWarnings("Duplicates")
    private List<PartnerContent> capture(List<InstagramMedia> mediaList, List<Article> articleList) {
        Set<String> authors = new HashSet<>();
        List<PartnerContent> partnerContentList = new ArrayList<>();

        mediaList.forEach(media -> {
            PartnerContent content = parse(media);
            if (content == null) return;
            if (!authors.contains(content.getAuthor())) {
                authors.add(content.getAuthor());
                partnerContentList.add(content);
            }
        });
        articleList.forEach(article -> {
            PartnerContent content = parse(article);
            if (content == null) return;
            if (!authors.contains(content.getAuthor())) {
                authors.add(content.getAuthor());
                partnerContentList.add(content);
            }
        });

        return partnerContentList.stream()
                .sorted(Comparator.comparing(PartnerContent::getDate).reversed())
                .collect(Collectors.toList());
    }

    @Nullable
    public PartnerContent parse(InstagramMedia media) {
        if (media.getImages() == null || media.getImages().isEmpty()) return null;
        if (StringUtils.isBlank(media.getCaption())) return null;

        PartnerContent content = new PartnerContent();
        content.setUniqueId("instagram|" + media.getMediaId());
        content.setType("instagram-media");
        content.setImage(media.getImages());

        content.setDate(new Date(media.getCreatedMillis()));
        content.setAuthor("@" + media.getUsername());
        content.setTitle(null);
        content.setDescription(media.getCaption());

        content.setInstagramMedia(media);
        return content;
    }

    @Nullable
    public PartnerContent parse(Article article) {
        if (article.getThumbnail() == null || article.getThumbnail().isEmpty()) return null;
        String description = cleanDescription(article.getDescription());
        if (StringUtils.isBlank(description)) return null;

        PartnerContent content = new PartnerContent();
        content.setUniqueId("article|" + article.getArticleId());
        content.setType("article");
        content.setImage(article.getThumbnail());

        Date createdDate = article.getCreatedDate();
        content.setDate(createdDate.getTime() == 0 ? null : createdDate);
        content.setAuthor(article.getBrand().toUpperCase());
        content.setTitle(article.getTitle());
        content.setDescription(description);

        content.setArticle(article);
        return content;
    }

    private static String cleanDescription(String description) {
        if (description == null) return null;
        return PATTERN_DESCRIPTION_CLEAN.matcher(description).replaceAll(" ");
    }

    public static class PartnerContentResult {
        public final List<PartnerContent> contents;
        public final String mediaMaxSort;
        public final String articleMaxSort;

        public PartnerContentResult(List<PartnerContent> contents, String mediaMaxSort, String articleMaxSort) {
            this.contents = contents;
            this.mediaMaxSort = mediaMaxSort;
            this.articleMaxSort = articleMaxSort;
        }

        public List<PartnerContent> getContents() {
            return contents;
        }

        public String getMediaMaxSort() {
            return mediaMaxSort;
        }

        public String getArticleMaxSort() {
            return articleMaxSort;
        }
    }
}
