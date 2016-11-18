package com.munch.core.struct.rdbms.spaghetti;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.munch.core.struct.rdbms.abs.AbsSortData;
import com.munch.core.struct.rdbms.place.Place;
import com.munch.core.struct.util.Lucene;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.spatial.DistanceSortField;
import org.locationtech.spatial4j.shape.Point;

import javax.persistence.*;
import java.util.Date;

/**
 * Created By: Fuxing Loh
 * Date: 26/8/2016
 * Time: 10:15 PM
 * Project: struct
 */
@Indexed
@Spatial(spatialMode = SpatialMode.HASH, name = "location")
@Entity
public class SeedPlaceTrack extends AbsSortData {

    // Just entered the system
    public static final int STATUS_RAW = 100;

    // Spaghetti Manual Crawler System
    public static final int STATUS_LOCATION_OPEN = 3_010;
    public static final int STATUS_PROBE_OPEN = 3_020;
    public static final int STATUS_PROBE_ERROR = 3_024;
    public static final int STATUS_SOURCE_ENTRY_OPEN = 3_030;
    public static final int STATUS_EXTRACT_OPEN = 3_040;
    public static final int STATUS_EXTRACT_ERROR = 3_044;
    public static final int STATUS_REVIEW_OPEN = 3_050;
    public static final int STATUS_CACHE_RESOURCE_OPEN = 3_100; // Run on Dashboard side
    public static final int STATUS_FINAL_ENTRY_OPEN = 3_200;
    public static final int STATUS_FINAL_SAVE_OPEN = 3_500;

    // Spaghetti Fast Forward Crawler System
    public static final int STATUS_FASTFORWARD_OPEN = 4_000;
    public static final int STATUS_FASTFORWARD_FINAL_SAVE_OPEN = 4_200;

    public static final int STATUS_SAVE_SAVING = 5_000;

    // Spaghetti Final Outcome
    public static final int STATUS_COMPLETED = 9_200;
    public static final int STATUS_ALREADY_EXIST = 9_201;
    public static final int STATUS_DELETED = 9_400;

    // Spaghetti Crawler Conflict (Paused)
    public static final int STATUS_DELAY = 9_500;

    // Main
    private String id;
    private String name;
    private String address;
    private Double lat, lng;

    private Integer status; // Various Stages
    private Place place; // Final Place after adding
    private String changes;

    // Stage Completion Dates
    private Date stageLocationDate;
    private Date stageProbeDate;
    private Date stageSourceDate;
    private Date stageExtractDate;
    private Date stageFinalEntryDate;
    private Date stageFinalSaveDate;

    private Integer source; // source.MunchSource
    private String sourceUrl; // If Source has a URL
    private String sourceId; // If Source has a formatted Id
    private String sourceUser; // If Source has user id

    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "CHAR(32)")
    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(length = 255, nullable = false)
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(length = 255, nullable = true)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(length = 255, nullable = true)
    public String getChanges() {
        return changes;
    }

    public void setChanges(String changes) {
        this.changes = changes;
    }

    @Column(nullable = false)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(nullable = false)
    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    @Column(nullable = false)
    @Latitude(of = "location")
    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    @Column(nullable = false)
    @Longitude(of = "location")
    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JsonIgnore
    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    @Column(nullable = true, length = 255)
    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    @Column(nullable = true, length = 255)
    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    @Column(nullable = true, length = 255)
    public String getSourceUser() {
        return sourceUser;
    }

    public void setSourceUser(String sourceUser) {
        this.sourceUser = sourceUser;
    }

    @Column(nullable = true)
    public Date getStageLocationDate() {
        return stageLocationDate;
    }

    public void setStageLocationDate(Date stageLocationDate) {
        this.stageLocationDate = stageLocationDate;
    }

    @Column(nullable = true)
    public Date getStageProbeDate() {
        return stageProbeDate;
    }

    public void setStageProbeDate(Date stageProbeDate) {
        this.stageProbeDate = stageProbeDate;
    }

    @Column(nullable = true)
    public Date getStageSourceDate() {
        return stageSourceDate;
    }

    public void setStageSourceDate(Date stageSourceDate) {
        this.stageSourceDate = stageSourceDate;
    }

    @Column(nullable = true)
    public Date getStageExtractDate() {
        return stageExtractDate;
    }

    public void setStageExtractDate(Date stageExtractDate) {
        this.stageExtractDate = stageExtractDate;
    }

    @Column(nullable = true)
    public Date getStageFinalEntryDate() {
        return stageFinalEntryDate;
    }

    public void setStageFinalEntryDate(Date stageFinalEntryDate) {
        this.stageFinalEntryDate = stageFinalEntryDate;
    }

    @Column(nullable = true)
    public Date getStageFinalSaveDate() {
        return stageFinalSaveDate;
    }

    public void setStageFinalSaveDate(Date stageFinalSaveDate) {
        this.stageFinalSaveDate = stageFinalSaveDate;
    }

    public static class Search extends Lucene implements Lucene.Spatial {

        protected Search(EntityManager entityManager) {
            super(entityManager);
        }

        /**
         * Init helper
         *
         * @param entityManager entity manager
         * @return Search instance
         */
        public static Search init(EntityManager entityManager) {
            return new Search(entityManager);
        }

        /**
         * Build index and wait
         *
         * @return Search Instance
         * @throws InterruptedException, if something goes wrong
         */
        protected Search buildIndex() throws InterruptedException {
            super.buildIndexWait();
            return this;
        }

        /**
         * Search with text
         *
         * @param text query string
         * @return Persistence Query to query
         */
        public FullTextQuery query(String text) {
            FullTextEntityManager textManager = getFullTextEntityManager();
            QueryBuilder qb = buildQuery(textManager, SeedPlaceTrack.class);

            Query luceneQuery = qb.keyword()
                    .onFields("name")
                    .matching(text)
                    .createQuery();

            // wrap Lucene query in a javax.persistence.Query and execute search
            return textManager.createFullTextQuery(luceneQuery, SeedPlaceTrack.class);
        }

        @Override
        public FullTextQuery distance(double lat, double lng, double radius) {
            FullTextEntityManager textManager = getFullTextEntityManager();
            QueryBuilder qb = buildQuery(textManager, SeedPlaceTrack.class);
            return textManager.createFullTextQuery(distanceQuery("location", qb, lat, lng, radius), SeedPlaceTrack.class);
        }

        public FullTextQuery distanceSort(double lat, double lng, double radius) {
            FullTextEntityManager textManager = getFullTextEntityManager();
            QueryBuilder qb = buildQuery(textManager, SeedPlaceTrack.class);
            FullTextQuery query = textManager.createFullTextQuery(distanceQuery("location", qb, lat, lng, radius), SeedPlaceTrack.class);
            Sort sort = new Sort(new DistanceSortField(lat, lng, "location"));
            query.setSort(sort);
            return query;
        }

        public FullTextQuery distanceSort(Point point, double radius) {
            return distanceSort(point.getY(), point.getX(), radius);
        }
    }
}
