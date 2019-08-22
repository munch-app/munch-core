package app.munch.api.v22v23;

import app.munch.image.ImageEntityManager;
import app.munch.manager.ArticleEntityManager;
import app.munch.model.*;
import app.munch.v22v23.PlaceBridge;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.client.util.Lists;
import dev.fuxing.err.ConflictException;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.utils.JsonUtils;
import munch.data.client.PlaceClient;
import munch.user.client.CreatorContentClient;
import munch.user.client.CreatorContentItemClient;
import munch.user.client.CreatorSeriesClient;
import munch.user.client.CreatorSeriesContentClient;
import munch.user.data.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 22/8/19
 * Time: 7:48 pm
 */
@Singleton
public final class CreatorMigration {
    private static final Logger logger = LoggerFactory.getLogger(CreatorMigration.class);

    private final CreatorSeriesClient seriesClient;
    private final CreatorSeriesContentClient seriesContentClient;
    private final CreatorContentClient contentClient;
    private final CreatorContentItemClient itemClient;

    private final TransactionProvider provider;
    private final PlaceBridge placeBridge;
    private final PlaceClient placeClient;
    private final ImageEntityManager imageEntityManager;

    private final ArticleEntityManager articleEntityManager;

    private final String creatorId = "00000000-0000-0000-0000-000000000001";

    Map<String, String> contentIdToArticleId = new HashMap<>();

    Set<List<String>> ID_NAME_USERNAME = Set.of(
            List.of("pCVf0HkPHAmLsaKnNejTgF", "Keane & Hilary", "ahgongahmajiak"),
            List.of("EypmCu7FFViDE2g3Yisi4.", "Calvin Lee", "foodmakescalhappy"),
            List.of("NcMoLulpFrqFy8LnnjS1AF", "Anchelski", "nusfatclub"),
            List.of("vGx7A3lWFQeCMbwbAQiJ_V", "Cathryn Lee", "msoinkee"),
            List.of("EpeIrCeIFLiBy3fQtSD.2k", "Daniel", "foodchiak"),
            List.of("VqJvtv33IFeDfOdldh_RvF", "Qing Xiang", "qingxiangsqx"),
            List.of("0aHM7KNeGu11gz._N.d3nk", "Sheryl", "sherbakes"),
            List.of("vEsXru18FC9N0rNgSxNuYk", "Kenneth Lee", "5meanders"),
            List.of("de9b882f-dc88-4c64-b852-bbdff908ecaf", "Justin Teo", "justinfoodprints"),
            List.of("d502ed48-0d5a-4e4c-8a71-5c036f1e8cb6", "Diana Tan", "dianaaatan"),
            List.of("bb188b42-1f09-4dc9-b9cd-86713e199a1b", "Serene", "serenetomato"),
            List.of("bace2f7e-37db-455d-a9d5-465391a82fc9", "Kenny Tang", "boyz86"),
            List.of("a1f4273e-4d2c-4e30-b259-10695068f081", "Alex", "sqtop"),
            List.of("a12c75e8-079f-410b-af47-5f8565fed84f", "Ryan Chen", "ryancsggluttony"),
            List.of("8431c8bd-7f9d-4ecf-9b40-edbc3b4d7a5c", "Lirong", "lirongs"),
            List.of("4dbbca47-1e22-4d98-98b9-4d0bf4a9227a", "Hilary", "sgnomster"),
            List.of("1a9204ef-ec93-43b8-9665-a9dd722b6610", "Nikolai Wee", "nikolaiwee"),
            List.of("1a47b58a-ec67-4ed0-9132-3daca1ab4df9", "Randy Lim", "randylim27")
    );


    @Inject
    CreatorMigration(CreatorSeriesClient seriesClient, CreatorSeriesContentClient seriesContentClient, CreatorContentClient contentClient, CreatorContentItemClient itemClient, TransactionProvider provider, PlaceBridge placeBridge, PlaceClient placeClient, ImageEntityManager imageEntityManager, ArticleEntityManager articleEntityManager) {
        this.seriesClient = seriesClient;
        this.seriesContentClient = seriesContentClient;
        this.contentClient = contentClient;
        this.itemClient = itemClient;
        this.provider = provider;
        this.placeBridge = placeBridge;
        this.placeClient = placeClient;
        this.imageEntityManager = imageEntityManager;
        this.articleEntityManager = articleEntityManager;


    }

    private Profile mapProfile(EntityManager entityManager, CreatorContent creatorContent) {
        String username = ID_NAME_USERNAME.stream()
                .filter(strings -> {
                    if (strings.get(0).equals(creatorContent.getContentId())) return true;
                    return strings.get(0).equals(CreatorContentPath.toCid(creatorContent.getContentId()));
                })
                .findFirst()
                .map(strings -> strings.get(2))
                .orElse("munch");

        return entityManager.createQuery("FROM Profile " +
                "WHERE username = :username", Profile.class)
                .setParameter("username", username)
                .getSingleResult();
    }

