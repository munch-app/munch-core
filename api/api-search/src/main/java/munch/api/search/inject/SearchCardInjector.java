package munch.api.search.inject;

import munch.api.search.SearchRequest;
import munch.api.search.cards.SearchCard;
import munch.api.search.SearchQuery;
import munch.data.location.Area;
import munch.restful.core.JsonUtils;
import org.apache.commons.lang3.StringUtils;

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
     * @param cards         to inject into
     * @param searchRequest SearchRequest
     */
    public void inject(List<SearchCard> cards, SearchRequest searchRequest) {
        Loader.Request request = new Loader.Request(cards, searchRequest);

        // Negative = Placed above content
        List<Loader.Position> negatives = new ArrayList<>();

        // Positive = Placed in content
        List<Loader.Position> positives = new ArrayList<>();

        // Collect into Negatives & Positives
        for (Loader loader : loaders) {
            List<Loader.Position> positions = loader.load(request);
            if (positions == null) continue;

            for (Loader.Position position : positions) {
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
            private static final int PAGE_SIZE = 30;

            private final List<SearchCard> cards;
            private final SearchQuery query;
            private final SearchRequest request;

            private final boolean complex;

            /**
             * @param cards   content card
             * @param request raw search request
             */
            public Request(List<SearchCard> cards, SearchRequest request) {
                this.cards = new ArrayList<>(cards);
                this.query = request.getSearchQuery();

                this.request = request;
                this.complex = isComplexQuery(query);
            }

            /**
             * @return raw search request
             */
            public SearchRequest getRequest() {
                return request;
            }

            /**
             * @param defaultName to default to if none is found
             * @return name of Location where search is applied to
             */
            public String getLocationName(String defaultName) {
                return request.getLocationName(defaultName);
            }

            /**
             * @return latLng of where the search is actually applied
             */
            public String getLatLngContext() {
                // Condition checks
                if (query.getSort() != null) {
                    String sortType = query.getSort().getType();
                    if (StringUtils.isNotBlank(sortType) && !SearchQuery.Sort.TYPE_MUNCH_RANK.equals(sortType)) {
                        return null;
                    }
                }

                // Find relevant latLng
                if (isNearby()) return request.getLatLng();
                if (isAnywhere()) return request.getLatLng();
                if (isWhere()) {
                    if (getAreas().size() == 1) return getAreas().get(0).getLocation().getLatLng();
                }

                if (isBetween()) return null;

                // Fallback
                return request.getLatLng();
            }

            public List<SearchCard> getCards() {
                return cards;
            }

            /**
             * Clear all cards results, used when a Loader want to inject or swallow all the cards into its own container
             */
            public void clearCards() {
                cards.clear();
            }

            public int getCardsCount() {
                return cards.size();
            }

            public boolean isCardsMoreThan(int count) {
                return getCardsCount() > count;
            }

            public boolean isFullPage() {
                return getCardsCount() == Request.PAGE_SIZE;
            }

            @SuppressWarnings("BooleanMethodIsAlwaysInverted")
            public boolean isFirstPage() {
                return getPage() == 0;
            }

            /**
             * @return page number user is on
             */
            public int getPage() {
                return request.getPage();
            }

            public SearchQuery getQuery() {
                return query;
            }

            public String getUserId() {
                return request.getUserId();
            }

            public boolean isComplex() {
                return complex;
            }

            public boolean isAnywhere() {
                return request.isAnywhere();
            }

            public boolean isNearby() {
                return request.isNearby();
            }

            public boolean isWhere() {
                return request.isWhere();
            }

            public boolean isBetween() {
                return request.isBetween();
            }

            public boolean hasArea() {
                return request.hasArea();
            }

            public List<Area> getAreas() {
                return request.getAreas();
            }

            /**
             * @return whether user turn on location service
             */
            public boolean hasUserLocation() {
                return request.hasUserLatLng();
            }

            public SearchQuery cloneQuery() {
                return JsonUtils.deepCopy(query, SearchQuery.class);
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

            /**
             * @param query to check
             * @return whether query is complex meaning; user has custom selection
             */
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
                    if (!query.getFilter().getTags().isEmpty()) {
                        return true;
                    }
                }

                return false;
            }
        }
    }
}