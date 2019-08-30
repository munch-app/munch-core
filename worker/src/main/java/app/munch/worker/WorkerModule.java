package app.munch.worker;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 30/8/19
 * Time: 12:53 PM
 * Project: munch-core
 */
public class WorkerModule extends AbstractModule {

    @Provides
    @Singleton
    DynamoDB provideDynamoDb() {
        AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClient.builder().build();
        return new DynamoDB(amazonDynamoDB);
    }
}
