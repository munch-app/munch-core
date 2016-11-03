package com.munch.core.essential.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;

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
    public void putFile(String keyId, File file) throws ContentTypeException {
        putFile(keyId, file, CannedAccessControlList.Private);
    }

    /**
     * Put file with:
     *
     * @param keyId id for the file
     * @param file  file to put
     * @param acl   acl of the file
     */
    public void putFile(String keyId, File file, CannedAccessControlList acl) throws ContentTypeException {
        putFile(keyId, file, null, acl);
    }

    /**
     * Put file with:
     *
     * @param keyId    id for the file
     * @param metadata meta data
     * @param file     file to put
     */
    public void putFile(String keyId, File file, ObjectMetadata metadata) throws ContentTypeException {
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
    public void putFile(String keyId, File file, ObjectMetadata metadata, CannedAccessControlList acl) throws ContentTypeException {
        PutObjectRequest request = new PutObjectRequest(getBucket(), keyId, file);
        if (metadata == null) {
            metadata = new ObjectMetadata();
        }
        metadata.setContentType(FileMapperUtils.getContentType(keyId, file));
        metadata.setContentDisposition("inline"); // For Browser to display
        request.withMetadata(metadata);

        // Update Access Control List
        if (acl != null) {
            request.withCannedAcl(acl);
        }
        // Put Object
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
     * Get file from AmazonS3
     *
     * @param keyId keyId of file in bucket
     * @return File temp file created
     * @throws IOException thrown if temp file cannot be created
     */
    public File getFile(String keyId) throws IOException {
        File tempFile = File.createTempFile(keyId, "tmp");
        getAmazonS3().getObject(new GetObjectRequest(getBucket(), keyId), tempFile);
        return tempFile;
    }

    /**
     * Get file from AmazonS3 bucket and save it to file given
     *
     * @param keyId keyId of file in the bucket
     * @param file  file to save it to
     */
    public void getFile(String keyId, File file) {
        getAmazonS3().getObject(new GetObjectRequest(getBucket(), keyId), file);
    }

    /**
     * Need a cleaner way to transport url and build url
     *
     * @param keyId key id of the Image
     * @return Generated Url of the image
     */
    public String publicUrl(String keyId) {
        return String.format("http://s3-%s.amazonaws.com/%s/%s", getRegion(), getBucket(), keyId);
//        if (MunchConfig.getInstance().isDev()) {
//            return String.format("%s/%s/%s", MunchConfig.getInstance().getString("development.aws.s3.endpoint"), getBucket(), keyId);
//        }
    }
}
