package patrimoine.wcs.fr.toulousemonuments;

import android.util.Log;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.json.gson.GsonFactory;
import com.octo.android.robospice.GsonGoogleHttpClientSpiceService;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

import patrimoine.wcs.fr.toulousemonuments.models.MonumentModel;

/**
 * Created by apprenti on 08/06/17.
 */

public class MonumentRequest extends GoogleHttpClientSpiceRequest<MonumentModel> {

    public final static String TAG = "MonumentRequest";
    public final static String BASE_URL = "https://data.haute-garonne.fr/api/records/1.0/search/?dataset=sites-touristiques&facet=icone&facet=commune&refine.commune=TOULOUSE&refine.icone=MONUMENT+HISTORIQUE&rows=20";

    public MonumentRequest() {
        super(MonumentModel.class);
    }

    @Override
    public MonumentModel loadDataFromNetwork() throws Exception {
        GenericUrl url = new GenericUrl(this.BASE_URL);

        MonumentModel monumentModel = getHttpRequestFactory()
                .buildGetRequest(url).setParser(new GsonFactory().createJsonObjectParser())
                .execute()
                .parseAs(getResultType());
        Log.d(TAG, "monuments loaded");
        return monumentModel;
    }
}
