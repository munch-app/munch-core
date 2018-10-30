package munch.sitemap;

import com.google.common.io.Files;
import com.munch.utils.file.AccessControl;
import com.munch.utils.file.ContentTypeError;
import com.munch.utils.file.FileMapper;
import com.redfin.sitemapgenerator.WebSitemapGenerator;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
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
    private final Set<SitemapProvider> providers;
    private final FileMapper fileMapper;

    @Inject
    public SitemapGenerator(Set<SitemapProvider> providers, FileMapper fileMapper) {
        this.providers = providers;
        this.fileMapper = fileMapper;
    }

    /**
     * Generate and upload to AWS S3
     */
    public void uploadGenerated() throws MalformedURLException, ContentTypeError {
        for (File file : generate()) {
            fileMapper.put(file.getName(), file, AccessControl.PublicRead);
        }
    }

    /**
     * @return all the files including the index
     */
    private List<File> generate() throws MalformedURLException {
        File dir = Files.createTempDir();
        WebSitemapGenerator wsg = new WebSitemapGenerator("https://www.munch.app", dir);
        for (SitemapProvider provider : providers) {
            provider.provide().forEachRemaining(wsg::addUrl);
        }

        List<File> files = new ArrayList<>(wsg.write());
        files.add(wsg.writeSitemapsWithIndex());
        return files;
    }
}
