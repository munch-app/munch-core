package munch.data.places;

/**
 * Created by: Fuxing
 * Date: 6/9/2017
 * Time: 12:50 AM
 * Project: munch-core
 */
public final class NameCard extends AbstractCard {

    private String name;

    @Override
    public String getId() {
        return "basic_Name_06092017";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
