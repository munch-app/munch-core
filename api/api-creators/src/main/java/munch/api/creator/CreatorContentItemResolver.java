package munch.api.creator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import munch.file.Image;
import munch.file.ImageClient;
import munch.file.ImageMeta;
import munch.restful.core.JsonUtils;
import munch.restful.core.KeyUtils;
import munch.user.data.CreatorContent;
import munch.user.data.CreatorContentDraft;
import munch.user.data.CreatorContentItem;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static munch.user.data.CreatorContentItem.Type;

/**
 * Created by: Fuxing
 * Date: 2019-02-10
 * Time: 23:26
 * Project: munch-core
 */
@Singleton
public final class CreatorContentItemResolver {

    private final ImageClient imageClient;

    @Inject
    public CreatorContentItemResolver(ImageClient imageClient) {
        this.imageClient = imageClient;
    }

    public Image resolveImage(String imageId) {
        if (imageId == null) return null;

        ImageMeta image = imageClient.get(imageId);
        if (image == null) return null;

        image.setMeta(null);
        image.setSource(null);
        return image;
    }

    /**
     * @param content to patch with data from draft
     * @param draft   to patch content with
     */
    public void patchContent(CreatorContent content, CreatorContentDraft draft) {
        if (content.getStatus() == CreatorContent.Status.published) return;

        content.setTitle(getTitle(draft));
        content.setBody(getBody(draft));
        content.setImage(getImage(draft));
    }

    public String getTitle(CreatorContentDraft draft) {
        List<CreatorContentItem> items = getItems(draft);
        for (CreatorContentItem item : items) {
            if (noneType(item, Type.title, Type.h1, Type.h2)) continue;

            String text = getText(item, 250);
            if (text != null) return text;
        }

        return null;
    }

    public String getBody(CreatorContentDraft draft) {
        List<CreatorContentItem> items = getItems(draft);
        for (CreatorContentItem item : items) {
            if (noneType(item, Type.text)) continue;

            String text = getText(item, 400);
            if (text != null) return text;
        }

        return null;
    }

    @Nullable
    private static String getText(CreatorContentItem item, int maxSize) {
        JsonNode body = item.getBody();
        Objects.requireNonNull(body);

        StringBuilder builder = new StringBuilder();
        for (JsonNode content : body.path("content")) {
            builder.append(content.path("text").asText());
        }

        String text = StringUtils.substring(builder.toString(), 0, maxSize);
        if (StringUtils.isBlank(text)) return null;
        return text.trim();
    }

    public Image getImage(CreatorContentDraft draft) {
        List<CreatorContentItem> items = getItems(draft);
        for (CreatorContentItem item : items) {
            if (noneType(item, Type.image)) continue;

            Objects.requireNonNull(item.getBody());
            JsonNode imageNode = item.getBody().path("image");
            return JsonUtils.toObject(imageNode, Image.class);
        }

        return null;
    }

    public List<CreatorContentItem> getItems(CreatorContentDraft draft) {
        List<CreatorContentItem> items = new ArrayList<>();

        for (CreatorContentDraft.Node node : draft.getContent()) {
            JsonNode attrs = node.getAttrs();

            switch (node.getType()) {
                case "place":
                    Objects.requireNonNull(attrs);

                    ObjectNode placeNode = JsonUtils.createObjectNode();
                    placeNode.put("placeId", attrs.path("placeId").asText());
                    placeNode.put("placeName", attrs.path("placeName").asText());
                    placeNode.set("options", attrs.path("options"));
                    items.add(newItem(items.size(), Type.place, placeNode));
                    break;

                case "avatar":
                    Objects.requireNonNull(attrs);
                    items.add(newItem(items.size(), Type.avatar, attrs));
                    break;

                case "line":
                    items.add(newItem(items.size(), Type.line, null));
                    break;

                case "image":
                    Objects.requireNonNull(attrs);
                    Image image = JsonUtils.toObject(attrs.path("image"), Image.class);
                    image = resolveImage(image.getImageId());

                    ObjectNode imageNode = JsonUtils.createObjectNode();
                    imageNode.put("caption", attrs.path("caption").asText());
                    imageNode.set("image", JsonUtils.toTree(image));
                    items.add(newItem(items.size(), Type.image, imageNode));
                    break;

                case "heading":
                    if (node.getContent() == null) continue;

                    long level = Objects.requireNonNull(attrs).path("level").asLong();
                    if (items.isEmpty() && level == 1) {
                        items.add(newTextItem(items.size(), Type.title, node));
                    } else if (level == 1) {
                        items.add(newTextItem(items.size(), Type.h1, node));
                    } else {
                        items.add(newTextItem(items.size(), Type.h2, node));
                    }
                    break;

                case "paragraph":
                    if (node.getContent() == null) continue;

                    items.add(newTextItem(items.size(), Type.text, node));
                    break;
            }
        }

        return items;
    }

    private static CreatorContentItem newTextItem(int index, Type type, CreatorContentDraft.Node node) {
        List<CreatorContentDraft.TextContent> content = Objects.requireNonNull(node.getContent());
        ObjectNode objectNode = JsonUtils.createObjectNode();
        objectNode.set("content", JsonUtils.valueToTree(content));
        return newItem(index, type, objectNode);
    }

    private static CreatorContentItem newItem(int index, Type type, JsonNode body) {
        long position = 100_000_000_000L - index;

        CreatorContentItem item = new CreatorContentItem();
        item.setType(type);
        item.setItemId(KeyUtils.createUUID(position, RandomUtils.nextLong()));
        item.setBody(body);
        return item;
    }

    /**
     * @param items to check
     * @return if contains any platform limitations
     */
    @Nullable
    public static String getPlatform(List<CreatorContentItem> items) {
        for (CreatorContentItem item : items) {
            if (item.getType() == Type.html) {
                return "web";
            }
        }

        return null;
    }

    private static boolean noneType(CreatorContentItem item, Type... types) {
        for (Type type : types) {
            if (item.getType() == type) return false;
        }
        return true;
    }
}