    public void profileCreation() {
        provider.with(entityManager -> {
            Profile munch = new Profile();
            munch.setName("Munch");
            munch.setUsername("munch");
            munch.setBio("Munch Official Account.");
            entityManager.persist(munch);

            ID_NAME_USERNAME.forEach(strings -> {
                Profile profile = new Profile();
                profile.setName(strings.get(1));
                profile.setUsername(strings.get(2));
                entityManager.persist(profile);
            });
        });
    }

    public void run() {
        HashMap<String, String> redirects = new HashMap<>();
        AtomicLong counter = new AtomicLong();

        // Content Migration -> Article
        contentIterator().forEachRemaining(content -> {
            // Only published content get migrated
            if (content.getStatus() != CreatorContent.Status.published) return;

            List<CreatorContentItem> items = Lists.newArrayList(itemClient.iterator(content.getContentId(), 30));
            ArticleRevision revision = contentToArticle(content, items);
            if (revision.getContent().isEmpty()) {
                throw new ConflictException("Empty");
            }

            logger.info("{}: Content: {}, Article: {}", counter.getAndIncrement(), JsonUtils.toString(content), JsonUtils.toString(revision));
            Article article = articleEntityManager.post(revision, entityManager -> {
                return mapProfile(entityManager, content);
            });

            revision.setPublished(true);
            articleEntityManager.post(article.getId(), revision, null);

            contentIdToArticleId.put(content.getContentId(), article.getId());

            // /contents/WPMylzC3F6m1Ztd3ZtA_bV/tiong-bahru-market
            String from = CreatorContentPath.toPath(content);

            // /@username/slug-id
            String to = "/@" + article.getProfile().getUsername() + "/" + article.getSlug() + "-" + article.getId();

            redirects.put(from, to);
        });

        // Series Migration -> Publication
        seriesIterator().forEachRemaining(series -> {
            Publication publication = seriesToPublication(series);
            logger.info("{}: Series: {}, Publication: {}", counter.getAndIncrement(), JsonUtils.toString(series), JsonUtils.toString(publication));

            provider.with(entityManager -> {

                entityManager.persist(publication);
                contentIdToArticleId.put(series.getSeriesId(), publication.getId());
            });

            // Series Content -> Publication Content
            seriesContentIterator(series.getSeriesId()).forEachRemaining(seriesContent -> {
                String articleId = contentIdToArticleId.get(seriesContent.getContentId());
                if (articleId == null) return;

                provider.with(entityManager -> {
                    PublicationArticle article = new PublicationArticle();
                    article.setArticle(entityManager.find(Article.class, articleId));
                    article.setPublication(entityManager.find(Publication.class, publication.getId()));
                    entityManager.persist(article);
                });
            });
        });

        logger.info("Url Redirects: {}", JsonUtils.toString(redirects));
    }

    public ArticleRevision contentToArticle(CreatorContent content, List<CreatorContentItem> items) {
        ArticleRevision article = new ArticleRevision();

        ArticleModel.Options options = new ArticleModel.Options();
        options.setMap(true);
        options.setAds(true);
        article.setOptions(options);

        article.setTitle(StringUtils.substring(content.getTitle(), 0, 100));
        article.setDescription(StringUtils.substring(content.getBody(), 0, 250));

        if (article.getDescription() == null) {
            article.setDescription(StringUtils.substring(content.getSubtitle(), 0, 250));
        }

        // Tags
        article.setTags(mapTags(content));

        // Image
        if (content.getImage() != null) {
            article.setImage(mapImage(content.getImage()));
        }

        // Content
        List<ArticleModel.Node> nodes = new ArrayList<>();
        for (CreatorContentItem item : items) {
            ArticleModel.Node node = itemToNode(item);
            if (node == null) continue;
            nodes.add(node);
        }
        article.setContent(nodes);
        return article;
    }

    public Publication seriesToPublication(CreatorSeries series) {
        Publication publication = new Publication();
        publication.setName(StringUtils.substring(series.getTitle(), 0, 80));
        publication.setDescription(StringUtils.substring(series.getSubtitle(), 0, 250));
        publication.setBody(StringUtils.substring(series.getBody(), 0, 800));
        publication.setTags(Set.of());

        if (publication.getDescription() == null) {
            publication.setDescription(publication.getName());
        }
        return publication;
    }

