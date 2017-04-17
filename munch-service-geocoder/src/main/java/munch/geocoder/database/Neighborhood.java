package munch.geocoder.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vividsolutions.jts.geom.Point;
import org.apache.lucene.analysis.core.KeywordTokenizerFactory;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.StopFilterFactory;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterFilterFactory;
import org.apache.lucene.analysis.ngram.EdgeNGramFilterFactory;
import org.apache.lucene.analysis.ngram.NGramFilterFactory;
import org.apache.lucene.analysis.pattern.PatternReplaceFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Parameter;

import javax.persistence.*;
import java.util.Set;

/**
 * Created By: Fuxing Loh
 * Date: 14/4/2017
 * Time: 3:08 PM
 * Project: munch-core
 */
@AnalyzerDefs({

        @AnalyzerDef(name = "autocompleteEdgeAnalyzer",

// Split input into tokens according to tokenizer
                tokenizer = @TokenizerDef(factory = KeywordTokenizerFactory.class),

                filters = {
                        // Normalize token text to lowercase, as the user is unlikely to
                        // care about casing when searching for matches
                        @TokenFilterDef(factory = PatternReplaceFilterFactory.class, params = {
                                @Parameter(name = "pattern", value = "([^a-zA-Z0-9\\.])"),
                                @Parameter(name = "replacement", value = " "),
                                @Parameter(name = "replace", value = "all")}),
                        @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                        @TokenFilterDef(factory = StopFilterFactory.class),
                        // Index partial words starting at the front, so we can provide
                        // Autocomplete functionality
                        @TokenFilterDef(factory = EdgeNGramFilterFactory.class, params = {
                                @Parameter(name = "minGramSize", value = "3"),
                                @Parameter(name = "maxGramSize", value = "50")})}),

        @AnalyzerDef(name = "autocompleteNGramAnalyzer",

// Split input into tokens according to tokenizer
                tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),

                filters = {
                        // Normalize token text to lowercase, as the user is unlikely to
                        // care about casing when searching for matches
                        @TokenFilterDef(factory = WordDelimiterFilterFactory.class),
                        @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                        @TokenFilterDef(factory = NGramFilterFactory.class, params = {
                                @Parameter(name = "minGramSize", value = "3"),
                                @Parameter(name = "maxGramSize", value = "5")}),
                        @TokenFilterDef(factory = PatternReplaceFilterFactory.class, params = {
                                @Parameter(name = "pattern", value = "([^a-zA-Z0-9\\.])"),
                                @Parameter(name = "replacement", value = " "),
                                @Parameter(name = "replace", value = "all")})
                }),

        @AnalyzerDef(name = "standardAnalyzer",

// Split input into tokens according to tokenizer
                tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),

                filters = {
                        // Normalize token text to lowercase, as the user is unlikely to
                        // care about casing when searching for matches
                        @TokenFilterDef(factory = WordDelimiterFilterFactory.class),
                        @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                        @TokenFilterDef(factory = PatternReplaceFilterFactory.class, params = {
                                @Parameter(name = "pattern", value = "([^a-zA-Z0-9\\.])"),
                                @Parameter(name = "replacement", value = " "),
                                @Parameter(name = "replace", value = "all")})
                }) // Def
})
public final class Neighborhood {

    private String name;
    private long sort;

    private Point center;
    private Set<Region> regions;

    @Id
    @DocumentId
    @Column(length = 255, updatable = false, nullable = false)
    @Fields({
            @Field(name = "edgeNGramName", index = Index.YES, store = Store.NO,
                    analyze = Analyze.YES, analyzer = @Analyzer(definition = "autocompleteEdgeAnalyzer")),
            @Field(name = "nGramName", index = Index.YES, store = Store.NO,
                    analyze = Analyze.YES, analyzer = @Analyzer(definition = "autocompleteNGramAnalyzer"))
    })
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * For reverse geo-coding, the one on top is more important
     *
     * @return order/sort of place, higher shows up first
     */
    @JsonIgnore
    @Column(updatable = false, nullable = false)
    public long getSort() {
        return sort;
    }

    public void setSort(long order) {
        this.sort = order;
    }

    /**
     * @return center of the place
     */
    @Column(updatable = false, nullable = true)
    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    /**
     * Cannot be empty
     *
     * @return multi linked region of place
     */
    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public Set<Region> getRegions() {
        return regions;
    }

    public void setRegions(Set<Region> regions) {
        this.regions = regions;
    }
}
