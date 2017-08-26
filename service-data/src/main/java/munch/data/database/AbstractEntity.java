package munch.data.database;

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

    @Column(nullable = false)
    public Long getCycleNo() {
        return cycleNo;
    }

    public void setCycleNo(Long cycleNo) {
        this.cycleNo = cycleNo;
    }

    public abstract T getData();

    public abstract void setData(T data);
}
