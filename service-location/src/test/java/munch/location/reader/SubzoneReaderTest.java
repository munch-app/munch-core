package munch.location.reader;

import munch.location.database.LocationV2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

/**
 * Created By: Fuxing Loh
 * Date: 16/6/2017
 * Time: 9:09 PM
 * Project: munch-core
 */
class SubzoneReaderTest {

    private SubzoneReader reader;

    @BeforeEach
    void setUp() throws Exception {
        reader = new SubzoneReader();
    }

    @Test
    void readTest() throws Exception {
        for (LocationV2 locationV2 : reader.read()) {
            System.out.println(locationV2.getName());
        }
    }

    @Test
    void count() throws Exception {
        long size = reader.read().stream()
                .filter(Objects::isNull)
                .count();
        System.out.println(size);
    }
}