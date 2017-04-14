package munch.geocoder.database;

import org.hibernate.spatial.dialect.h2geodb.GeoDBDialect;
import org.hibernate.spatial.dialect.h2geodb.GeoDBGeometryTypeDescriptor;

/**
 * Created By: Fuxing Loh
 * Date: 14/4/2017
 * Time: 8:29 PM
 * Project: munch-core
 */
public class GeoH2Dialect extends GeoDBDialect {

    public GeoH2Dialect() {
        super();
        registerColumnType( GeoDBGeometryTypeDescriptor.INSTANCE.getSqlType(), "blob" );
    }
}
