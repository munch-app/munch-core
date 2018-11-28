package munch.api.search.filter;

/**
 * Created by: Fuxing
 * Date: 15/9/18
 * Time: 10:53 PM
 * Project: munch-core
 */
public final class FilterResult {
    private long count;

    private TagGraph tagGraph;
    private PriceGraph priceGraph;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public TagGraph getTagGraph() {
        return tagGraph;
    }

    public void setTagGraph(TagGraph tagGraph) {
        this.tagGraph = tagGraph;
    }

    public PriceGraph getPriceGraph() {
        return priceGraph;
    }

    public void setPriceGraph(PriceGraph priceGraph) {
        this.priceGraph = priceGraph;
    }
}
