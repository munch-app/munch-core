package app.munch.worker.google;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.apache.commons.io.IOUtils;

import javax.inject.Named;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 6/9/19
 * Time: 9:21 am
 */
public class GoogleSheetModule extends AbstractModule {

    @Provides
    @Singleton
    @Named("GoogleSheets")
    GoogleCredential provideGoogleCredential(@Named("GoogleSheets") InputStream inputStream) throws IOException {
        return GoogleCredential.fromStream(inputStream)
                .createScoped(Set.of(SheetsScopes.SPREADSHEETS_READONLY));
    }

    @Provides
    @Named("GoogleSheets")
    InputStream provideJsonInputStream() throws IOException {
        AWSSimpleSystemsManagement client = AWSSimpleSystemsManagementClientBuilder.defaultClient();
        GetParameterResult result = client.getParameter(
                new GetParameterRequest().withName("MUNCH_GOOGLESHEETS_SERVICE_ACCOUNT")
        );

        String value = result.getParameter().getValue();
        return IOUtils.toInputStream(value, "utf-8");
    }
}
