package volley.haydens.com.volley.core;

import android.content.Context;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import volley.haydens.com.volley.R;

/**
 * Created by lipy on 9/11/2017.
 */

public class ApiPool {

    private static final String TAG = ApiPool.class.getName();

    private static ApiPool instance;

    private static RequestQueue queue;

    public static ApiPool getInstance(Context context) {
        if (instance == null) {
            instance = new ApiPool();
            queue = Volley.newRequestQueue(context);
        }
        return instance;
    }

    public void call(StringRequest stringRequest){
        if(BuildConfig.DEBUG) {
            try {
                Log.d(TAG, "api call: " + stringRequest.getUrl());
                Log.v(TAG, "header: " + stringRequest.getHeaders().toString());
                Log.v(TAG, "body type: " + stringRequest.getBodyContentType());
                Log.v(TAG, "payload:[ " + new String(stringRequest.getBody(), "UTF-8") + "]");
            } catch (Exception e) {
                Log.v(TAG, "cannot log api call");
            }
        }

        queue.add(stringRequest);
    }

    public static String getApiDomain(Context context) {
        return context.getString(R.string.api_domain);
    }

}
