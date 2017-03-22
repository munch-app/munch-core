package munch.document.hibernate;

import org.hibernate.dialect.PostgreSQL82Dialect;

import java.sql.Types;

/**
 * Created By: Fuxing Loh
 * Date: 5/1/2017
 * Time: 3:56 PM
 * Project: corpus-catalyst
 */
public class JsonPostgreSQLDialect extends PostgreSQL82Dialect {

    public JsonPostgreSQLDialect() {
        super();
        registerColumnType(Types.JAVA_OBJECT, "jsonb");
    }
}
