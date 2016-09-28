package com.munch.core.struct;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.munch.core.essential.json.BlockMapper;
import com.munch.core.essential.json.DefaultBlockMapper;
import com.munch.core.essential.source.DataSource;
import com.munch.core.struct.block.source.SeedPlace;
import com.munch.core.struct.block.source.SourceHour;
import com.munch.core.struct.rdbms.place.PlaceHour;
import com.munch.core.struct.rdbms.source.SeedPlaceTrack;
import com.munch.core.struct.util.HibernateUtil;

import javax.persistence.EntityManager;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created By: Fuxing Loh
 * Date: 13/9/2016
 * Time: 5:40 PM
 * Project: struct
 */
public class AddSpaghettiTestData {

    private EntityManager entityManager;
    private BlockMapper blockMapper = new DefaultBlockMapper(SeedPlace.BUCKET_NAME);

    public void open() {
        entityManager = HibernateUtil.createEntityManager();
        entityManager.getTransaction().begin();
    }

    public void close() {
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    private List<Data> readFile() throws FileNotFoundException {
        Type type = new TypeToken<List<Data>>() {
        }.getType();
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader("c:\\Users\\Fuxin\\Desktop\\ffv.json"));
        return gson.fromJson(reader, type);
    }

    public String[][] putHoursToList(List<SourceHour> list, String[][] strings, int day) {
        for (String[] string : strings) {
            SourceHour hour = new SourceHour();
            hour.setDay(day);
            hour.setOpen(string[0]);
            hour.setClose(string[1]);
            list.add(hour);
        }
        return strings;
    }

    private List<SourceHour> toHours(Map<String, String[][]> data) {
        List<SourceHour> list = new ArrayList<>();

        data.computeIfPresent("monday", (s, strings) -> putHoursToList(list, strings, PlaceHour.MON));
        data.computeIfPresent("tuesday", (s, strings) -> putHoursToList(list, strings, PlaceHour.TUE));
        data.computeIfPresent("wednesday", (s, strings) -> putHoursToList(list, strings, PlaceHour.WED));
        data.computeIfPresent("thursday", (s, strings) -> putHoursToList(list, strings, PlaceHour.THU));
        data.computeIfPresent("friday", (s, strings) -> putHoursToList(list, strings, PlaceHour.FRI));
        data.computeIfPresent("saturday", (s, strings) -> putHoursToList(list, strings, PlaceHour.SAT));
        data.computeIfPresent("sunday", (s, strings) -> putHoursToList(list, strings, PlaceHour.SUN));

        return list;
    }

    public void persist(Data data) {
        SeedPlaceTrack track = new SeedPlaceTrack();
        track.setName(data.name);

        track.setLat(data.lat);
        track.setLng(data.lng);

        track.setStatus(SeedPlaceTrack.STATUS_RAW);
        track.setSource(DataSource.FACT);
        track.setOrigin("fx");

        entityManager.persist(track);
        entityManager.flush();

        SeedPlace seedPlace = new SeedPlace();

        seedPlace.setId(track.getId());
        seedPlace.setName(data.name);

        seedPlace.setPhoneNumber(data.telephone);
        seedPlace.setWebsiteUrl(data.website);

        List<String> types = new ArrayList<>();
        if (data.categoryLabels != null) {
            for (String[] categoryLabel : data.categoryLabels) {
                Collections.addAll(types, categoryLabel);
            }
        }
        seedPlace.setTypes(types);

        if (data.hours != null) {
            seedPlace.setHours(toHours(data.hours));
        }

        seedPlace.setLat(data.lat);
        seedPlace.setLng(data.lng);
        seedPlace.setAddress(data.address);
        seedPlace.setAddressExt(data.addressExtended);
        seedPlace.setZipCode(data.postalCode);

        seedPlace.setSource(DataSource.FACT);
        seedPlace.setSourceUrl(data.factualId);
        seedPlace.setOrigin("fx");
        blockMapper.save(seedPlace.getId(), seedPlace);
    }

    public static void main(String[] args) throws FileNotFoundException {
        AddSpaghettiTestData testData = new AddSpaghettiTestData();
        List<Data> list = testData.readFile();
        testData.open();
        for (int i = 0; i < 1000; i++) {
            Data data = list.get(i);
            testData.persist(data);
            System.out.println(i + " : Completed: " + data.name);
        }
        testData.close();
    }

    public class Data {
        public String factualId;
        public String name;
        public String address;

        // Normally it means unit number.
        public String addressExtended;
        public String postalCode;
        public String telephone;
        public String website;

        public double lat;
        public double lng;

        // Format: JSON
        public String[][] categoryLabels;
        public Map<String, String[][]> hours;

        public int state = 0;
    }

}
