package munch.api.user.collection;

import munch.restful.core.exception.StructuredException;

/**
 * Created by: Fuxing
 * Date: 15/6/18
 * Time: 7:57 PM
 * Project: munch-core
 */
public class ItemAlreadyExistInPlaceCollection extends StructuredException {
    public ItemAlreadyExistInPlaceCollection(StructuredException e) {
        super(e);
    }

    public ItemAlreadyExistInPlaceCollection(String placeId) {
        super(400, ItemAlreadyExistInPlaceCollection.class, placeId + " already exists inside PlaceCollection.");
    }
}
