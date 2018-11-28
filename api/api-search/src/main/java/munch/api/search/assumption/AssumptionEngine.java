package munch.api.search.assumption;

import com.google.common.base.Joiner;
import edit.utils.PatternSplit;
import munch.api.search.SearchQuery;
import munch.api.search.SearchRequest;
import munch.api.search.assumption.data.AssumptionQuery;
import munch.api.search.assumption.data.AssumptionToken;
import munch.api.search.assumption.data.TagAssumptionToken;
import munch.api.search.assumption.data.TextAssumptionToken;
import munch.data.location.Area;
import munch.data.location.Location;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;

/**
 * Created by: Fuxing
 * Date: 23/2/18
 * Time: 8:50 PM
 * Project: munch-data
 */
@Singleton
public class AssumptionEngine {
    public static final Set<String> STOP_WORDS = Set.of("around", "near", "in", "at", "and", "or",
            "food", "cuisine", "cuisines", "places", "place", "location");
    public static final PatternSplit TOKENIZE_PATTERN = PatternSplit.compile(" {1,}|,|\\.");
    public static final Area SINGAPORE;

    static {
        SINGAPORE = new Area();
        SINGAPORE.setType(Area.Type.City);
        SINGAPORE.setAreaId("singapore");
        SINGAPORE.setName("Singapore");

        Location location = new Location();
        location.setCountry("SGP");
        location.setCity("singapore");
        location.setLatLng("1.290270, 103.851959");

        Location.Polygon polygon = new Location.Polygon();
        polygon.setPoints(List.of("1.26675774823,103.603134155", "1.32442122318,103.617553711", "1.38963424766,103.653259277", "1.41434608581,103.666305542", "1.42944763543,103.671798706", "1.43905766081,103.682785034", "1.44386265833,103.695831299", "1.45896401284,103.720550537", "1.45827758983,103.737716675", "1.44935407163,103.754196167", "1.45004049736,103.760375977", "1.47887018872,103.803634644", "1.4754381021,103.826980591", "1.45827758983,103.86680603", "1.43219336108,103.892211914", "1.4287612035,103.897018433", "1.42670190649,103.915557861", "1.43219336108,103.934783936", "1.42189687297,103.960189819", "1.42464260763,103.985595703", "1.42121043879,104.000701904", "1.43974408965,104.02130127", "1.44592193988,104.043960571", "1.42464260763,104.087219238", "1.39718511473,104.094772339", "1.35737118164,104.081039429", "1.29009788407,104.127044678", "1.277741368,104.127044678", "1.25371463932,103.982162476", "1.17545464492,103.812561035", "1.13014521522,103.736343384", "1.19055762617,103.653945923", "1.1960495989,103.565368652", "1.26675774823,103.603134155"));
        location.setPolygon(polygon);

        SINGAPORE.setLocation(location);
    }

    private final AssumeManager manager;

    @Inject
    public AssumptionEngine(AssumeManager manager) {
        this.manager = manager;
    }

    public List<AssumptionQuery> assume(SearchRequest request, String text) {
        Map<String, Assumption> assumptionMap = manager.get();
        text = text.trim();
        List<Object> tokenList = tokenize(assumptionMap, text);

        // Validate
        if (tokenList.isEmpty()) return List.of();
        if (tokenList.size() == 1) {
            // Only one token, check if token is explicit
            Object token = tokenList.get(0);
            if (token instanceof String) return List.of();
        }
        for (Object token : tokenList) {
            if (token instanceof String) {
                // Stop-words checking
                String textToken = (String) token;
                String[] parts = textToken.split(" +");
                for (String part : parts) {
                    // If any part is not stop word, return empty
                    if (!STOP_WORDS.contains(part)) return List.of();
                }
            }
        }

        return createList(request, text, tokenList);
    }

    private List<AssumptionQuery> createList(SearchRequest request, String text, List<Object> tokenList) {
        List<AssumptionToken> assumedTokens = asToken(request.getSearchQuery(), tokenList);
        List<Assumption> assumptions = asAssumptions(tokenList);
        request = createSearchRequest(request, assumptions);

        String location = getLocation(tokenList);
        if (location != null) {
            return List.of(createLocation(location, text, assumedTokens, request));
        }

        List<AssumptionQuery> list = new ArrayList<>();
        createCurrent(text, assumedTokens, request).ifPresent(list::add);
        createNearby(text, assumedTokens, request).ifPresent(list::add);
        list.add(createAnywhere(text, assumedTokens, request));
        return list;
    }

    /**
     * Location without any other token will be prefixed with 'Places in' + Location
     *
     * @param location      name
     * @param text          searched
     * @param assumedTokens collected tokens
     * @param request       search request
     * @return AssumptionQuery for location
     */
    private AssumptionQuery createLocation(String location, String text, List<AssumptionToken> assumedTokens, SearchRequest request) {
        if (assumedTokens.size() == 1) {
            List<AssumptionToken> tokens = List.of(new TextAssumptionToken("Places in"), assumedTokens.get(0));
            return create(location, text, tokens, request.getSearchQuery());
        }
        return create(location, text, assumedTokens, request.getSearchQuery());
    }

    private AssumptionQuery createAnywhere(String text, List<AssumptionToken> assumedTokens, SearchRequest request) {
        request = request.deepCopy();

        SearchQuery query = request.getSearchQuery();
        query.getFilter().getLocation().setAreas(List.of(SINGAPORE));

        assumedTokens = new ArrayList<>(assumedTokens);
        assumedTokens.add(new TagAssumptionToken("Anywhere"));
        return create("Anywhere", text, assumedTokens, request.getSearchQuery());
    }

