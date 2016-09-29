package com.munch.core.struct.rdbms.place;

import com.munch.core.struct.rdbms.abs.AbsAuditData;
import com.munch.core.struct.util.Lucene;
import com.munch.core.struct.util.map.BiDirectionHashSet;
import com.munch.core.struct.util.map.OneEntity;
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
public class Place extends AbsAuditData implements OneEntity {

    public static final int STATUS_ACTIVE = 200;
    public static final int STATUS_DELETED = 400;

    // Basic
    private String id;
    private String name;
    private String description;

    // Informational
    private String phoneNumber;
    private String websiteUrl;
    private Set<PlaceType> types = new HashSet<>();

    // Details
    private Double priceStart;
    private Double priceEnd;

    // Menu
    private Set<PlaceMenu> menus = new BiDirectionHashSet<>(this);

    // Related
    private Set<PlaceLocation> locations = new BiDirectionHashSet<>(this);

    // Data Tracking
    private int status = STATUS_ACTIVE;
    private int humanVersion = 0;
    private int machineVersion = 0;

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

    @Column(length = 255, nullable = false)
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
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

    @Column(nullable = false)
    public Double getPriceStart() {
        return priceStart;
    }

    public void setPriceStart(Double priceStart) {
        this.priceStart = priceStart;
    }

    @Column(nullable = false)
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

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true, mappedBy = "place")
    public Set<PlaceLocation> getLocations() {
        return locations;
    }

    protected void setLocations(Set<PlaceLocation> locations) {
        this.locations = locations;
    }

    @Column(nullable = false)
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Column(nullable = false)
    public int getHumanVersion() {
        return humanVersion;
    }

    protected void setHumanVersion(int humanVersion) {
        this.humanVersion = humanVersion;
    }

    @Column(nullable = false)
    public int getMachineVersion() {
        return machineVersion;
    }

    protected void setMachineVersion(int machineVersion) {
        this.machineVersion = machineVersion;
    }

    @Transient
    public void incrementHumanVersion() {
        setHumanVersion(humanVersion++);
        incrementMachineVersion();
    }

    @Transient
    public void incrementMachineVersion() {
        setMachineVersion(machineVersion++);
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
