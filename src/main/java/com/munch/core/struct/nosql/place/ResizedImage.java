package com.munch.core.struct.nosql.place;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

/**
 * Created By: Fuxing Loh
 * Date: 27/9/2016
 * Time: 12:31 AM
 * Project: struct
 */
@DynamoDBTable(tableName = "ResizedImage")
public class ResizedImage {

    private String keySize;

    @DynamoDBHashKey(attributeName = "k")
    public String getKeySize() {
        return keySize;
    }

    @Deprecated
    public void setKeySize(String keySize) {
        this.keySize = keySize;
    }

    /* ** Hypothesis **
     * If populated means exists,
     * if not generate a resize image (or return a error Image)?
     * and maybe send a message to tracker
     */

    @SuppressWarnings("deprecation")
    @DynamoDBIgnore
    public void setKeyAndSize(String key, int width, int height) {
        setKeySize(key + ":" + width + "x" + height);
    }
}
