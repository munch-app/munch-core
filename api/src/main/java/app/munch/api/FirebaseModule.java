package app.munch.api;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.inject.AbstractModule;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import munch.restful.server.firebase.FirebaseAuthenticationModule;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

/**
 * Created by: Fuxing
 * Date: 9/9/19
 * Time: 2:54 pm
 */
public final class FirebaseModule extends AbstractModule {

    @Override
    protected void configure() {
        install(getAuthenticationModule());
    }

    /**
     * @return Firebase Authentication Module
     */
    private static FirebaseAuthenticationModule getAuthenticationModule() {
        Config config = ConfigFactory.load().getConfig("services.firebase");
        final String projectId = config.getString("projectId");

        try {
            if (config.hasPath("base64")) {
                byte[] bytes = Base64.getDecoder().decode(config.getString("base64"));
                InputStream inputStream = new ByteArrayInputStream(bytes);
                GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream);
                return new FirebaseAuthenticationModule(projectId, credentials);
            }

            String ssmKey = config.getString("ssmKey");
            AWSSimpleSystemsManagement client = AWSSimpleSystemsManagementClientBuilder.defaultClient();
            GetParameterResult result = client.getParameter(
                    new GetParameterRequest().withName(ssmKey).withWithDecryption(true)
            );

            String value = result.getParameter().getValue();
            InputStream inputStream = IOUtils.toInputStream(value, "utf-8");
            GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream);
            return new FirebaseAuthenticationModule(projectId, credentials);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
