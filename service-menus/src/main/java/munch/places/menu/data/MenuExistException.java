package munch.places.menu.data;


import munch.restful.core.exception.StructuredException;

/**
 * Menu already exist in database
 */
public class MenuExistException extends StructuredException {
    protected MenuExistException() {
        super(400, "MenuAlreadyExistException", "Menu already exist in the database.");
    }
}
