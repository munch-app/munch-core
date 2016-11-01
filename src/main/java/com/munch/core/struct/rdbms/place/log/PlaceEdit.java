package com.munch.core.struct.rdbms.place.log;

import com.munch.core.essential.util.DateTime;
import com.munch.core.struct.rdbms.abs.HashSetData;
import com.munch.core.struct.util.map.ManyEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
public class PlaceEdit implements HashSetData, ManyEntity<PlaceLog> {

    public static final int AUTHORITY_MUNCH_STAFF = 327;
    public static final int AUTHORITY_RESTAURANT_OWNER = 500;

    private String id;
    private PlaceLog log;

    private Integer authority;
    private String user; // User Id from AdminAccount or Munch Id
    private Date editedDate;

    @PrePersist
    protected void onCreate() {
        setEditedDate(new Timestamp(DateTime.millisNow()));
    }

    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "CHAR(32)")
    @Id
    public String getId() {
        return id;
    }

    protected void setId(String id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public PlaceLog getLog() {
        return log;
    }

    public void setLog(PlaceLog log) {
        this.log = log;
    }

    @Column(nullable = false)
    public Integer getAuthority() {
        return authority;
    }

    public void setAuthority(Integer authority) {
        this.authority = authority;
    }

    @Column(nullable = false, length = 100)
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Column(nullable = false)
    public Date getEditedDate() {
        return editedDate;
    }

    public void setEditedDate(Date editedDate) {
        this.editedDate = editedDate;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public void setOneEntity(PlaceLog single) {
        setLog(single);
    }
}
