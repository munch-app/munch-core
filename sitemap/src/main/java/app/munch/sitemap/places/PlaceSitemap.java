package app.munch.sitemap.places;

import com.google.common.collect.Iterators;
import com.redfin.sitemapgenerator.ChangeFreq;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import munch.data.client.PlaceClient;
import munch.data.place.Place;
import app.munch.sitemap.SitemapProvider;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Date;
import java.util.Iterator;
import java.util.Objects;

/**
 * Created by: Fuxing
 * Date: 3/11/18
 * Time: 12:27 PM
 * Project: munch-core
 */
@Singleton
public final class PlaceSitemap implements SitemapProvider {

    protected final PlaceClient placeClient;

    @Inject
    public PlaceSitemap(PlaceClient placeClient) {
        this.placeClient = placeClient;
    }

    @Override
    public String name() {
        return "places";
    }

    @Override
    public Iterator<WebSitemapUrl> provide() {
        Iterator<Place> filtered = Iterators.filter(placeClient.iterator(), place -> {
            if (place == null) return false;
            if (place.getTaste() == null) return false;
            return place.getTaste().getGroup() >= 2;
        });

        return Iterators.transform(filtered, place -> {
            Objects.requireNonNull(place);
            Place.Taste taste = place.getTaste();
            Date updated = new Date(place.getUpdatedMillis());
            return build("/places/" + place.getPlaceId(), updated, taste.getImportance(), ChangeFreq.WEEKLY);
        });
    }
}
