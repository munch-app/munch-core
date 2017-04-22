package munch.places.menu.data;

import munch.restful.server.exceptions.StructuredException;

/**
 * Created by: Fuxing
 * Date: 22/4/2017
 * Time: 4:28 PM
 * Project: munch-core
 */
public class MenuAlreadyExistException extends StructuredException {

    protected MenuAlreadyExistException() {
        super("MenuAlreadyExistException", "Menu already exist in the database.", 400);
    }

}
