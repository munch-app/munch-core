package com.munch.core.struct.rdbms.media;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.munch.core.essential.util.AWSUtil;
import com.munch.core.struct.rdbms.abs.AbsSortData;
import com.munch.core.struct.rdbms.abs.HashSetData;
import com.munch.core.struct.util.object.PlaceImageSetting;
import com.munch.core.struct.util.object.StorageMapper;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.File;

/**
 * Created By: Fuxing Loh
 * Date: 28/9/2016
 * Time: 7:26 PM
 * Project: struct
 */
@Entity
public class SortedImage extends AbsSortData implements HashSetData {

    private String id;
    private String keyId;

    private String caption;
    // Image Type e.g: Round, Square and such
    private Integer type;

    // Transient File and name
    private File file;
    private String fileName;

    // Storage mapper for Sorted Image
    private static StorageMapper storageMapper = new StorageMapper(AWSUtil.getS3(), new PlaceImageSetting());

    /**
     * You have to set object before you can upload the object.
     * You have to validate the file type yourself, due to the restriction of checking file type on the core.
     * Don't use this method if you are using play framework filePart file.
     * Check file is only as accurate as it can get, doing your own checking
     *
     * @param file     the file
     * @param fileName full file name(e.g. filePart.getName)
     */
    public void setFile(File file, String fileName) {
        this.file = file;
        this.fileName = fileName;
        setKeyId(RandomStringUtils.randomAlphanumeric(40) + "." + FilenameUtils.getExtension(fileName));
    }

    /**
     * You have to set object before you can upload the object.
     * You have to validate the file type yourself, due to the restriction of checking file type on the core.
     * Check file is only as accurate as it can get, doing your own checking
     *
     * @param file the file
     */
    public void setFile(File file) {
        setFile(file, file.getName());
    }

    @PrePersist
    @Override
    protected void onCreate() {
        super.onCreate();
        // Uploaded file are defaulted to public
        if (file != null) {
            // Do Actual image put
            storageMapper.putObject(keyId, file, fileName, CannedAccessControlList.PublicRead);
        } else {
            throw new IllegalArgumentException("File not available.");
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
        storageMapper.removeObject(getKeyId());
    }

    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "CHAR(32)")
    @Id
    @Override
    public String getId() {
        return id;
    }

    protected void setId(String id) {
        this.id = id;
    }

    @Column(length = 255, nullable = false)
    public String getKeyId() {
        return keyId;
    }

    @Column(nullable = false, unique = true, length = 60)
    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    @Column(length = 255, nullable = true)
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Column(nullable = true)
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
