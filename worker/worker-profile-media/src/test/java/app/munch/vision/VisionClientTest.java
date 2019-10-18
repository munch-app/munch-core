package app.munch.vision;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

/**
 * @author Fuxing Loh
 * @since 2019-10-18 at 8:20 am
 */
class VisionClientTest {

    static VisionClient client;

    @BeforeAll
    static void beforeAll() {
        Injector injector = Guice.createInjector();
        client = injector.getInstance(VisionClient.class);
    }

    @ParameterizedTest
    @MethodSource("files")
    void post(File file) throws IOException {
        VisionResult result = client.post(file);
        System.out.println(file.getName() + ": " + result.getName());
    }

    private static Stream<File> files() {
        return FilesLibrary.stream();
    }
}
