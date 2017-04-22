package munch.places.menu.data.hibernate;


import munch.places.menu.data.Menu;

/**
 * Created By: Fuxing Loh
 * Date: 10/3/2017
 * Time: 2:30 PM
 * Project: munch-core
 */
public class MenuUserType extends PojoUserType<Menu> {

    public MenuUserType() {
        super(Menu.class);
    }

}
