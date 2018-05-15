package munch.api.services.search.inject;

import munch.api.services.search.cards.SearchCard;
import munch.data.clients.LocationUtils;
import munch.data.structure.SearchQuery;
import munch.restful.core.JsonUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 11/12/2017
 * Time: 4:43 AM
 * Project: munch-core
 */
@Singleton
public final class SearchCardInjector {

    private final Set<Loader> loaders;

    @Inject
    public SearchCardInjector(Set<Loader> loaders) {
        this.loaders = loaders;
    }

    /**
     * @param cards  to inject into
     * @param query  to read from
     * @param userId of user to read from
     */
    public void inject(List<SearchCard> cards, SearchQuery query, @Nullable String userId) {
        Loader.Request request = new Loader.Request(cards, query, userId);

        List<Loader.Position> negatives = new ArrayList<>();
        List<Loader.Position> positives = new ArrayList<>();

        // Collect into Negatives & Positives
        for (Loader loader : loaders) {
            for (Loader.Position position : loader.load(request)) {
                if (position.index < 0) negatives.add(position);
                else positives.add(position);
            }
        }

        // Put from last first so that the index number won't be corrupted
        positives.sort((o1, o2) -> Integer.compare(o2.index, o1.index));
        for (Loader.Position positive : positives) {
            int index = positive.index;
            if (index < cards.size()) {
                cards.add(index, positive.card);
            } else {
                cards.add(positive.card);
            }
        }

        // Put from last first so that it will be in order
        negatives.sort((o1, o2) -> Integer.compare(o2.index, o1.index));
        for (Loader.Position negative : negatives) {
            cards.add(0, negative.card);
        }
    }

    /**
     * Search Card Loader
     */
    @FunctionalInterface
    public interface Loader {

        /**
         * @param request to read from
         * @return Card with position
         */
        List<Position> load(Request request);

        /**
         * @param index index to apply to each position
         * @param cards to add to with index
         * @return Card with position
         */
        default List<Position> of(int index, SearchCard... cards) {
            if (cards.length == 0) return List.of();

            List<Position> positions = new ArrayList<>();
            for (SearchCard card : cards) {
                positions.add(new Position(card, index++));
            }
            return positions;
        }

        /**
         * Position of where to inject card into
         */
        class Position {
            private final SearchCard card;
            private final int index;

            /**
             * @param card  to put into position
             * @param index location to position the card
             */
            private Position(SearchCard card, int index) {
                this.card = card;
                this.index = index;
            }
        }

        /**
         * Search query request
         */
        class Request {
            private final List<SearchCard> cards;
            private final SearchQuery query;
            private final String userId;

            private final boolean complex;

            public Request(List<SearchCard> cards, SearchQuery query, String userId) {
                this.cards = new ArrayList<>(cards);
                this.query = query;
                this.userId = userId;

                this.complex = isComplexQuery(query);
            }

            public String getLocationName(String defaultName) {
                return LocationUtils.getName(query, defaultName);
            }

            /**
             * @return latLng of where the search is applied
             */
            public String getLatLngContext() {
                // Condition checks
                if (query.getSort() != null) {
                    String sortType = query.getSort().getType();
                    if (StringUtils.isNotBlank(sortType) && !SearchQuery.Sort.TYPE_MUNCH_RANK.equals(sortType)) {
                        return null;
                    }
                }

                if (query.getFilter() != null) {
                    SearchQuery.Filter filter = query.getFilter();
                    if (filter.getContainers() != null) {
                        if (!filter.getContainers().isEmpty()) return null;
                    }

                    if (filter.getLocation() != null) {
                        if ("singapore".equals(filter.getLocation().getId())) return null;
                        return filter.getLocation().getLatLng();
                    }
                }

                return query.getLatLng();
            }

            public List<SearchCard> getCards() {
                return cards;
            }

            public int getCardsCount() {
                return cards.size();
            }

            public int getFrom() {
                return query.getFrom();
            }

            public boolean isCardsMore(int count) {
                return getCardsCount() > count;
            }

            public SearchQuery getQuery() {
                return query;
            }

            public SearchQuery cloneQuery() {
                return JsonUtils.deepCopy(query, SearchQuery.class);
            }

            public String getUserId() {
                return userId;
            }

            public boolean isComplex() {
                return complex;
            }

            public boolean isAnywhere() {
                return LocationUtils.isAnywhere(query);
            }

            public boolean isNearby() {
                return LocationUtils.isNearby(query);
            }

            public boolean hasContainer() {
                if (query.getFilter() == null) return false;
                if (query.getFilter().getContainers() == null) return false;
                return !query.getFilter().getContainers().isEmpty();
            }

            /**
             * @return natural search without any filter input
             */
            public boolean isNatural() {
                if (isComplex()) return false;
                if (isNearby()) return true;
                if (isAnywhere()) return true;
                return false;
            }

            private static boolean isComplexQuery(SearchQuery query) {
                if (query.getSort() != null) {
                    if (query.getSort().getType() != null)
                        if (!query.getSort().getType().equals(SearchQuery.Sort.TYPE_MUNCH_RANK)) return true;
                }
                if (query.getFilter() != null) {
                    if (query.getFilter().getHour() != null) {
                        if (query.getFilter().getHour().getDay() != null) return true;
                    }
                    if (query.getFilter().getPrice() != null) {
                        if (query.getFilter().getPrice().getMin() != null) return true;
                        if (query.getFilter().getPrice().getMax() != null) return true;
                    }
                    if (query.getFilter().getTag() != null) {
                        if (query.getFilter().getTag().getPositives() != null) {
                            if (!query.getFilter().getTag().getPositives().isEmpty()) return true;
                        }
                        if (query.getFilter().getTag().getNegatives() != null) {
                            if (!query.getFilter().getTag().getNegatives().isEmpty()) return true;
                        }
                    }
                }

                return false;
            }
        }
    }
}