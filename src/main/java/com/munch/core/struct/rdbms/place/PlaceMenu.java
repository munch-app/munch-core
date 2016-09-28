package com.munch.core.struct.rdbms.place;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.munch.core.essential.util.AWSUtil;
import com.munch.core.struct.rdbms.abs.AbsSortData;
import com.munch.core.struct.rdbms.abs.HashSetData;
import com.munch.core.struct.util.object.MenuSetting;
import com.munch.core.struct.util.object.StorageMapper;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
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
public class PlaceMenu extends AbsSortData implements HashSetData {

    public static final int TYPE_PDF = 5_100;
    public static final int TYPE_IMAGE = 5_200;
    public static final int TYPE_WEBSITE = 5_300;

    private String id;
    private String name; // Editable title
    private String caption;
    private Integer type;

    // For Website Based, it is use URL
    // For Image/PDF, will be url + fileKeyId
    private String url;
    private String keyId;

    // Transient File
    private File file;
    private String fileName;

    // Storage mapper for Sorted Image
    private static StorageMapper storageMapper = new StorageMapper(AWSUtil.getS3(), new MenuSetting());

    /**
     * Upload PDF Menu
     *
     * @param file       pdf file
     * @param fileName   file name of pdf (filePart.getName)
     * @param websiteUrl website url of the original source of menu
     */
    public void setPdf(File file, String fileName, String websiteUrl) {
        this.file = file;
        this.fileName = fileName;
        setType(TYPE_PDF);
        setUrl(websiteUrl);
        setKeyId(RandomStringUtils.randomAlphanumeric(40) + "." + FilenameUtils.getExtension(fileName));
    }

    /**
     * Upload Image Menu
     *
     * @param file       image file
     * @param fileName   file name of image (filePart.getName)
     * @param websiteUrl website url of the original source of menu
     */
    public void setImage(File file, String fileName, String websiteUrl) {
        this.file = file;
        this.fileName = fileName;
        setType(TYPE_IMAGE);
        setUrl(websiteUrl);
        setKeyId(RandomStringUtils.randomAlphanumeric(40) + "." + FilenameUtils.getExtension(fileName));
    }

    /**
     * Add a Website Menu (HTML)
     *
     * @param webUrl website url of the html menu
     */
    public void setWebsite(String webUrl) {
        setType(TYPE_WEBSITE);
        setUrl(webUrl);
    }

    @PrePersist
    @Override
    protected void onCreate() {
        super.onCreate();
        // Uploaded file are defaulted to public
        if (file != null) {
            // Do Actual image put
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.addUserMetadata("originalFileName", fileName);
            storageMapper.putObject(keyId, file, metadata, CannedAccessControlList.PublicRead);
        } else if (getType() != TYPE_WEBSITE) {
            throw new IllegalArgumentException("File not available. ()");
        }
    }

    /**
     * Not allowed, delete/remove this object and create another again.
     */
    @PreUpdate
    @Override
    protected void onUpdate() {
        throw new IllegalArgumentException("Not allowed, delete/remove this object and create another again.");
    }

    /**
     * Auto delete file from S3 before being removed from db
     */
    @PreRemove
    protected void onRemove() {
        if (getType() != TYPE_WEBSITE) {
            storageMapper.removeObject(getKeyId());
        }
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

    @Column(length = 50)
    public String getName() {
        return name;
    }

    /**
     * @param name name/title of menu
     */
    public void setName(String name) {
        this.name = name;
    }

    @Column(length = 255)
    public String getCaption() {
        return caption;
    }

    /**
     * @param caption caption/description of menu
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Column(nullable = false)
    public Integer getType() {
        return type;
    }

    protected void setType(Integer type) {
        this.type = type;
    }

    @Column(length = 512)
    public String getUrl() {
        return url;
    }

    protected void setUrl(String url) {
        this.url = url;
    }

    @Column(nullable = false, unique = true, length = 60)
    public String getKeyId() {
        return keyId;
    }

    protected void setKeyId(String fileKey) {
        this.keyId = fileKey;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return equals(obj, getClass());
    }

}
