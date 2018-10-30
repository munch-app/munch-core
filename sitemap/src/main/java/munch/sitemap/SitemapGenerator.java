package munch.sitemap;

import catalyst.utils.NamedCounter;
import com.google.common.io.Files;
import com.munch.utils.file.AccessControl;
import com.munch.utils.file.ContentTypeError;
import com.munch.utils.file.FileMapper;
import com.redfin.sitemapgenerator.SitemapIndexGenerator;
import com.redfin.sitemapgenerator.WebSitemapGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 30/10/18
 * Time: 9:05 AM
 * Project: munch-core
 */
@Singleton
@SuppressWarnings("UnstableApiUsage")
public final class SitemapGenerator {
    private static final Logger logger = LoggerFactory.getLogger(SitemapGenerator.class);

    private final Set<SitemapProvider> providers;
    private final FileMapper fileMapper;
    private final NamedCounter counter = new NamedCounter(logger, 1000);

    @Inject
    public SitemapGenerator(Set<SitemapProvider> providers, FileMapper fileMapper) {
        this.providers = providers;
        this.fileMapper = fileMapper;
    }

    /**
     * Generate and upload to AWS S3
     */
    public void uploadGenerated() throws IOException, ContentTypeError {
        for (File file : generate()) {
            fileMapper.put(file.getName(), file, AccessControl.PublicRead);
        }
    }

    /**
     * @return all the files including the index
     */
    private List<File> generate() throws IOException {
        File dir = Files.createTempDir();
        WebSitemapGenerator wsg = WebSitemapGenerator.builder("https://www.munch.app", dir).build();

        for (SitemapProvider provider : providers) {
            provider.provide().forEachRemaining(url -> {
                counter.increment("Url");
                wsg.addUrl(url);
            });
        }

        // Track and print total number of URL
        counter.print();

        List<File> files = new ArrayList<>(wsg.write());
        files.add(createIndex(dir, files));
        return files;
    }

    private File createIndex(File dir, List<File> files) throws MalformedURLException {
        File index = new File(dir, "index.xml");
        SitemapIndexGenerator sig = new SitemapIndexGenerator("https://www.munch.app", index);
        for (File file : files) {
            sig.addUrl("https://www.munch.app/sitemap/" + file.getName());
        }
        sig.write();
        return index;
    }
}
