package app.munch.elastic;

import app.munch.model.ElasticDocumentType;

import java.util.Date;

/**
 * Created by: Fuxing
 * Date: 10/9/19
 * Time: 8:31 pm
 */
public class ElasticDocumentQueue {
    /*
     * Future: certain env does allow access to elastic env, there need to be a way to queue indexing task
     * This class will facilitate it.
     */

    // TODO(fuxing): PlaceEntityManager -> Auto queue?
    // TODO(fuxing): controllers -> to manage entities

    public void queue(ElasticDocumentType type, String id, Date date) {

    }
}
