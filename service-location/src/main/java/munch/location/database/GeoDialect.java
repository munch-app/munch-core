package munch.location.database;

import org.hibernate.spatial.dialect.h2geodb.GeoDBDialect;
import org.hibernate.spatial.dialect.h2geodb.GeoDBGeometryTypeDescriptor;

/**
 * Created By: Fuxing Loh
 * Date: 14/4/2017
 * Time: 8:29 PM
 * Project: munch-core
 */
public class GeoDialect extends GeoDBDialect {

    public GeoDialect() {
        super();
        registerColumnType( GeoDBGeometryTypeDescriptor.INSTANCE.getSqlType(), "blob" );
    }
}
