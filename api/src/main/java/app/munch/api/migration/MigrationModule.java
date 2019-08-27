package app.munch.api.migration;

import app.munch.api.ApiServiceModule;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.google.inject.Provides;

import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 19/8/19
 * Time: 5:00 am
 */
public class MigrationModule extends ApiServiceModule {
    @Override
    protected void configure() {
        addService(TempMigrationService.class);
        addService(PlaceMigrationService.class);
    }

    @Singleton
    @Provides
    DynamoDB provideDynamoDB() {
        AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.defaultClient();
        return new DynamoDB(amazonDynamoDB);
    }
}
