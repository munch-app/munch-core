package app.munch.worker.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.typesafe.config.ConfigFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 6/9/19
 * Time: 9:19 am
 */
@Singleton
public final class GoogleSheetService {

    private final String spreadsheetId;
    private final String sheet;

    private final Sheets service;

    @Inject
    GoogleSheetService(@Named("GoogleSheets") GoogleCredential credential) throws GeneralSecurityException, IOException {
        this.spreadsheetId = ConfigFactory.load().getString("affiliate.chope.spreadsheetId");
        this.sheet = ConfigFactory.load().getString("affiliate.chope.sheet");

        NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        this.service = new Sheets.Builder(transport, jsonFactory, credential)
                .setApplicationName("WorkerAffiliateChope")
                .build();
    }

    public Iterator<List<Object>> iterator() throws IOException {
        ValueRange valueRange = service.spreadsheets().values()
                .get(spreadsheetId, sheet)
                .execute();

        List<List<Object>> values = valueRange.getValues();
        return values.iterator();
    }
}
