package com.munch.core.struct.nosql.place;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.google.common.base.Splitter;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created By: Fuxing Loh
 * Date: 26/9/2016
 * Time: 7:50 PM
 * Project: struct
 */
@DynamoDBTable(tableName = "PlaceTag")
public class PlaceTag {

    private String placeId;
    private String countName;

    private int count;
    private String name;

    @DynamoDBHashKey(attributeName = "k")
    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    @Deprecated
    @DynamoDBRangeKey(attributeName = "s")
    public String getCountName() {
        return countName;
    }

    /**
     * @see Builder
     * @deprecated Since Creation, use builder to create tags
     */
    @Deprecated
    public void setCountName(String countCumName) {
        this.countName = countCumName;
    }

    /**
     * 1 time operation to fill count and name form countName
     *
     * @see PlaceTag#getCount()
     * @see PlaceTag#getName() ()
     */
    @DynamoDBIgnore
    private void fillCountName() {
        Iterator<String> split = Splitter.on('|').split(countName).iterator();
        if (split.hasNext())
            count = Integer.parseInt(split.next());
        if (split.hasNext())
            name = split.next();
    }

    @DynamoDBIgnore
    public String getName() {
        if (name == null) {
            fillCountName();
        }
        return name;
    }

    @DynamoDBIgnore
    public int getCount() {
        if (count == 0) {
            fillCountName();
        }
        return count;
    }

    /**
     * Builder tool for place tags
     * All Place tags must be created with Builder tool so that the read and write efficiency is met
     */
    public static class Builder {

        private final String placeId; // Place Id
        private final int maxDigitSize;

        public Builder(String placeId, int maxCount) {
            this.placeId = placeId;
            this.maxDigitSize = getDigitCount(maxCount);
        }

        /**
         * http://stackoverflow.com/questions/1306727/way-to-get-number-of-digits-in-an-int
         *
         * @param n number to check digits with
         * @return total digits of a number
         */
        public static int getDigitCount(int n) {
            if (n < 100000) {
                // 5 or less
                if (n < 100) {
                    // 1 or 2
                    if (n < 10)
                        return 1;
                    else
                        return 2;
                } else {
                    // 3 or 4 or 5
                    if (n < 1000)
                        return 3;
                    else {
                        // 4 or 5
                        if (n < 10000)
                            return 4;
                        else
                            return 5;
                    }
                }
            } else {
                // 6 or more
                if (n < 10000000) {
                    // 6 or 7
                    if (n < 1000000)
                        return 6;
                    else
                        return 7;
                } else {
                    // 8 to 10
                    if (n < 100000000)
                        return 8;
                    else {
                        // 9 or 10
                        if (n < 1000000000)
                            return 9;
                        else
                            return 10;
                    }
                }
            }
        }

        /**
         * @param count count of tags
         * @param tag   tag name
         * @return trailing zeros + count + | + name
         */
        String convertSort(int count, String tag) {
            return String.format("%0" + maxDigitSize + "d", count) + "|" + tag;
        }

        /**
         * Create Place Tag with Builder
         *
         * @param tag   name of tag
         * @param count count of tags
         * @return created PlaceTag
         */
        public PlaceTag create(String tag, int count) {
            PlaceTag placeTag = new PlaceTag();
            placeTag.placeId = this.placeId;
            placeTag.countName = convertSort(count, tag);
            return placeTag;
        }

        /**
         * Helper tool to map tags to place tag
         *
         * @param placeId place id that the tags belongs to
         * @param tags    map of tags
         * @return Generated List of PlaceTag
         */
        public static List<PlaceTag> createList(final String placeId, Map<String, Integer> tags) {
            //noinspection OptionalGetWithoutIsPresent
            Builder builder = new Builder(placeId, tags.values().stream().max(Integer::compare).get());
            return tags.entrySet().stream()
                    .map(es -> builder.create(es.getKey(), es.getValue()))
                    .collect(Collectors.toList());
        }
    }
}
