package munch.catalyst.service;

import com.typesafe.config.Config;
import munch.restful.client.RestfulClient;
import munch.struct.place.Place;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Date;
import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 17/4/2017
 * Time: 9:44 PM
 * Project: munch-core
 */
@Singleton
public class DataClient extends RestfulClient {

    /**
     * Look at data service package to api service settings
     *
     * @param config config to load data.url
     */
    @Inject
    public DataClient(@Named("services") Config config) {
        super(config.getString("data.url"));
    }

    @Nullable
    public PostgresPlace latest() {
        return doGet("/places/batch/latest")
                .hasMetaCodes(200, 404)
                .asDataObject(PostgresPlace.class);
    }

    public void put(List<Place> places) {
        if (places.isEmpty()) return;
        doPut("/places/batch/put")
                .body(places)
                .hasMetaCodes(200);
    }

    public void delete(List<String> keys) {
        if (keys.isEmpty()) return;
        doDelete("/places/batch/delete")
                .body(keys)
                .hasMetaCodes(200);
    }

    public static class PostgresPlace {
        private String id;
        private Place place;

        private Date createdDate;
        private Date updatedDate;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Place getPlace() {
            return place;
        }

        public void setPlace(Place place) {
            this.place = place;
        }

        public Date getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(Date createdDate) {
            this.createdDate = createdDate;
        }

        public Date getUpdatedDate() {
            return updatedDate;
        }

        public void setUpdatedDate(Date updatedDate) {
            this.updatedDate = updatedDate;
        }
    }
}
