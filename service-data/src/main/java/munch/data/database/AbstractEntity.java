package munch.data.database;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Created by: Fuxing
 * Date: 23/8/2017
 * Time: 12:28 AM
 * Project: munch-core
 */
@MappedSuperclass
public abstract class AbstractEntity<T> {
    private Long cycleNo;

    private T data;

    @Column(nullable = false)
    public Long getCycleNo() {
        return cycleNo;
    }

    public void setCycleNo(Long cycleNo) {
        this.cycleNo = cycleNo;
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
