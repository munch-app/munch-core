package com.munch.core.essential.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.apache.tika.Tika;

import java.io.File;
import java.io.IOException;

/**
 * Created By: Fuxing Loh
 * Date: 28/9/2016
 * Time: 7:33 PM
 * Project: struct
 */
public class FileMapper {

    public static final String OCTET_STREAM = "application/octet-stream";

    private final AmazonS3 amazonS3;
    private final FileSetting fileSetting;
    private final Tika tika;

    public FileMapper(AmazonS3 amazonS3, FileSetting fileSetting) {
        this.amazonS3 = amazonS3;
        this.fileSetting = fileSetting;
        this.tika = new Tika();
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
        metadata.setContentType(getContentType(keyId, file));
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
     * Get content type from file name
     *
     * @param filename file name
     * @return content type
     */
    public String getContentType(String filename) {
        return tika.detect(filename);
    }

    public String getContentType(File file) throws ContentTypeException {
        try {
            return tika.detect(file);
        } catch (IOException e) {
            throw new ContentTypeException(e);
        }
    }

    public String getContentType(byte[] bytes) throws ContentTypeException {
        return tika.detect(bytes);
    }

    public String getContentType(String filename, File file) throws ContentTypeException {
        String type = getContentType(filename);
        if (type.equalsIgnoreCase(OCTET_STREAM)) {
            type = getContentType(file);
        }
        return type;
    }

    public String getContentType(String filename, byte[] bytes) throws ContentTypeException {
        String type = getContentType(filename);
        if (type.equalsIgnoreCase(OCTET_STREAM)) {
            type = getContentType(bytes);
        }
        return type;
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
