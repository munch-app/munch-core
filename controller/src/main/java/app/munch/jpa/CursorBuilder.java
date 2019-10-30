package app.munch.jpa;

import dev.fuxing.jpa.EntityStream;
import dev.fuxing.transport.TransportCursor;
import dev.fuxing.utils.JsonUtils;

import java.util.Date;

/**
 * @author Fuxing Loh
 * @since 2019-10-31 at 02:09
 */
public interface CursorBuilder<T, B extends QueryBuilder<T>> extends Builder<T, B> {

    default EntityStream<T> cursorAtIdDesc(TransportCursor cursor, String at, String id) {
        B b1 = with(b -> {
            b.orderDesc(at).orderDesc(id);

            if (!cursor.has(at, id)) {
                return;
            }

            Date atValue = cursor.getDate(at);
            String idValue = cursor.get(id);
            b.criteria.or(
                    b.criteria.lessThan(b.root.get(at), atValue),
                    b.criteria.and(
                            b.criteria.equal(b.root.get(at), atValue),
                            b.criteria.lessThan(b.root.get(id), idValue)
                    )
            );
        });

        return EntityStream.of(b1.asJson())
                .cursor(b1.max, (node, builder) -> {
                    builder.putAll(cursor);

                    builder.put(at, node.path(at).asLong());
                    builder.put(id, node.path(id).asText());
                })
                .map(node -> JsonUtils.toObject(node, b1.clazz));
    }
}
