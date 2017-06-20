package munch.location;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vividsolutions.jts.geom.Polygon;
import munch.location.database.PointsUserType;
import org.apache.lucene.analysis.core.KeywordTokenizerFactory;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.StopFilterFactory;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterFilterFactory;
import org.apache.lucene.analysis.ngram.EdgeNGramFilterFactory;
import org.apache.lucene.analysis.ngram.NGramFilterFactory;
import org.apache.lucene.analysis.pattern.PatternReplaceFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.search.annotations.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by: Fuxing
 * Date: 15/6/2017
 * Time: 6:36 AM
 * Project: munch-core
 */
@Entity
@Indexed
@AnalyzerDefs({
        @AnalyzerDef(name = "autocompleteEdgeAnalyzer",
                tokenizer = @TokenizerDef(factory = KeywordTokenizerFactory.class),

                filters = {
                        // Normalize token text to lowercase, as the user is unlikely to
                        // care about casing when searching for matches
                        @TokenFilterDef(factory = PatternReplaceFilterFactory.class, params = {
                                @org.hibernate.search.annotations.Parameter(name = "pattern", value = "([^a-zA-Z0-9\\.])"),
                                @org.hibernate.search.annotations.Parameter(name = "replacement", value = " "),
                                @org.hibernate.search.annotations.Parameter(name = "replace", value = "all")}),
                        @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                        @TokenFilterDef(factory = StopFilterFactory.class),
                        // Index partial words starting at the front, so we can provide
                        // Autocomplete functionality
                        @TokenFilterDef(factory = EdgeNGramFilterFactory.class, params = {
                                @org.hibernate.search.annotations.Parameter(name = "minGramSize", value = "3"),
                                @org.hibernate.search.annotations.Parameter(name = "maxGramSize", value = "50")})}),

        @AnalyzerDef(name = "autocompleteNGramAnalyzer",
                tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),

                filters = {
                        // Normalize token text to lowercase, as the user is unlikely to
                        // care about casing when searching for matches
                        @TokenFilterDef(factory = WordDelimiterFilterFactory.class),
                        @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                        @TokenFilterDef(factory = NGramFilterFactory.class, params = {
                                @org.hibernate.search.annotations.Parameter(name = "minGramSize", value = "3"),
                                @org.hibernate.search.annotations.Parameter(name = "maxGramSize", value = "5")}),
                        @TokenFilterDef(factory = PatternReplaceFilterFactory.class, params = {
                                @org.hibernate.search.annotations.Parameter(name = "pattern", value = "([^a-zA-Z0-9\\.])"),
                                @org.hibernate.search.annotations.Parameter(name = "replacement", value = " "),
                                @org.hibernate.search.annotations.Parameter(name = "replace", value = "all")})
                }),

        @AnalyzerDef(name = "standardAnalyzer",
                tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),

                filters = {
                        // Normalize token text to lowercase, as the user is unlikely to
                        // care about casing when searching for matches
                        @TokenFilterDef(factory = WordDelimiterFilterFactory.class),
                        @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                        @TokenFilterDef(factory = PatternReplaceFilterFactory.class, params = {
                                @org.hibernate.search.annotations.Parameter(name = "pattern", value = "([^a-zA-Z0-9\\.])"),
                                @org.hibernate.search.annotations.Parameter(name = "replacement", value = " "),
                                @org.hibernate.search.annotations.Parameter(name = "replace", value = "all")})
                })
})
@TypeDefs(value = {
        @TypeDef(name = "points", typeClass = PointsUserType.class)
})
public class Location {
    private String name;
    private String center;
    private String[] points;

    private long sort;
    private Polygon geoPolygon;

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

    @JsonIgnore
    @Column(updatable = false, nullable = false)
    public long getSort() {
        return sort;
    }

    public void setSort(long sort) {
        this.sort = sort;
    }

    @Column(updatable = false, nullable = true)
    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    @JsonIgnore
    @Column(updatable = false, nullable = false)
    public Polygon getGeoPolygon() {
        return geoPolygon;
    }

    public void setGeoPolygon(Polygon polygon) {
        this.geoPolygon = polygon;
    }

    @Type(type = "points")
    @Column(updatable = false, nullable = false)
    public String[] getPoints() {
        return points;
    }

    public void setPoints(String[] points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "LocationV2{" +
                "name='" + name + '\'' +
                ", sort=" + sort +
                ", center=" + center +
                ", polygon=" + geoPolygon +
                '}';
    }
}
