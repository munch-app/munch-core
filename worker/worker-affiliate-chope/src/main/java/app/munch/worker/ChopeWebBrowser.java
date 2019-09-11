package app.munch.worker;

import app.munch.utils.website.WebsiteBrowser;
import dev.fuxing.err.ValidationException;
import org.jsoup.nodes.Document;

import javax.inject.Singleton;
import java.io.IOException;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 11/9/19
 * Time: 11:14 pm
 */
@Singleton
public final class ChopeWebBrowser {
    private final WebsiteBrowser browser = new WebsiteBrowser("https://www.chope.co");

    /**
     * https://www.chope.co/singapore-restaurants/list_of_restaurants
     *
     * @return List of URL for restaurant in singapore
     * @throws IOException network browser error
     */
    List<String> fetch() throws IOException {
        Document document = browser.open("/singapore-restaurants/list_of_restaurants", 3, Duration.ofMinutes(10));
        return document.select("#content ul li.cf > a")
                .stream()
                .map(element -> element.absUrl("href"))
                .collect(Collectors.toList());
    }

    public Iterator<String> iterator() throws IOException {
        List<String> list = fetch();
        if (list.size() < 1000) {
            throw new ValidationException("size", "must be more than 1000, to prevent corruption of data.");
        }
        return list.iterator();
    }

    public Document get(String url) throws IOException {
        return browser.open(url, 3, Duration.ofMinutes(10));
    }
}
