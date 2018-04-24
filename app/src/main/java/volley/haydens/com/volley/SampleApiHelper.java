package volley.haydens.com.volley;

import android.content.Context;

import java.util.Map;

import volley.haydens.com.volley.core.GeneralResponse;
import volley.haydens.com.volley.core.GenericApiHelper;

public class SampleApiHelper extends GenericApiHelper {

    public interface SampleApiCallbackListener extends ApiResponseListener {
        @Override
        void onApiError(String errorMsg);

        @Override
        void onApiResponse(int responseCode, GeneralResponse response);
    }


    public static final int DEFAULT_TIME = 10*1000;

    private static SampleApiHelper instance;

    public static SampleApiHelper getInstance() {
        if (instance == null) {
            instance = new SampleApiHelper();
        }
        return instance;
    }

    public void call(final Context context, final int method, final String endpoint, final Map<String,String> requestParams, final Class<?> responseObjectClazz, final int responseCode, final SampleApiCallbackListener apiResponseListener) {
        call(context, method, endpoint, null, requestParams, null, responseObjectClazz, responseCode, DEFAULT_TIME, apiResponseListener);
    }
}
