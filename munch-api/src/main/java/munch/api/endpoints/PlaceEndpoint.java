package munch.api.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import munch.api.endpoints.service.DataClient;
import munch.api.endpoints.service.SearchClient;
import munch.restful.server.JsonCall;
import munch.struct.place.*;
import org.apache.commons.lang3.RandomUtils;

import java.util.*;

/**
 * Created by: Fuxing
 * Date: 2/4/2017
 * Time: 4:15 PM
 * Project: munch-core
 */
@Singleton
public class PlaceEndpoint extends AbstractEndpoint {

    private final DataClient dataClient;
    private final SearchClient searchClient;
    private final PlaceRandom placeRandom;

    @Inject
    public PlaceEndpoint(DataClient dataClient, SearchClient searchClient, PlaceRandom placeRandom) {
        this.dataClient = dataClient;
        this.searchClient = searchClient;
        this.placeRandom = placeRandom;
    }

    @Override
    public void route() {
        PATH("/places", () -> {
            POST("/search", this::search);

            GET("/:placeId", this::get);

            GET("/:placeId/gallery", this::gallery);
            GET("/:placeId/articles", this::articles);
        });
    }

    /**
     * @param call    json call
     * @param request body node
     * @return list of place result
     */
    private List<Place> search(JsonCall call, JsonNode request) {
        int from = request.path("from").asInt();
        int size = request.path("size").asInt();

        // Check geometry node exist
        JsonNode geometry = null;
        if (request.has("geometry")) {
            geometry = request.path("geometry");
        }
        return searchClient.search(from, size, geometry);
    }

    private Place get(JsonCall call) {
        String placeId = call.pathString("placeId");
        return dataClient.get(placeId);
    }

    private List<Graphic> gallery(JsonCall call) {
        String placeId = call.pathString("placeId");
        return placeRandom.randomGraphics(RandomUtils.nextInt(20, 30));
    }

    private List<Article> articles(JsonCall call) {
        String placeId = call.pathString("placeId");
        return placeRandom.randomArticles(RandomUtils.nextInt(8, 12));
    }

    /**
     * Created By: Fuxing Loh
     * Date: 27/3/2017
     * Time: 5:06 PM
     * Project: munch-core
     */
    @Singleton
    static class PlaceRandom {

        private final Lorem lorem = LoremIpsum.getInstance();
        private final List<String> establishments = ImmutableList.of(
                "Restaurant", "Bars & Pubs", "Cafe",
                "Buffet", "BBQ", "Hawker", "Steamboat", "Desserts");
        private final List<String> amenities = ImmutableList.of(
                "Halal", "Healthy", "Vegetarian", "Pet Friendly");

        private final List<String> images = ImmutableList.of(
                "https://static.pexels.com/photos/6267/menu-restaurant-vintage-table.jpg",
                "https://static.pexels.com/photos/29346/pexels-photo-29346.jpg",
                "https://static.pexels.com/photos/67468/pexels-photo-67468.jpeg",
                "https://static.pexels.com/photos/260922/pexels-photo-260922.jpeg",
                "https://static.pexels.com/photos/5317/food-salad-restaurant-person.jpg",
                "http://weknowyourdreams.com/images/restaurant/restaurant-07.jpg",
                "http://www.pdcdc.org/wp-content/uploads/2016/03/restaurant-939435_960_720.jpg",
                "http://kazan-restaurant.com/img/large-images/home.jpg",
                "http://everything-pr.com/wp-content/uploads/2015/08/Restaurant.jpg",
                "https://static1.squarespace.com/static/51ab58f4e4b0361e5f3ed294/51ab58f4e4b0361e5f3ed29a/51ab80bee4b0058e26cfcb1f/1370194240299/Benchmark_Restaurant_Dining_Room_Photographed_by_Evan_Sung.jpg",
                "http://spizzico.org/wp-content/uploads/2015/03/spizzico1.jpg",
                "https://cdn.pixabay.com/photo/2015/03/26/10/28/restaurant-691397_960_720.jpg",
                "http://www.riadfes.com/wp-content/uploads/2012/02/restaurant-riad-fes3.jpg",
                "https://media-cdn.tripadvisor.com/media/photo-s/0a/56/44/5a/restaurant.jpg",
                "http://9bf9ac9a3eed53610ead-5ced3b8622611961454de23b8be691ab.r71.cf1.rackcdn.com/XLGallery/BnB-Restaurant.jpg",
                "https://media.timeout.com/images/103756135/630/472/image.jpg",
                "https://www.panpacific.com/content/dam/pp/PP%20Perth/dining/montereys-restaurant/Montereys-Restaurant-1.jpg/_jcr_content/renditions/cq5dam.thumbnail.540.296.png",
                "http://static.asiawebdirect.com/m/bangkok/portals/pattaya-bangkok-com/homepage/best-restaurants/allParagraphs/0/top10Set/02/image/radius-restaurant-1200.jpg",
                "http://www.corinthia.com/application/files/3514/7516/6124/Massimo-Restaurant-and-Bar-Italian-Fine-Dining-London.jpg",
                "http://greensrestaurant.com/wp-content/uploads/2015/03/greens-photo05-1130x430.jpg",
                "http://i.insing.com.sg/business/19/c5/11/00/town-restaurant_800x0_crop_800x800_ddebb6fbf7.jpg",
                "http://s3.amazonaws.com/stnd-narcissa-prod/graphics/assets/000/000/026/medium/narcissa_night_january_2014_cwmosier-5.jpg?1390860090",
                "http://www.hotelvillemarie.com/img/restaurant/lebanese-restaurant-montreal-zawedeh-restaurant.jpg",
                "https://media.timeout.com/images/103700310/630/472/image.jpg",
                "http://nandos.com.sg/nandos_sg/images/restaurants/junction8.png"
//            ,"https://www.ledr.com/colours/white.jpg"
        );

