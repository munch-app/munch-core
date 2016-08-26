package com.munch.core.struct.rdbms.menu;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.munch.core.essential.source.DataSource;
import com.munch.core.struct.rdbms.abs.AbsSortData;
import com.munch.core.struct.rdbms.media.MenuS3Setting;
import com.munch.core.struct.rdbms.media.S3Setting;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.*;
import java.io.File;

import static com.munch.core.struct.rdbms.place.PlaceMedia.STATUS_UPLOADED;

/**
 * Created By: Fuxing Loh
 * Date: 23/8/2016
 * Time: 9:19 PM
 * Project: struct
 */
@Entity
public class MenuMedia extends AbsSortData {
    public static final int TYPE_IMAGE = 2000;
    public static final int TYPE_VIDEO = 3000;
    public static final int TYPE_PDF = 5000;

    public static final int SOURCE_WEBSITE = DataSource.WEBSITE;

    private static final S3Setting setting = new MenuS3Setting();

    // Transient
    private File file;

    // Persisted
    private String key;
    private String name;
    private String originalName;

    private String sourceUrl;
    private int sourceType;

    private int type;
    private int status;

    /**
     * You have to set object before you can upload the object.
     * You have to validate the file type yourself, due to the restriction of checking file type on the core.
     * Don't use this method if you are using play framework filePart file.
     * Check file is only as accurate as it can get, doing your own checking
     *
     * @param file     the file
     * @param fileName full file name(e.g. filePart.getName)
     */
    public void setFile(File file, String fileName, int type) {
        this.file = file;
        setOriginalName(fileName);
        setKey(RandomStringUtils.randomAlphanumeric(40) + "." + FilenameUtils.getExtension(fileName));
        setStatus(STATUS_UPLOADED);
        setType(type);
    }

    /**
     * You have to set object before you can upload the object.
     * You have to validate the file type yourself, due to the restriction of checking file type on the core.
     * Check file is only as accurate as it can get, doing your own checking
     *
     * @param file the file
     */
    public void setFile(File file, int type) {
        setFile(file, file.getName(), type);
    }

    @Transient
    public String getUrl() {
        if (setting.getConfig().isDev()) {
            return String.format("%s/%s/%s", setting.getConfig().getString("development.aws.s3.endpoint"), getBucket(), getKey());
        }
        return String.format("http://s3-%s.amazonaws.com/%s/%s", setting.getRegion(), getBucket(), getKey());
    }

    @PrePersist
    @Override
    protected void onCreate() {
        super.onCreate();
        // Uploaded file are defaulted to public
        if (file != null) {
            setting.getAmazonS3().putObject(new PutObjectRequest(getBucket(), key, file).withCannedAcl(CannedAccessControlList.PublicRead));
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
        setting.getAmazonS3().deleteObject(getBucket(), key);
    }

    @Transient
    public File getFile() {
        return file;
    }

    @Id
    @Column(nullable = false, unique = true, length = 55)
    public String getKey() {
        return key;
    }

    protected void setKey(String fileKey) {
        this.key = fileKey;
    }

    @Transient
    public String getBucket() {
        return setting.getBucket();
    }

    @Column(nullable = true, length = 80)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(nullable = true, length = 50)
    public String getOriginalName() {
        return originalName;
    }

    protected void setOriginalName(String originalFileName) {
        this.originalName = originalFileName;
    }

    @Column(nullable = true, length = 500)
    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    @Column
    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    @Column
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Column
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
