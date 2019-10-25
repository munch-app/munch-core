package app.munch.elastic;

import app.munch.model.*;

import javax.inject.Singleton;

/**
 * Use-case:
 * <ul>
 *     <li>Certain env does allow access to elastic client.</li>
 *     <li>A queue for indexing task.</li>
 *     <li>Lower the priority of indexing to prevent upstream bottleneck.</li>
 * </ul>
 *
 * @author Fuxing Loh
 * @since 2019-09-10 at 20:31
 */
@Singleton
public final class ElasticDocumentQueue {

    public void queue(Place place) {
        queue(ElasticDocumentType.PLACE, place.getId());
    }

    public void queue(Tag tag) {
        queue(ElasticDocumentType.TAG, tag.getId());
    }

    public void queue(Article article) {
        queue(ElasticDocumentType.ARTICLE, article.getId());
    }

    public void queue(Mention mention) {
        queue(ElasticDocumentType.MENTION, mention.getId());
    }

    public void queue(Publication publication) {
        queue(ElasticDocumentType.PUBLICATION, publication.getId());
    }

    protected void queue(ElasticDocumentType type, String id) {
        // TODO(fuxing): SQS Service
    }

    // TODO(fuxing): Dequeue Service

    // Not Yet Implemented, Brand & Location
}
