package munch.api.services.discover.cards;

import munch.data.structure.Container;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 11/12/2017
 * Time: 4:13 AM
 * Project: munch-core
 */
public final class SearchContainersCard implements SearchCard {

    private Set<String> types;
    private List<Container> containers;

    public SearchContainersCard(List<Container> containers) {
        this.containers = containers;
        this.types = containers.stream()
                .map(Container::getType)
                .collect(Collectors.toSet());
    }

    @Override
    public String getCardId() {
        return "injected_Containers_20171211";
    }

    @Override
    public String getUniqueId() {
        return getCardId();
    }

    public Set<String> getTypes() {
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }

    public List<Container> getContainers() {
        return containers;
    }

    public void setContainers(List<Container> containers) {
        this.containers = containers;
    }
}
