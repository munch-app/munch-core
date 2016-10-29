package com.munch.core.struct.rdbms.place;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 29/10/2016
 * Time: 11:07 PM
 * Project: struct
 */
public class PlaceTypeTest {

    public static void main(String[] args) throws IOException {
        String json = IOUtils.toString(PlaceTypeTest.class.getClassLoader().getResourceAsStream("types.json"), "utf-8");
        Gson gson = new Gson();
        TypeDataContainer dataContainer = gson.fromJson(json, TypeDataContainer.class);
        dataContainer.cuisines.forEach(typeData -> printSQL(typeData, PlaceType.CUISINE));
        dataContainer.mealTypes.forEach(typeData -> printSQL(typeData, PlaceType.MEAL));
        dataContainer.venueTypes.forEach(typeData -> printSQL(typeData, PlaceType.VENUE));

        TypeDataContainer.TypeData halal = new TypeDataContainer.TypeData();
        halal.setId(101_000);
        halal.setName("Halal");

        TypeDataContainer.TypeData veg = new TypeDataContainer.TypeData();
        veg.setId(102_000);
        veg.setName("Vegetarian");

        printSQL(halal, PlaceType.OTHER);
        printSQL(veg, PlaceType.OTHER);
    }

    public static void printSQL(TypeDataContainer.TypeData typeData, int type){
        System.out.printf("INSERT INTO munch.PlaceType (id, createdDate, updatedDate, name, sort, type)\n" +
                "VALUES (%d, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '%s', %d, %d);\n",
                typeData.getId(), typeData.getName(), typeData.getPopularity(), type);
    }

    private static class TypeDataContainer {
        List<TypeData> cuisines;
        List<TypeData> mealTypes;
        List<TypeData> venueTypes;

        static class TypeData {
            int id;
            String name;
            String altName;
            int popularity;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAltName() {
                return altName;
            }

            public void setAltName(String altName) {
                this.altName = altName;
            }

            public int getPopularity() {
                return popularity;
            }

            public void setPopularity(int popularity) {
                this.popularity = popularity;
            }
        }
    }
}