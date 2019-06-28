package munch.sitemap.creator;

import com.google.common.collect.Iterators;
import com.redfin.sitemapgenerator.ChangeFreq;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import munch.restful.core.NextNodeList;
import munch.sitemap.SitemapProvider;
import munch.user.client.CreatorContentClient;
import munch.user.data.CreatorContent;
import munch.user.data.CreatorContentIndex;
import munch.user.data.MunchCreatorId;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.MalformedURLException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 2019-06-28
 * Time: 07:31
 */
@Singleton
public final class CreatorSitemap implements SitemapProvider {
    private static final char[] toBase64URL = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_'
    };

    private static final char[] toCID = {
            '.', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B',
            'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
            'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '_', 'a',
            'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    };

    private static final Base64.Encoder ENCODER = java.util.Base64.getUrlEncoder().withoutPadding();

    private final CreatorContentClient contentClient;

    @Inject
    public CreatorSitemap(CreatorContentClient contentClient) {
        this.contentClient = contentClient;
    }

    @Override
    public String name() {
        return "creators";
    }

    @Override
    public Iterator<WebSitemapUrl> provide() throws MalformedURLException {
        final CreatorContentIndex index = CreatorContentIndex.published;
        NextNodeList<CreatorContent> list = contentClient.list(index, MunchCreatorId.Singapore.creatorId, null, 20);

        Iterator<CreatorContent> iterator = list.toIterator(node -> {
            String s = node.path(index.getRangeName()).asText();
            return contentClient.list(index, MunchCreatorId.Singapore.creatorId, s, 20);
        });


        return Iterators.transform(iterator, content -> {
            Objects.requireNonNull(content);
            return build(buildPath(content), new Date(content.getUpdatedMillis()), 1.0, ChangeFreq.WEEKLY);
        });
    }

    private String buildPath(CreatorContent content) {
        Objects.requireNonNull(content.getTitle());
        String slug = content.getTitle().toLowerCase();
        slug = slug.replaceAll(" ", "-");
        slug = slug.replaceAll("[^0-9a-z-]", "");

        String cid = toCid(content.getContentId());
        return "/contents/" + cid + "/" + slug;
    }

    private static String toCid(String contentId) {
        UUID uuid = UUID.fromString(contentId);
        ByteBuffer uuidBytes = ByteBuffer.wrap(new byte[16]);
        uuidBytes.putLong(uuid.getMostSignificantBits());
        uuidBytes.putLong(uuid.getLeastSignificantBits());
        String base64 = ENCODER.encodeToString(uuidBytes.array());

        return base64.chars()
                .mapToObj(value -> String.valueOf(convertSingleCid((char) value)))
                .collect(Collectors.joining(""));
    }

    private static char convertSingleCid(char single) {
        for (int i = 0; i < toBase64URL.length; i++) {
            char c = toBase64URL[i];
            if (c == single) return toCID[i];
        }

        return 0;
    }

    public static void main(String[] args) {
        String cide = toCid("d3ddbf55-4a1a-4b5f-8056-dde92ae6a931");
        System.out.println(cide);
    }
}
