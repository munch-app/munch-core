package munch.api.services;

import com.google.inject.Singleton;
import com.munch.struct.Place;
import com.munch.struct.utils.DocumentDatabase;

import javax.inject.Inject;

/**
 * Created By: Fuxing Loh
 * Date: 20/3/2017
 * Time: 11:32 PM
 * Project: munch-core
 */
@Singleton
public class DataService {

    private final DocumentDatabase database;

    @Inject
    public DataService(DocumentDatabase database) {
        this.database = database;
    }

    /**
     * @param key place key
     * @return Place with key
     */
    public Place get(String key) {
        try {
            return database.get(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
