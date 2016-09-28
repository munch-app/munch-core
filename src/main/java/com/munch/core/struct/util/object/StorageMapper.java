package com.munch.core.struct.util.object;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.munch.core.essential.util.MunchConfig;

import java.io.File;

/**
 * Created By: Fuxing Loh
 * Date: 28/9/2016
 * Time: 7:33 PM
 * Project: struct
 */
public class StorageMapper {

    private final AmazonS3 amazonS3;
    private final StorageSetting storageSetting;

    public StorageMapper(AmazonS3 amazonS3, StorageSetting storageSetting) {
        this.amazonS3 = amazonS3;
        this.storageSetting = storageSetting;
    }

    public AmazonS3 getAmazonS3() {
        return amazonS3;
    }

    public StorageSetting getStorageSetting() {
        return storageSetting;
    }

    public String getBucket() {
        return storageSetting.getBucket();
    }

    public String getRegion() {
        return storageSetting.getRegion();
    }

    /**
     * Put file with:
     *
     * @param keyId id for the file
     * @param file  file to put
     * @param acl   acl of the file
     */
    public void putObject(String keyId, File file, CannedAccessControlList acl) {
        getAmazonS3().putObject(new PutObjectRequest(getBucket(), keyId, file).withCannedAcl(acl));
    }

    /**
     * Put file with:
     *
     * @param keyId    id for the file
     * @param metadata meta data
     * @param file     file to put
     * @param acl      acl of the file
     */
    public void putObject(String keyId, File file, ObjectMetadata metadata, CannedAccessControlList acl) {
        getAmazonS3().putObject(
                new PutObjectRequest(getBucket(), keyId, file)
                        .withMetadata(metadata)
                        .withCannedAcl(acl));
    }

    /**
     * Remove object from Amazaon S3
     *
     * @param keyId key id of the object to remove
     */
    public void removeObject(String keyId) {
        getAmazonS3().deleteObject(getBucket(), keyId);
    }

    /**
     * Need a cleaner way to transport url and build url
     *
     * @param keyId key id of the Image
     * @return Generated Url of the image
     */
    public String buildUrl(String keyId) {
        if (MunchConfig.getInstance().isDev()) {
            return String.format("%s/%s/%s", MunchConfig.getInstance().getString("development.aws.s3.endpoint"), getBucket(), keyId);
        }
        return String.format("http://s3-%s.amazonaws.com/%s/%s", getRegion(), getBucket(), keyId);
    }
}
