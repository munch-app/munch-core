package munch.api.services.search.cards;

import munch.data.structure.Container;
import munch.data.structure.SourcedImage;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 11/5/18
 * Time: 8:57 PM
 * Project: munch-core
 */
public final class SearchContainerHeaderCard implements SearchCard {

    private String name;
    private String description;

    private String address;
    private String latLng;

    private List<Container.Hour> hours;
    private List<SourcedImage> images;
    private long count;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatLng() {
        return latLng;
    }

    public void setLatLng(String latLng) {
        this.latLng = latLng;
    }

    public List<Container.Hour> getHours() {
        return hours;
    }

    public void setHours(List<Container.Hour> hours) {
        this.hours = hours;
    }

    public List<SourcedImage> getImages() {
        return images;
    }

    public void setImages(List<SourcedImage> images) {
        this.images = images;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @Override
    public String getCardId() {
        return "injected_ContainerHeader_20180511";
    }

    @Override
    public String getUniqueId() {
        return getCardId();
    }
}
