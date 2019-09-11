package app.munch.worker;

import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * Created by: Fuxing
 * Date: 12/9/19
 * Time: 12:51 am
 */
class ChopeAffiliateParserTest {

    private ChopeWebBrowser browser = new ChopeWebBrowser();

    @Test
    void crawl() throws IOException {
        Document document = browser.get("https://www.chope.co/singapore-restaurants/restaurant/10-scotts");

        Assertions.assertEquals("4617", ChopeAffiliateParser.RID.findRid(document));

        Assertions.assertNotNull(ChopeAffiliateParser.LinkedData.find(document));
    }

    @Test
    void link() {
        Assertions.assertEquals("https://book.chope.co/booking?rid=4617&source=munch", ChopeAffiliateParser.Linking.build("4617"));
    }
}
