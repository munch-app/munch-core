package munch.api.search.plugin;

import munch.api.search.SearchQuery;
import munch.api.search.SearchRequest;
import munch.api.search.cards.SearchCard;
import munch.api.search.cards.SearchPlaceCard;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Search Card Loader
 */
@FunctionalInterface
public interface SearchCardPlugin {

    /**
     * @param request to read from
     * @return Card with position
     */
    @Nullable
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

    default List<Position> of(int index, List<? extends SearchCard> cards) {
        if (cards.isEmpty()) return List.of();

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
     * SearchCardPlugin Request
     * In order not to confuse the methods in SearchRequest with
     * SearchCardPlugin.Request don't forward the methods over.
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
         * @return SearchRequest
         */
        public SearchRequest getRequest() {
            return request;
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
            if (request.isNearby()) return request.getLatLng();
            if (request.isAnywhere()) return request.getLatLng();
            if (request.isWhere()) {
                if (request.getAreas().size() == 1) return request.getAreas().get(0).getLocation().getLatLng();
            }

            if (request.isBetween()) return null;

            // Fallback
            return request.getLatLng();
        }

        public List<SearchCard> getCards() {
            return cards;
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
            return request.getPage() == 0;
        }

        public SearchQuery getQuery() {
            return query;
        }

        public boolean isComplex() {
            return complex;
        }

        /**
         * @return natural search without any filter input
         */
        public boolean isNatural() {
            if (isComplex()) return false;
            if (request.isNearby()) return true;
            if (request.isAnywhere()) return true;
            return false;
        }

        /**
         * is Complex if:
         * - has sort
         * - has filter hour
         * - has filter price
         * - has filter tags
         *
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

    /**
     * Created by: Fuxing
     * Date: 11/12/2017
     * Time: 4:43 AM
     * Project: munch-core
     */
    @Singleton
    final class Runner {
        private static final Comparator<Position> COMPARATOR;

        static {
            Comparator<Position> comparator = Comparator.comparingInt(o -> o.index);
            comparator = comparator.thenComparing(o -> !(o.card instanceof SearchPlaceCard));
            comparator = comparator.thenComparing(o -> o.card.getCardId());
            COMPARATOR = comparator;
        }

        private final Set<SearchCardPlugin> plugins;

        @Inject
        public Runner(Set<SearchCardPlugin> plugins) {
            this.plugins = plugins;
        }

        /**
         * @param cards         to inject into
         * @param searchRequest SearchRequest
         */
        public List<SearchCard> run(List<SearchCard> cards, SearchRequest searchRequest) {
            Request request = new Request(cards, searchRequest);

            List<Position> positions = new ArrayList<>();

            for (int i = 0; i < cards.size(); i++) {
                positions.add(new Position(cards.get(i), i));
            }

            for (SearchCardPlugin loader : plugins) {
                List<Position> loaded = loader.load(request);
                if (loaded != null) positions.addAll(loaded);
            }

            return positions.stream()
                    .sorted(COMPARATOR)
                    .map(position -> position.card)
                    .collect(Collectors.toList());
        }
    }
}
