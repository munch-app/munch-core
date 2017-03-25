package munch.struct;

/**
 * Created by: Fuxing
 * Date: 26/3/2017
 * Time: 6:27 AM
 * Project: munch-core
 */
public final class Price {

    private Double lowest;
    private Double highest;

    /**
     * Part of price range
     *
     * @return lowest in price range
     */
    public Double getLowest() {
        return lowest;
    }

    public void setLowest(Double lowest) {
        this.lowest = lowest;
    }

    /**
     * Part of price range
     *
     * @return highest in price range
     */
    public Double getHighest() {
        return highest;
    }

    public void setHighest(Double highest) {
        this.highest = highest;
    }
}
