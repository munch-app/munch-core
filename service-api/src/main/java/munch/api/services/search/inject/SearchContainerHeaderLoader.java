package munch.api.services.search.inject;

import munch.api.services.search.cards.SearchContainerHeaderCard;
import munch.data.clients.ContainerClient;
import munch.data.structure.Container;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 12/5/18
 * Time: 2:32 AM
 * Project: munch-core
 */
public final class SearchContainerHeaderLoader implements SearchCardInjector.Loader {

    private final ContainerClient containerClient;

    @Inject
    public SearchContainerHeaderLoader(ContainerClient containerClient) {
        this.containerClient = containerClient;
    }

    @Override
    public List<Position> load(Request request) {
        if (request.getFrom() != 0) return List.of();
        if (!request.hasContainer()) return List.of();

        Container container = request.getQuery().getFilter().getContainers().get(0);
        container = containerClient.get(container.getId());

        SearchContainerHeaderCard card = new SearchContainerHeaderCard();
        card.setName(container.getName());
        card.setAddress(container.getLocation().getAddress());
        card.setLatLng(container.getLocation().getLatLng());
        card.setDescription(container.getDescription());
        card.setHours(container.getHours());
        card.setImages(container.getImages());
        card.setCount(container.getCount());
        return of(-1, card);
    }
}
