package app.munch.utils.website;

import app.munch.utils.retry.SleepRetry;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;

/**
 * Created by: Fuxing
 * Date: 21/8/18
 * Time: 4:14 PM
 * Project: catalyst
 */
public class WebsiteBrowser {
    private static final Logger logger = LoggerFactory.getLogger(WebsiteBrowser.class);

    protected final String domain;
    protected final String host;
    protected int timeout = 30_000;

    public WebsiteBrowser(String domain) {
        this.domain = domain;
        try {
            this.host = new URI(domain).getHost();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param path    to open
     * @param retries number of retries, per 30 seconds interval
     * @return Document
     * @throws IOException exception that fall though
     */
    public Document open(String path, int retries) throws IOException {
        return open(path, retries, Duration.ofSeconds(30));
    }

    /**
     * @param path     to open
     * @param retries  number of retries
     * @param duration duration to sleep between each retry
     * @return Document
     * @throws IOException exception that fall though
     */
    public Document open(String path, int retries, Duration duration) throws IOException {
        SleepRetry retry = new SleepRetry(retries, duration) {
            @Override
            public void log(Throwable exception, int executionCount) {
                WebsiteBrowser.logger.warn("Exception caught, retry count: {}", executionCount, exception);
            }
        };
        return retry.loop(() -> open(path));
    }

    /**
     * @param path to open
     * @return Document
     * @throws IOException failed to load website
     */
    public Document open(String path) throws IOException {
        return Jsoup.connect(domain + path)
                .header("Connection", "keep-alive")
                .header("Host", host)
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6,ms;q=0.4")
                .timeout(timeout)
                .get();
    }
}
