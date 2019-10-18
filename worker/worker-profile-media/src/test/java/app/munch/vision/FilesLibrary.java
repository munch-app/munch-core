package app.munch.vision;

import com.google.common.io.Resources;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * Created by: Fuxing
 * Date: 15/7/18
 * Time: 3:50 PM
 * Project: vision-service
 */
public final class FilesLibrary {
    public static Stream<File> stream() {
        try {
            Path path = new File(
                    Resources.getResource("files/a.jpg").toURI()
            ).getParentFile().toPath();

            return Files.walk(path)
                    .map(Path::toFile)
                    .sorted(Comparator.comparing(File::getName))
                    .filter(file -> !file.isDirectory());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
