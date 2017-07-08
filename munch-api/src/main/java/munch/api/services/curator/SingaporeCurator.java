package munch.api.services.curator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.clients.PlaceClient;
import munch.api.data.LatLng;
import munch.api.data.Location;
import munch.api.data.PlaceCollection;
import munch.api.data.SearchQuery;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Use this curator if there is no Location data at all
 * 1. No polygon
 * 2. No latLng
 * This curator is compatible if there is query or filter data
 * <p>
 * Created by: Fuxing
 * Date: 1/7/2017
 * Time: 10:15 PM
 * Project: munch-core
 */
@Singleton
public class SingaporeCurator extends Curator {
    private static final String[] POINTS_BISHAN = new String[]{"1.3643644097063403,103.84341716766357", "1.3638924747085996,103.84650707244873", "1.363077314039988,103.84869575500488", "1.3603744109020752,103.85212898254395", "1.3571137618176285,103.85101318359375", "1.3505066436261501,103.85663509368896", "1.3430414365289747,103.85985374450684", "1.344028218088107,103.84552001953125", "1.3450149992485252,103.8413143157959", "1.3444572534242645,103.8389539718628", "1.3511501948903304,103.83976936340332", "1.3549256922034412,103.84118556976318", "1.3570279552033715,103.84148597717285", "1.3588084418253272,103.84129285812378", "1.3604602173974558,103.84170055389404", "1.3643644097063403,103.84341716766357"};
    private static final String[] POINTS_ONE_NORTH = new String[]{"1.3036771111496797,103.78711223602295", "1.3018536804743503,103.78629684448242", "1.2998586312835478,103.78528833389282", "1.2939592830658952,103.78612518310547", "1.289068176683197,103.79284143447876", "1.2903982153153257,103.79457950592041", "1.2918355143455886,103.79786252975464", "1.294087996264556,103.79906415939331", "1.2982926238251127,103.79593133926392", "1.30065236072045,103.79530906677246", "1.3011243078348445,103.79226207733154", "1.3014246377707814,103.79093170166016", "1.3014675420444106,103.78955841064453", "1.303001369347451,103.78896832466125", "1.3036771111496797,103.78711223602295"};
    private static final String[] POINTS_THOMSON = new String[]{"1.355751581456867,103.83016705513", "1.353552784769583,103.82882595062256", "1.3499596249176187,103.83215188980103", "1.3492195405866363,103.83325695991516", "1.3501955937873715,103.83516669273376", "1.3518044718341302,103.83764505386353", "1.3532739128520377,103.83965134620667", "1.3553547256170044,103.83724808692932", "1.356529204197699,103.83552610874176", "1.3574355365940158,103.83383631706238", "1.355751581456867,103.83016705513"};
    private static final String[] POINTS_SERANGOON_GARDENS = new String[]{"1.3717222933585818,103.86817932128906", "1.3706926200372191,103.8648533821106", "1.3693197215867106,103.86124849319458", "1.3616400563995428,103.85875940322876", "1.356706180372775,103.85684967041016", "1.3554190806224888,103.8568925857544", "1.3537243982413203,103.86412382125854", "1.3555263389611376,103.8678789138794", "1.3601598946502338,103.86826515197754", "1.363677958769941,103.86785745620728", "1.3635438862985774,103.86974573135376", "1.363238201035924,103.8712477684021", "1.3624981207656577,103.87309312820435", "1.363677958769941,103.87408018112183", "1.3648148930268882,103.87197732925415", "1.3659035607031396,103.87118339538574", "1.3678931934574605,103.8703465461731", "1.3697755668626195,103.86987447738647", "1.3716579387889734,103.86983156204224", "1.3717222933585818,103.86817932128906"};

    @Inject
    public SingaporeCurator(PlaceClient placeClient) {
        super(placeClient);
    }

    /**
     * Location data is ignored
     * Singapore curator only respect this values
     * 1. query
     * 2. filters
     *
     * @param query  mandatory query in search bar, polygon will be ignored
     * @param latLng ignored for Singapore curator
     * @return Curated List of PlaceCollection
     */
    @Override
    protected List<PlaceCollection> curate(SearchQuery query, @Nullable LatLng latLng) {
        List<PlaceCollection> collections = new ArrayList<>();

        if (isEmpty(query)) {
            // If query is empty: means location collections
            collections.add(locationQuery("BISHAN", query, POINTS_BISHAN));
            collections.add(locationQuery("ONE NORTH", query, POINTS_ONE_NORTH));
            collections.add(locationQuery("THOMSON", query, POINTS_THOMSON));
            collections.add(locationQuery("SERANGOON GARDENS", query, POINTS_SERANGOON_GARDENS));
        } else {
            // Else do single collection search
            query.setFrom(0);
            query.setSize(15);
            collections.add(new PlaceCollection(null, query, placeClient.search(query)));
        }
        return collections;
    }

    /**
     * Create PlaceLocation with no place data populated
     *
     * @param name   name collection name
     * @param source source of query that will be cloned
     * @param points points for polygon
     * @return collection created
     */
    protected static PlaceCollection locationQuery(String name, SearchQuery source, String[] points) {
        SearchQuery query = clone(source);
        query.setLocation(new Location());
        query.getLocation().setPoints(Arrays.asList(points));
        return new PlaceCollection(name, query);
    }
}
