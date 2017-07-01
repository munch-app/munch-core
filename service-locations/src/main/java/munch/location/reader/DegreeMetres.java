package munch.location.reader;

/**
 * Created By: Fuxing Loh
 * Date: 16/6/2017
 * Time: 9:42 PM
 * Project: munch-core
 */
public class DegreeMetres {

    // In kilometers
    private static final double factor = 111.325;

    public static double kmToDegree(double km) {
        return km / factor;
    }
}