        /**
         * Override random data for testing phase
         *
         * @param place place, add random data to it
         */
        public void random(Place place) {
            place.setPrice(randomPrice());
            place.setPhone("+65 9999 4544");
            place.setWebsite("www.munchapp.co");
            place.setDescription(lorem.getParagraphs(2, 2));

            place.setEstablishments(Collections.singleton(random(establishments)));
            place.setAmenities(Collections.singleton(random(amenities)));

            place.setImages(randomImages());
            place.setMenus(randomMenus());
            place.setHours(randomHours());
        }

        public List<Graphic> randomGraphics(int size) {
            List<Graphic> list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                Graphic graphic = new Graphic();
                graphic.setId(UUID.randomUUID().toString());
                graphic.setMediaId(UUID.randomUUID().toString());
                graphic.setImageUrl(random(this.images));
                list.add(graphic);
            }
            return list;
        }

        public List<Article> randomArticles(int size) {
            List<Article> list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                Article article = new Article();
                article.setId(UUID.randomUUID().toString());
                article.setAuthor(lorem.getFirstName());
                article.setSummary(lorem.getParagraphs(3, 5));
                article.setImageUrl(random(images));
                article.setUrl("http://www.ladyironchef.com/2010/04/best-buffet-singapore/");
                list.add(article);
            }
            return list;
        }

        private Price randomPrice() {
            Price price = new Price();
            price.setLowest(RandomUtils.nextDouble(5, 20));
            price.setHighest(RandomUtils.nextDouble(20, 40));
            return price;
        }

        private Set<Hour> randomHours() {
            Set<Hour> hours = new HashSet<>();
            for (int i = 1; i < 8; i++) {
                Hour hour = new Hour();
                hour.setDay(i);
                hour.setOpen("11:00");
                hour.setClose("22:00");
                hours.add(hour);
            }
            return hours;
        }

        private Set<Menu> randomMenus() {
            Set<Menu> menus = new HashSet<>();
            menus.add(createMenu("https://image.freepik.com/free-vector/restaurant-menu-template_23-2147535344.jpg"));
            menus.add(createMenu("https://s-media-cache-ak0.pinimg.com/originals/e7/a4/ba/e7a4ba1c9764939cde30912f90c3912a.png"));
            menus.add(createMenu("http://hooters.com.sg/images/OurMenu/ChefMenu2011-1.jpg"));
            menus.add(createMenu("https://static1.squarespace.com/static/5566a0a8e4b027e20d761a0e/55cc25bbe4b06cc6fabdde12/55cc28cfe4b0b32ffb4ea4db/1486465413595/PC+Menu+2016_Page_16.jpg?format=500w"));
            menus.add(createMenu("http://hooters.com.sg/images/OurMenu/ChefMenu2011-1.jpg"));
            return menus;
        }

        private static Menu createMenu(String thumbUrl) {
            Menu menu = new Menu();
            menu.setType(Menu.TYPE_IMAGE);
            menu.setThumbUrl(thumbUrl);
            menu.setUrl(thumbUrl);
//        menu.setUrl("http://nandos.com.sg/nandos_sg/download/Nandos-Singapore-Website-Menu-Jan-2017.pdf");
            return menu;
        }

        private Set<Image> randomImages() {
            Set<Image> images = new HashSet<>();
            Image image = new Image();
            image.setUrl(random(this.images));
            images.add(image);
            return images;
        }

        private static String random(List<String> strings) {
            return strings.get(RandomUtils.nextInt(0, strings.size()));
        }
    }
}
