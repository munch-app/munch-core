package munch.places.data.hibernate;


/**
 * Created By: Fuxing Loh
 * Date: 10/3/2017
 * Time: 2:30 PM
 * Project: munch-core
 */
public class TagsUserType extends PojoUserType<String[]> {

    public TagsUserType() {
        super(String[].class);
    }

}
