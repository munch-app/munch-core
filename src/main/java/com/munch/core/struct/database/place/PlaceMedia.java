package com.munch.core.struct.database.place;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.munch.core.essential.source.DataSource;
import com.munch.core.struct.database.abs.AbsAuditData;
import com.munch.core.struct.database.media.PlaceS3Setting;
import com.munch.core.struct.database.media.S3Setting;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.*;
import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 11/2/2016
 * Time: 6:48 PM
 * Project: vital-core
 */
@Entity
public class PlaceMedia extends AbsAuditData {

    public static final int STATUS_PRESIGNED = 100;
    public static final int STATUS_UPLOADED = 200;

    public static final int TYPE_IMAGE = 2000;
    public static final int TYPE_VIDEO = 3000;

    public static final int SOURCE_FACEBOOK = DataSource.FACEBOOK;
    public static final int SOURCE_GOOGLE = DataSource.GOOGLE;
    public static final int SOURCE_WEBSITE = DataSource.WEBSITE;

    private static final S3Setting setting = new PlaceS3Setting();

    // Transient
    private File file;

    // Presigned
    private int presignedExpiryFromNow;
    private URL presignedUrl;

    // Persisted
    private String key;
    private String description;
    private String originalName;

    private String sourceUrl;
    private int sourceType;

    // Location associated to this place
    private Set<PlaceLocation> locations;

    private int type;
    private int status;

    // TODO all sizes of the image to persist here, and push from lamba to here once resized. Store in different bucket
    // Image size to be pushed also
    // Image Attribute to display which image at which order in which location


    /**
     * Set and generate a presigned url the url will only be ready once the user upload the model to hibernate
     *
     * @param fileName        file name of file to upload
     * @param presignedExpiry presigned url millis to expiry from now
     */
    public void setPresignedUrl(String fileName, int presignedExpiry, int type) {
        setOriginalName(fileName);
        setKey(RandomStringUtils.randomAlphanumeric(40) + "." + FilenameUtils.getExtension(fileName));
        setStatus(STATUS_PRESIGNED);
        setType(type);

        this.presignedExpiryFromNow = presignedExpiry;
    }

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

        // Upload actual file
        if (file != null) {
            // Add to aws
            // Force setup amazon s3 first
            // Uploaded file are defaulted to public
            setting.getAmazonS3().putObject(new PutObjectRequest(getBucket(), key, file).withCannedAcl(CannedAccessControlList.PublicRead));
        } else {
            if (getStatus() == STATUS_PRESIGNED) {
                Date expiration = new Date();
                expiration.setTime(expiration.getTime() + presignedExpiryFromNow);

                GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(getBucket(), key);
                generatePresignedUrlRequest.setMethod(HttpMethod.PUT);
                generatePresignedUrlRequest.setExpiration(expiration);

                presignedUrl = setting.getAmazonS3().generatePresignedUrl(generatePresignedUrlRequest);
            } else {
                throw new IllegalArgumentException("File not available.");
            }
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

    @Transient
    public URL getPresignedPutUrl() {
        return presignedUrl;
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

    @Column(nullable = true, length = 50)
    public String getOriginalName() {
        return originalName;
    }

    protected void setOriginalName(String originalFileName) {
        this.originalName = originalFileName;
    }

    @Column(nullable = true, length = 600)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // TODO
//    public Set<PlaceLocation> getLocations() {
//        return locations;
//    }
//
//    public void setLocations(Set<PlaceLocation> locations) {
//        this.locations = locations;
//    }

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