    private Optional<AssumptionQuery> createNearby(String text, List<AssumptionToken> assumedTokens, SearchRequest request) {
        request = request.deepCopy();
        if (!request.hasUserLatLng()) return Optional.empty();

        SearchQuery query = request.getSearchQuery();
        query.getFilter().getLocation().setAreas(null);

        assumedTokens = new ArrayList<>(assumedTokens);
        assumedTokens.add(new TagAssumptionToken("Nearby"));
        return Optional.of(create("Nearby", text, assumedTokens, query));
    }

    private Optional<AssumptionQuery> createCurrent(String text, List<AssumptionToken> assumedTokens, SearchRequest request) {
        request = request.deepCopy();
        if (!request.isWhere()) return Optional.empty();

        String locationName = request.getLocationName(null);
        if (locationName == null) return Optional.empty();

        assumedTokens = new ArrayList<>(assumedTokens);
        assumedTokens.add(new TextAssumptionToken("in"));
        assumedTokens.add(new TagAssumptionToken(locationName));
        return Optional.of(create(locationName, text, assumedTokens, request.getSearchQuery()));
    }

    protected AssumptionQuery create(String location, String text, List<AssumptionToken> assumedTokens, SearchQuery query) {
        AssumptionQuery assumptionQuery = new AssumptionQuery();
        assumptionQuery.setText(text);
        assumptionQuery.setLocation(location);

        assumptionQuery.setTokens(assumedTokens);
        assumptionQuery.setSearchQuery(query);
        return assumptionQuery;
    }

    private static SearchRequest createSearchRequest(SearchRequest request, List<Assumption> assumptions) {
        SearchRequest copied = request.deepCopy();
        for (Assumption assumption : assumptions) {
            assumption.apply(copied);
        }
        return copied;
    }

    /**
     * @param tokenList list of parsed tokens
     * @return Location
     */
    @Nullable
    private static String getLocation(List<Object> tokenList) {
        for (Object o : tokenList) {
            if (o instanceof Assumption) {
                Assumption assumption = (Assumption) o;
                if (Assumption.Type.Location.equals(assumption.getType())) {
                    return assumption.getTag();
                }
            }
        }
        return null;
    }

    private static List<AssumptionToken> asToken(SearchQuery query, List<Object> tokenList) {
        List<AssumptionToken> assumedTokens = new ArrayList<>(getTokens(query));
        for (Object o : tokenList) {
            if (o instanceof String) {
                assumedTokens.add(new TextAssumptionToken((String) o));
            } else {
                TagAssumptionToken token = new TagAssumptionToken(((Assumption) o).getTag());
                if (!assumedTokens.contains(token)) {
                    assumedTokens.add(token);
                }
            }
        }
        return assumedTokens;
    }

    private static List<AssumptionToken> getTokens(SearchQuery query) {
        SearchQuery.Filter filter = query.getFilter();
        if (filter == null) return List.of();

        List<AssumptionToken> assumedTokens = new ArrayList<>();
        SearchQuery.Filter.Hour hour = query.getFilter().getHour();
        if (hour != null) {
            assumedTokens.add(new TagAssumptionToken(hour.asText()));
        }
        query.getFilter().getTags().forEach(tag -> {
            assumedTokens.add(new TagAssumptionToken(tag.getName()));
        });

        return assumedTokens;
    }

    private static List<Assumption> asAssumptions(List<Object> tokenList) {
        List<Assumption> tokens = new ArrayList<>();
        for (Object o : tokenList) {
            if (o instanceof Assumption) {
                tokens.add((Assumption) o);
            }
        }
        return tokens;
    }

    private List<Object> tokenize(Map<String, Assumption> assumptionMap, String text) {
        if (StringUtils.isBlank(text)) return List.of();
        Assumption assumption = assumptionMap.get(text.toLowerCase());
        if (assumption != null) return List.of(assumption);


        List<String> parts = TOKENIZE_PATTERN.splitRemoved(text);
        for (int i = 2; i < parts.size() + 1; i++) {
            for (Triple<String, String, String> combo : reverseSplitInto(parts, i)) {
                assumption = assumptionMap.get(combo.getMiddle().toLowerCase());
                if (assumption != null) {
                    // Found Assumption
                    List<Object> tokenList = new ArrayList<>();
                    tokenList.add(assumption);
                    if (combo.getLeft() != null) {
                        tokenList.addAll(0, tokenize(assumptionMap, combo.getLeft()));
                    }
                    if (combo.getRight() != null) {
                        tokenList.addAll(tokenize(assumptionMap, combo.getRight()));
                    }
                    return tokenList;
                }
            }
        }

        // Not Found
        return List.of(text);
    }

    public static List<Triple<String, String, String>> splitInto(List<String> parts, int join) {
        List<Triple<String, String, String>> joined = new ArrayList<>();
        for (int i = 0; i < parts.size() - join; i++) {
            String left = Joiner.on(" ").join(parts.subList(0, i));
            String middle = Joiner.on(" ").join(parts.subList(i, i + join + 1));
            String right = Joiner.on(" ").join(parts.subList(i + join + 1, parts.size()));
            joined.add(Triple.of(left, middle, right));
        }
        return joined;
    }

    public static List<Triple<String, String, String>> reverseSplitInto(List<String> parts, int combination) {
        return splitInto(parts, combination > parts.size() ? 0 : parts.size() - combination);
    }
}
