package app.munch.api.v22v23;

import munch.user.data.CreatorContent;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 22/8/19
 * Time: 8:48 pm
 */
public class CreatorContentPath {

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

    public static String toPath(CreatorContent content) {
        Objects.requireNonNull(content.getTitle());
        String slug = content.getTitle().toLowerCase();
        slug = slug.replaceAll(" ", "-");
        slug = slug.replaceAll("[^0-9a-z-]", "");

        String cid = toCid(content.getContentId());
        return "/contents/" + cid + "/" + slug;
    }

    public static String toCid(String contentId) {
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
