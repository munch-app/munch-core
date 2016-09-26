package com.munch.core.struct.nosql.place;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created By: Fuxing Loh
 * Date: 26/9/2016
 * Time: 8:52 PM
 * Project: struct
 */
public class BuilderTest {

    @Test
    public void convertSort() throws Exception {
        PlaceTag.Builder builder = new PlaceTag.Builder("8a80cb8157381d0f0157381d9c4602dc", 103);
        builder.convertSort(100, "pot");
    }

    @Test
    public void getDigitCount() throws Exception {
        assertThat(PlaceTag.Builder.getDigitCount(10000)).isEqualTo(5);
        assertThat(PlaceTag.Builder.getDigitCount(99000)).isEqualTo(5);
    }

    @Test
    public void testBuilder() {
        PlaceTag.Builder builder = new PlaceTag.Builder("8a80cb8157381d0f0157381d9c4602dc", 103);
        PlaceTag tag1 = builder.create("chicken", 100);
        PlaceTag tag2 = builder.create("chicken pie", 1);
        PlaceTag tag3 = builder.create("chicken pot", 20);

        assertThat(tag1.getCountCumName()).isEqualTo("100|chicken");
        assertThat(tag1.getCount()).isEqualTo(100);
        assertThat(tag1.getName()).isEqualTo("chicken");

        assertThat(tag2.getCountCumName()).isEqualTo("001|chicken pie");
        assertThat(tag2.getCount()).isEqualTo(1);
        assertThat(tag2.getName()).isEqualTo("chicken pie");

        assertThat(tag3.getCountCumName()).isEqualTo("020|chicken pot");
        assertThat(tag3.getCount()).isEqualTo(20);
        assertThat(tag3.getName()).isEqualTo("chicken pot");
    }

    @Test
    public void testBuilderBigger() {
        PlaceTag.Builder builder = new PlaceTag.Builder("8a80cb8157381d0f0157381d9c4602dc", 10300);
        PlaceTag tag1 = builder.create("chicken", 100);
        PlaceTag tag2 = builder.create("chicken pie", 1);
        PlaceTag tag3 = builder.create("chicken pot", 20);

        PlaceTag tag4 = builder.create("chicken pot 2", 2000);
        PlaceTag tag5 = builder.create("chicken pot 3", 10000);

        assertThat(tag1.getCountCumName()).isEqualTo("100|chicken");
        assertThat(tag1.getCount()).isEqualTo(100);
        assertThat(tag1.getName()).isEqualTo("chicken");

        assertThat(tag2.getCountCumName()).isEqualTo("001|chicken pie");
        assertThat(tag2.getCount()).isEqualTo(1);
        assertThat(tag2.getName()).isEqualTo("chicken pie");

        assertThat(tag3.getCountCumName()).isEqualTo("020|chicken pot");
        assertThat(tag3.getCount()).isEqualTo(20);
        assertThat(tag3.getName()).isEqualTo("chicken pot");

        assertThat(tag4.getCountCumName()).isEqualTo("2000|chicken pot 2");
        assertThat(tag4.getCount()).isEqualTo(2000);
        assertThat(tag4.getName()).isEqualTo("chicken pot 2");

        assertThat(tag5.getCountCumName()).isEqualTo("10000|chicken pot 3");
        assertThat(tag5.getCount()).isEqualTo(10000);
        assertThat(tag5.getName()).isEqualTo("chicken pot 3");
    }
}