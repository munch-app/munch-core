package munch.images;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.munch.utils.file.FakeS3Mapper;
import com.munch.utils.file.FileEndpoint;
import com.munch.utils.file.FileMapper;
import com.typesafe.config.Config;

import javax.inject.Named;
import java.util.Collection;
import java.util.Collections;

/**
 * Created By: Fuxing Loh
 * Date: 20/4/2017
 * Time: 7:29 PM
 * Project: munch-core
 */
public class FakeModule extends AbstractModule {

    @Override
    protected void configure() {
        requestInjection(this);
    }

    @Inject
    void trySetupDynamo(AmazonDynamoDB dynamoDB, @Named("tableName") String tableName) throws InterruptedException {
        Collection<KeySchemaElement> keySchema = Collections.singleton(new KeySchemaElement()
                .withAttributeName("k")
                .withKeyType(KeyType.HASH));

        Collection<AttributeDefinition> attributeDefinitions = Collections.singleton(new AttributeDefinition()
                .withAttributeName("k")
                .withAttributeType("S"));

        CreateTableRequest request = new CreateTableRequest()
                .withTableName(tableName)
                .withKeySchema(keySchema)
                .withAttributeDefinitions(attributeDefinitions)
                .withProvisionedThroughput(
                        new ProvisionedThroughput().withReadCapacityUnits(1L).withWriteCapacityUnits(1L));

        TableUtils.createTableIfNotExists(dynamoDB, request);
        TableUtils.waitUntilActive(dynamoDB, tableName);
    }

    @Provides
    @Singleton
    AmazonDynamoDB provideDynamoDBClient(Config config) {
        String endpoint = config.getString("fake.dynamo.endpoint");
        return AmazonDynamoDBClientBuilder.standard()
                .withCredentials(
                        new AWSStaticCredentialsProvider(new BasicAWSCredentials("local", ""))
                ).withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(endpoint, "us-west-2")
                ).build();
    }

    @Provides
    @Singleton
    DynamoDB provideDynamoDB(AmazonDynamoDB client) {
        return new DynamoDB(client);
    }

    @Provides
    @Singleton
    FileMapper provideFileMapper(final Config config) {
        String endpoint = config.getString("fake.s3.endpoint");
        String bucket = config.getString("aws.bucket");
        return new FakeS3Mapper(endpoint, bucket);
    }

    @Provides
    @Singleton
    FileEndpoint provideFileEndpoint(Config config) {
        String bucket = config.getString("aws.bucket");
        if (config.hasPath("fake.s3.public-endpoint")) {
            // Some bug, too lazy to debug
            String endpoint = config.getString("fake.s3.public-endpoint");
            return key -> endpoint + "/fake-s3/" + bucket + "/" + key;
        }
        String endpoint = config.getString("fake.s3.endpoint");
        return key -> endpoint + "/" + bucket + "/" + key;
    }
}
