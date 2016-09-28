package com.munch.core.struct.rdbms.place;

import com.munch.core.struct.rdbms.abs.AbsSortData;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.File;

/**
 * Created By: Fuxing Loh
 * Date: 28/9/2016
 * Time: 7:00 PM
 * Project: struct
 */
@Entity
public class PlaceMenu extends AbsSortData {

    public static final int TYPE_PDF = 5_100;
    public static final int TYPE_IMAGE = 5_200;
    public static final int TYPE_WEBSITE = 5_300;

    private String id;
    private String name; // Editable title
    private Integer type;

    // For Website Based, it is use URL
    // For Image/PDF, will be url + fileKeyId
    private String url;
    private String keyId;

    // Transient File
    private File file; // TODO

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

    @Column(length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(nullable = false)
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Column(length = 512)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Column(nullable = false, unique = true, length = 55)
    public String getKeyId() {
        return keyId;
    }

    protected void setKeyId(String fileKey) {
        this.keyId = fileKey;
    }

    @Transient
    public File getFile() {
        return file;
    }
}
