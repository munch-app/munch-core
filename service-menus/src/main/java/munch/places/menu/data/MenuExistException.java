package munch.places.menu.data;

import munch.restful.server.exceptions.StructuredException;

/**
 * Menu already exist in database
 */
public class MenuExistException extends StructuredException {
    protected MenuExistException() {
        super("MenuAlreadyExistException", "Menu already exist in the database.", 400);
    }
}
