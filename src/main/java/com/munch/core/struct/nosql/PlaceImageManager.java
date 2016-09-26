package com.munch.core.struct.nosql;

/**
 * Created By: Fuxing Loh
 * Date: 26/9/2016
 * Time: 11:06 PM
 * Project: struct
 */

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.munch.core.struct.nosql.place.PlaceLocationImage;
import com.munch.core.struct.nosql.place.PlaceS3Setting;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.File;
import java.util.List;

/**
 * Move to S3 managed lifecycle when ready,
 * this is too dirty.
 */
public class PlaceImageManager {

    private static final PlaceS3Setting setting = new PlaceS3Setting();
    private final AmazonS3 amazonS3;
    private final DynamoDBMapper dynamoMapper;
    private final String bucket;

    public PlaceImageManager(AmazonS3 amazonS3, DynamoDBMapper dynamoMapper, String bucket) {
        this.amazonS3 = amazonS3;
        this.dynamoMapper = dynamoMapper;
        this.bucket = bucket;
    }

    public void upload(PlaceLocationImage image, File file, String fileName, String sourceUrl) {
        // Generate KeyId for S3
        String keyId = RandomStringUtils.randomAlphanumeric(40) + "." + FilenameUtils.getExtension(fileName);
        image.setKeyId(keyId);

        // Assert that it's build with builder
        assert image.getSortTypeTimeUserId() != null;
        // The rest is optional, user name & description should be on demand viewing. so is finn type

        // Add metadata for S3
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.addUserMetadata("originalFileName", fileName);
        metadata.addUserMetadata("sourceUrl", sourceUrl);

        // Put object to aws s3
        amazonS3.putObject(new PutObjectRequest(bucket, keyId, file)
                .withMetadata(metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        dynamoMapper.save(image);
        // TODO move this to lambda to prevent transaction error which will cause aws s3 storage to leak data
    }

    /**
     * TODO pagination in future
     *
     * @param placeLocationId place location id of all images
     * @return List of PlaceLocationImage belonging to placeLocationId
     */
    public List<PlaceLocationImage> list(String placeLocationId) {
        PlaceLocationImage image = new PlaceLocationImage();
        image.setPlaceLocationId(placeLocationId);
        return dynamoMapper.query(PlaceLocationImage.class, new DynamoDBQueryExpression<PlaceLocationImage>()
                .withHashKeyValues(image)
                .withConsistentRead(false));
    }

    public String getUrl(PlaceLocationImage image) {
        return getUrl(image.getKeyId());
    }

    public String getUrl(String keyId) {
        return String.format("http://s3-%s.amazonaws.com/%s/%s", setting.getRegion(), bucket, keyId);
    }

}
