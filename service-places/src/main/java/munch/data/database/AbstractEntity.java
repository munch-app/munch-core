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
public abstract class AbstractEntity<T> {

    private T data;
    private final Function<T, String> idMapper;

    public AbstractEntity(Function<T, String> idMapper) {
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
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
