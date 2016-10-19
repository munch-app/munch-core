package com.munch.core.essential.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;

/**
 * Created By: Fuxing Loh
 * Date: 28/9/2016
 * Time: 7:33 PM
 * Project: struct
 */
public class FileMapper {

    private final AmazonS3 amazonS3;
    private final FileSetting fileSetting;

    public FileMapper(AmazonS3 amazonS3, FileSetting fileSetting) {
        this.amazonS3 = amazonS3;
        this.fileSetting = fileSetting;
    }

    protected AmazonS3 getAmazonS3() {
        return amazonS3;
    }

    protected FileSetting getFileSetting() {
        return fileSetting;
    }

    protected String getBucket() {
        return fileSetting.getBucket();
    }

    protected String getRegion() {
        return fileSetting.getRegion();
    }

    /**
     * Put file with:
     *
     * @param keyId id for the file
     * @param file  file to put
     */
    public void putFile(String keyId, File file) {
        putFile(keyId, file, CannedAccessControlList.Private);
    }

    /**
     * Put file with:
     *
     * @param keyId id for the file
     * @param file  file to put
     * @param acl   acl of the file
     */
    public void putFile(String keyId, File file, CannedAccessControlList acl) {
        putFile(keyId, file, null, acl);
    }

    /**
     * Put file with:
     *
     * @param keyId    id for the file
     * @param metadata meta data
     * @param file     file to put
     */
    public void putFile(String keyId, File file, ObjectMetadata metadata) {
        putFile(keyId, file, metadata, CannedAccessControlList.Private);
    }

    /**
     * Put file with:
     *
     * @param keyId    id for the file
     * @param metadata meta data
     * @param file     file to put
     * @param acl      acl of the file
     */
    public void putFile(String keyId, File file, ObjectMetadata metadata, CannedAccessControlList acl) {
        PutObjectRequest request = new PutObjectRequest(getBucket(), keyId, file);
        if (metadata != null) {
            request.withMetadata(metadata);
        }
        if (acl != null) {
            request.withCannedAcl(acl);
        }
        getAmazonS3().putObject(request);
    }

    /**
     * Remove object from Amazaon S3
     *
     * @param keyId key id of the object to remove
     */
    public void removeFile(String keyId) {
        getAmazonS3().deleteObject(getBucket(), keyId);
    }

    /**
     * Need a cleaner way to transport url and build url
     *
     * @param keyId key id of the Image
     * @return Generated Url of the image
     */
    public String fileUrl(String keyId) {
        return String.format("http://s3-%s.amazonaws.com/%s/%s", getRegion(), getBucket(), keyId);
//        if (MunchConfig.getInstance().isDev()) {
//            return String.format("%s/%s/%s", MunchConfig.getInstance().getString("development.aws.s3.endpoint"), getBucket(), keyId);
//        }
    }
}
