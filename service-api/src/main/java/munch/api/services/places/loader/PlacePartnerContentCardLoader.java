package munch.api.services.places.loader;

import com.fasterxml.jackson.databind.JsonNode;
import munch.api.services.places.partner.PartnerContentManager;
import munch.data.structure.Place;
import munch.data.structure.PlaceJsonCard;
import munch.restful.core.JsonUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

/**
 * Created by: Fuxing
 * Date: 5/4/18
 * Time: 3:47 PM
 * Project: munch-core
 */
@Singleton
public final class PlacePartnerContentCardLoader {

    private final PartnerContentManager partnerContentManager;

    @Inject
    public PlacePartnerContentCardLoader(PartnerContentManager partnerContentManager) {
        this.partnerContentManager = partnerContentManager;
    }

    /**
     * @param place to load from
     * @return Optional Partner Content Card
     */
    public Optional<PlaceJsonCard> load(Place place) {
        PartnerContentManager.PartnerContentResult contentResult = partnerContentManager.query(place.getId());
        if (contentResult.contents.isEmpty()) return Optional.empty();

        JsonNode jsonNode = JsonUtils.toTree(contentResult.contents);
        return Optional.of(new PlaceJsonCard("extended_PartnerContent_20180405", jsonNode));
    }
}
