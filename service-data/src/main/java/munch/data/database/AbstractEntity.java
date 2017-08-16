package munch.data.database;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.function.Function;

/**
 * Created by: Fuxing
 * Date: 16/8/2017
 * Time: 10:25 PM
 * Project: munch-core
 */
@MappedSuperclass
public abstract class AbstractEntity<D> {

    private D data;
    private final Function<D, String> idMapper;

    protected AbstractEntity(Function<D, String> idMapper) {
        this.idMapper = idMapper;
    }

    @Id
    @Column(columnDefinition = "CHAR(36)", nullable = false, updatable = false)
    public String getId() {
        return idMapper.apply(data);
    }

    private void setId(String id) {
    }

    @Type(type = "data")
    @Column(nullable = false)
    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }
}
