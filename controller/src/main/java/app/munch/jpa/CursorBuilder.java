package app.munch.jpa;

import com.fasterxml.jackson.databind.JsonNode;
import dev.fuxing.jpa.EntityStream;
import dev.fuxing.transport.TransportCursor;
import dev.fuxing.utils.JsonUtils;

import java.util.function.Function;

/**
 * @author Fuxing Loh
 * @since 2019-10-31 at 02:09
 */
public interface CursorBuilder<T, B extends QueryBuilder<T>> extends Builder<T, B> {

    default EntityStream<JsonNode> cursorAtIdDesc(TransportCursor cursor, String at, String id) {
        return cursorAtIdDesc(cursor, at, id, JsonUtils::valueToTree);
    }

    default EntityStream<JsonNode> cursorAtIdDesc(TransportCursor cursor, String at, String id, Function<Object, JsonNode> mapper) {
        B b1 = with(b -> {
            b.orderDesc(at).orderDesc(id);

            if (!cursor.has(at, id)) {
                return;
            }

            b.criteria.or(
                    b.criteria.lessThan(b.root.get(at), cursor.getDate(at)),
                    b.criteria.and(
                            b.criteria.equal(b.root.get(at), cursor.getDate(at)),
                            b.criteria.lessThan(b.root.get(id), cursor.get(id))
                    )
            );
        });

        return EntityStream.of(b1.asJson(mapper))
                .cursor(b1.max, (node, builder) -> {
                    builder.putAll(cursor);

                    builder.put(at, node.path(at).asLong());
                    builder.put(id, node.path(id).asText());
                });
    }
}