    public ArticleModel.Node itemToNode(CreatorContentItem item) {
        JsonNode body = item.getBody();

        switch (item.getType()) {
            case title:
            case h1:
                ArticleModel.HeadingNode.Attrs h1Attrs = new ArticleModel.HeadingNode.Attrs();
                h1Attrs.setLevel(1);

                ArticleModel.HeadingNode h1 = new ArticleModel.HeadingNode();
                h1.setAttrs(h1Attrs);
                h1.setContent(JsonUtils.toList(body.path("content"), ArticleModel.TextContent.class));
                return h1;

            case h2:
                ArticleModel.HeadingNode.Attrs h2Attrs = new ArticleModel.HeadingNode.Attrs();
                h2Attrs.setLevel(2);

                ArticleModel.HeadingNode h2 = new ArticleModel.HeadingNode();
                h2.setAttrs(h2Attrs);
                h2.setContent(JsonUtils.toList(body.path("content"), ArticleModel.TextContent.class));
                return h2;

            case text:
                ArticleModel.ParagraphNode paragraph = new ArticleModel.ParagraphNode();
                paragraph.setContent(JsonUtils.toList(body.path("content"), ArticleModel.TextContent.class));
                return paragraph;

            case line:
                return new ArticleModel.LineNode();

            case image:
                ArticleModel.ImageNode.Attrs imageAttrs = new ArticleModel.ImageNode.Attrs();
                imageAttrs.setImage(mapImage(body.path("image")));
                Objects.requireNonNull(imageAttrs.getImage());

                ArticleModel.ImageNode imageNode = new ArticleModel.ImageNode();
                imageNode.setAttrs(imageAttrs);
                return imageNode;

            case place:
                String cid = body.path("placeId").asText();
                Place place = mapPlace(cid);
                if (place == null) {
                    logger.info("Not Found: {}, {}", cid, body);
                    return null;
                }

                ArticlePlace articlePlace = JsonUtils.toObject(JsonUtils.valueToTree(place), ArticlePlace.class);
                articlePlace.setPlace(place);

                Image image = mapImage(body.path("options").path("image"));
                if (image != null) {
                    articlePlace.setImage(image);
                }

                ArticleModel.PlaceNode.Attrs placeAttrs = new ArticleModel.PlaceNode.Attrs();
                placeAttrs.setPlace(articlePlace);

                ArticleModel.PlaceNode placeNode = new ArticleModel.PlaceNode();
                placeNode.setAttrs(placeAttrs);
                return placeNode;


            case avatar:
            case html:
            case quote:
            default:
                return null;
        }
    }

    private Image mapImage(JsonNode node) {
        if (!node.isObject()) {
            return null;
        }

        munch.file.Image fileImage = JsonUtils.toObject(node, munch.file.Image.class);
        return mapImage(fileImage);
    }

    private Image mapImage(munch.file.Image fileImage) {
        List<munch.file.Image.Size> sizes = fileImage.getSizes();

        String url = sizes.stream().max(Comparator.comparing(munch.file.Image.Size::getHeight))
                .map(munch.file.Image.Size::getUrl)
                .orElse(null);

        if (url == null) return null;

        return provider.reduce(entityManager -> {
            try {
                return imageEntityManager.postDeprecated(entityManager, url);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private Place mapPlace(String cid) {
        return provider.reduce(entityManager -> {
            munch.data.place.Place deprecatedPlace = placeClient.get(cid);
            if (deprecatedPlace == null) {
                return null;
            }

            Place place = Optional.of(entityManager.createQuery("FROM Place " +
                    "WHERE cid = :cid", Place.class)
                    .setParameter("cid", cid)
                    .getResultList())
                    .filter(places -> !places.isEmpty())
                    .map(places -> places.get(0))
                    .orElseGet(() -> {

                        Place newPlace = new Place();
                        newPlace.setCid(cid);
                        newPlace.setCreatedAt(new Timestamp(deprecatedPlace.getCreatedMillis()));
                        return newPlace;
                    });

            // Whenever queried it's automatically updated
            placeBridge.bridge(entityManager, place, deprecatedPlace);
            entityManager.persist(place);
            return place;
        });
    }

    private Set<Tag> mapTags(CreatorContent content) {
        if (content.getTags() == null) {
            return Set.of();
        }

        return provider.reduce(entityManager -> {
            return content.getTags()
                    .stream()
                    .map(text -> {
                        List<Tag> list = entityManager.createQuery("FROM Tag " +
                                "WHERE LOWER(name) = :name", Tag.class)
                                .setParameter("name", text.toLowerCase())
                                .getResultList();

                        if (!list.isEmpty()) {
                            return list.get(0);
                        }

                        Tag tag = new Tag();
                        tag.setName(text);
                        tag.setType(TagType.UNKNOWN);
                        entityManager.persist(tag);
                        return tag;
                    }).filter(Objects::nonNull).limit(8).collect(Collectors.toSet());
        });
    }

    public Iterator<CreatorSeries> seriesIterator() {
        return seriesClient.list(CreatorSeriesIndex.updatedMillis, creatorId, null, 30)
                .toIterator(jsonNode -> {
                    return seriesClient.list(CreatorSeriesIndex.updatedMillis, creatorId, jsonNode.path("updatedMillis").asLong(), 30);
                });
    }

    public Iterator<CreatorContent> contentIterator() {
        return contentClient.list(CreatorContentIndex.updatedMillis, creatorId, null, 30)
                .toIterator(jsonNode -> {
                    return contentClient.list(CreatorContentIndex.updatedMillis, creatorId, jsonNode.path("updatedMillis").asLong(), 30);
                });
    }

    public Iterator<CreatorSeriesContent> seriesContentIterator(String seriesId) {
        return seriesContentClient.iterator(CreatorSeriesContentIndex.sortId, seriesId, 30);
    }
}
