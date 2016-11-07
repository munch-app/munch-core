package com.munch.core.struct.rdbms.place;

import com.munch.core.struct.rdbms.locality.Container;
import com.munch.core.struct.rdbms.locality.Location;
import com.munch.core.struct.rdbms.locality.Neighborhood;
import com.munch.core.struct.rdbms.place.log.PlaceLog;
import com.munch.core.struct.util.Lucene;
import com.munch.core.struct.util.many.BiHashSet;
import com.munch.core.struct.util.many.CollectionEntity;
import com.munch.core.struct.util.many.EntityMany;
import com.munch.core.struct.util.many.EntityOne;
import org.apache.lucene.search.Query;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.dsl.QueryBuilder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created By: Fuxing Loh
 * Date: 23/8/2016
 * Time: 7:35 PM
 * Project: struct
 */
@Indexed
@Entity
public class Place implements EntityOne, EntityMany<Brand>, CollectionEntity {

    public static final int STATUS_ACTIVE = 200;
    public static final int STATUS_DELETED = 400;

    // Basic
    private String id;
    private String name;
    private String description;

    // Informational (Contact)
    private String phoneNumber;
    private String websiteUrl;
    private Set<PlaceType> types = new HashSet<>();
    private Set<PlaceHour> placeHours = new BiHashSet<>(this);

    // Details (Decision)
    private Double priceStart;
    private Double priceEnd;
    private Set<PlaceMenu> menus = new BiHashSet<>(this);
    private ReviewSummary summary;
    private PlaceLink social;

    // Location Data
    private Location location;
    private Neighborhood neighbourhood;
    private Container container;

    // Brand
    private Brand brand;

    // Data Tracking
    private int status = STATUS_ACTIVE;
    private PlaceLog placeLog;

    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "CHAR(32)")
    @Id
    public String getId() {
        return id;
    }

    protected void setId(String id) {
        this.id = id;
    }

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @Column(length = 255, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(length = 500, nullable = true)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(length = 45, nullable = true)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Column(length = 255, nullable = true)
    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    public Set<PlaceType> getTypes() {
        return types;
    }

    public void setTypes(Set<PlaceType> types) {
        this.types = types;
    }

    @Column(nullable = true)
    public Double getPriceStart() {
        return priceStart;
    }

    public void setPriceStart(Double priceStart) {
        this.priceStart = priceStart;
    }

    @Column(nullable = true)
    public Double getPriceEnd() {
        return priceEnd;
    }

    public void setPriceEnd(Double priceEnd) {
        this.priceEnd = priceEnd;
    }

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, orphanRemoval = true, mappedBy = "place")
    public Set<PlaceMenu> getMenus() {
        return menus;
    }

    protected void setMenus(Set<PlaceMenu> menus) {
        this.menus = menus;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, orphanRemoval = true, mappedBy = "place")
    public Set<PlaceHour> getPlaceHours() {
        return placeHours;
    }

    protected void setPlaceHours(Set<PlaceHour> placeHours) {
        this.placeHours = placeHours;
    }

    @OneToOne(cascade = {CascadeType.ALL}, optional = false, orphanRemoval = true, fetch = FetchType.EAGER)
    public ReviewSummary getSummary() {
        return summary;
    }

    public void setSummary(ReviewSummary summary) {
        this.summary = summary;
    }

    @OneToOne(cascade = {CascadeType.ALL}, optional = false, orphanRemoval = true, fetch = FetchType.EAGER)
    public PlaceLink getSocial() {
        return social;
    }

    public void setSocial(PlaceLink social) {
        this.social = social;
    }

    @OneToOne(cascade = {CascadeType.ALL}, optional = false, orphanRemoval = true, fetch = FetchType.EAGER)
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    public Neighborhood getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(Neighborhood neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    @Column(nullable = false)
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @OneToOne(cascade = {CascadeType.ALL}, optional = false, orphanRemoval = true, fetch = FetchType.LAZY)
    public PlaceLog getPlaceLog() {
        return placeLog;
    }

    public void setPlaceLog(PlaceLog placeLog) {
        this.placeLog = placeLog;
    }

    @Override
    public int hashCode() {
        if (getId() == null) {
            return super.hashCode();
        } else {
            return getId().hashCode();
        }
    }

    @Override
    public boolean equals(Object obj) {
        return equals(obj, getClass());
    }

    @Override
    public void applyEntityOne(Brand one) {
        setBrand(one);
    }

    public static class Search extends Lucene {

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
         * @throws InterruptedException
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
            QueryBuilder qb = buildQuery(textManager, Place.class);

            Query luceneQuery = qb.keyword()
                    .onFields("name")
                    .matching(text)
                    .createQuery();

            // wrap Lucene query in a javax.persistence.Query and execute search
            return textManager.createFullTextQuery(luceneQuery, Place.class);
        }
    }
}
