package api.munch.migration;

import app.munch.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.client.util.Lists;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.utils.JsonUtils;
import munch.user.client.CreatorContentClient;
import munch.user.client.CreatorContentItemClient;
import munch.user.data.CreatorContentIndex;
import munch.user.data.CreatorContentItem;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 2019-08-14
 * Time: 10:37
 */
public class ContentMigration {
    @Inject
    public ContentMigration(CreatorContentClient creatorContentClient, CreatorContentItemClient creatorContentItemClient, TransactionProvider transactionProvider) {
        this.creatorContentClient = creatorContentClient;
        this.creatorContentItemClient = creatorContentItemClient;
        this.transactionProvider = transactionProvider;
    }

    private final String creatorId = "00000000-0000-0000-0000-000000000001";
    private final CreatorContentClient creatorContentClient;
    private final CreatorContentItemClient creatorContentItemClient;
    private final TransactionProvider transactionProvider;

    public List<JsonNode> download() {
        List<JsonNode> tree = new ArrayList<>();

        creatorContentClient.list(CreatorContentIndex.updatedMillis, creatorId, null, 30)
                .toIterator(jsonNode -> {
                    return creatorContentClient.list(CreatorContentIndex.updatedMillis, creatorId, jsonNode.path("updatedMillis").asLong(), 30);
                })
                .forEachRemaining(content -> {
                    ArrayList<CreatorContentItem> items = Lists.newArrayList(creatorContentItemClient.iterator(content.getContentId(), 30));
                    ArticleRevision revision = new ArticleRevision();
                    Article article = new Article();
                    revision.setArticle(article);

                    switch (content.getStatus()) {
                        case archived:
                            return;

                        case draft:
                            revision.setPublished(false);
                            break;

                        case published:
                            revision.setPublished(true);
                            break;
                    }

                    revision.setTitle(content.getTitle());
                    revision.setDescription(content.getBody());

                    if (content.getImage() != null) {
                        content.getImage().getSizes().stream().max((o1, o2) -> o1.getHeight())
                                .ifPresent(size -> {
                                    Image image = new Image();
//                                    image.setSizes(Map.of(size.getWidth() + "x" + size.getHeight(), size.getUrl()));
                                    image.setSource(ImageSource.ARTICLE);
                                    revision.setImage(image);
                                });
                    }

                    if (content.getTags() != null) {
                        Set<Tag> tags = content.getTags().stream().map(s -> {
                            Tag tag = new Tag();
                            tag.setType(TagType.UNKNOWN);
                            tag.setName(s.trim());
                            return tag;
                        }).collect(Collectors.toSet());
                        revision.setTags(tags);
                    }

                    List<ArticleModel.Node> nodes = new ArrayList<>();
                    revision.setContent(nodes);
                    for (CreatorContentItem item : items) {
                        JsonNode body = item.getBody();

                        switch (item.getType()) {
                            case title:
                            case h1:
                                ArticleModel.HeadingNode h1 = new ArticleModel.HeadingNode();
                                ArticleModel.HeadingNode.Attrs attrs = new ArticleModel.HeadingNode.Attrs();
                                attrs.setLevel(1);
                                h1.setAttrs(attrs);
                                h1.setContent(JsonUtils.toList(body.path("content"), ArticleModel.TextContent.class));
                                nodes.add(h1);
                                break;
                            case h2:
                                ArticleModel.HeadingNode h2 = new ArticleModel.HeadingNode();
                                attrs = new ArticleModel.HeadingNode.Attrs();
                                attrs.setLevel(2);
                                h2.setContent(JsonUtils.toList(body.path("content"), ArticleModel.TextContent.class));
                                nodes.add(h2);
                                break;

                            case text:
                                ArticleModel.ParagraphNode text = new ArticleModel.ParagraphNode();
                                text.setContent(JsonUtils.toList(body.path("content"), ArticleModel.TextContent.class));
                                nodes.add(text);
                                break;

                            case line:
                                nodes.add(new ArticleModel.LineNode());
                                break;

                            case image:
                                ArticleModel.ImageNode imageNode = new ArticleModel.ImageNode();
                                ArticleModel.ImageNode.Attrs imageAttrs = new ArticleModel.ImageNode.Attrs();
                                imageNode.setAttrs(imageAttrs);

                                imageAttrs.setImage(mapImage(body.path("image")));
                                imageAttrs.setCaption(body.path("caption").asText());

                                nodes.add(imageNode);
                                break;

                            case place:
                                ArticleModel.PlaceNode placeNode = new ArticleModel.PlaceNode();
                                ArticleModel.PlaceNode.Attrs placeAttrs = new ArticleModel.PlaceNode.Attrs();
                                placeNode.setAttrs(placeAttrs);

                                Place place = new Place();
                                place.setDeprecatedId(body.path("placeId").asText());
                                place.setImage(mapImage(body.path("options").path("image")));

                                placeAttrs.setPlace(place);

                                placeAttrs.setOptions(new ArticleModel.PlaceNode.Attrs.Options());
                                placeAttrs.getOptions().setAutoPublish(false);
                                placeAttrs.getOptions().setAutoUpdate(true);

                                nodes.add(placeNode);
                                break;


                            case avatar:
                                ArticleModel.AvatarNode avatarNode = new ArticleModel.AvatarNode();
                                ArticleModel.AvatarNode.Attrs avatarAttrs = new ArticleModel.AvatarNode.Attrs();

                                avatarNode.setAttrs(avatarAttrs);
                                Objects.requireNonNull(body);

                                avatarAttrs.setImage(mapImage(body.path("image")));
                                avatarAttrs.setLine1(body.path("line1").asText());
                                avatarAttrs.setLine2(body.path("line2").asText());

                                nodes.add(avatarNode);
                                break;

                            case html:
                            case quote:
                                break;
                        }
                    }

                    tree.add(JsonUtils.valueToTree(Map.of(
                            "revision", revision,
                            "article", article
                    )));
                });

        return tree;
    }

    private static Image mapImage(JsonNode node) {
        if (!node.isObject()) {
            return null;
        }
        munch.file.Image image1 = JsonUtils.toObject(node, munch.file.Image.class);

        return image1.getSizes().stream().max((o1, o2) -> o1.getHeight())
                .map(size -> {
                    Image image = new Image();
//                    image.setSizes(Map.of(size.getWidth() + "x" + size.getHeight(), size.getUrl()));
                    image.setSource(ImageSource.ARTICLE);
                    return image;
                })
                .orElse(null);
    }

    private void upload() {
        // Using the public facing API:

        // TODO(fuxing): create all tags first
        // TODO(fuxing): loop article
        // TODO(fuxing): look for tags -> copy
        // TODO(fuxing): look for place -> copy
        // TODO(fuxing): look for place image -> copy image
        // TODO(fuxing): look for avatar -> copy image
        // TODO(fuxing): persist image -> copy
    }
}
