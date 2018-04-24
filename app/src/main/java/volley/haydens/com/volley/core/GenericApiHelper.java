package volley.haydens.com.volley.core;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.util.HashMap;
import java.util.Map;

/**
 * Generic ApiHelper to handle api calls
 * Created by lipy on 9/18/2017.
 */

public class GenericApiHelper {

    protected GsonBuilder gsonBuilder = new GsonBuilder();
    protected Gson gson;

    public interface ApiResponseListener {
        void onApiResponse(int responseCode, GeneralResponse response);
        void onApiError(String errorMsg);
    }

    private static final String TAG = GenericApiHelper.class.getName();

    public GenericApiHelper() {
        gson = gsonBuilder.create();
    }

    public Gson getGson() {
        return gson;
    }

    private String decorateUrl(Context context, final String domain, final String endpoint, final Map<String,String> requestParams) {
        String url = domain + endpoint;

        if(requestParams != null) {
            url = url + "?";

            for(Map.Entry<String, String> entry : requestParams.entrySet()) {
                url = url + entry.getKey() + "=" + entry.getValue() + "&";
            }
        }

        return url;
    }

    protected void call(final Context context, int method, final String domain, final String endpoint, final Map<String,String> header, final Map<String,String> requestParams, final GeneralRequest requestObject, final Class<?> responseObjectClazz, final int responseCode, final int outTime, final ApiResponseListener apiResponseListener) {

        String url = decorateUrl(context,domain, endpoint,requestParams);

        Log.d(TAG, "url: " + url);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(method, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, endpoint + "response:" + response);
                        if(apiResponseListener != null) {
                            try {
                                Object responseObj = gson.fromJson(response,(Class<?>) responseObjectClazz);
                                Log.d(TAG, "responseObj:" + responseObj.toString());
                                if(responseObj instanceof GeneralResponse) {
                                    GeneralResponse generalResponse = (GeneralResponse) responseObj;
                                    apiResponseListener.onApiResponse(responseCode, generalResponse);
                                }
                                else {
                                    String errorMsg = "error parsing response to GeneralResponse";
                                    apiResponseListener.onApiError(errorMsg);
                                }
                            }
                            catch(JsonSyntaxException e) {
                                apiResponseListener.onApiError(e.toString());
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "error calling:[" + endpoint + "], error:" + error.toString());

                if(apiResponseListener != null) {
                    apiResponseListener.onApiError(error.toString());
                }
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                if(requestObject != null) {
                    try {
                        return gson.toJson(requestObject).getBytes();
                    } catch (Exception e) {
                        Log.e(TAG, "error parsing request body to json");

                    }
                }
                return super.getBody();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Content-Type","application/json; charset=utf-8");

                if(header != null) {
                    for(Map.Entry<String, String> entry : header.entrySet()) {
                        params.put(entry.getKey(), entry.getValue());
                    }
                }

               params = CookieHelper.getInstance().addSessionCookie(context, params);

                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                // since we don't know which of the two underlying network vehicles
                // will Volley use, we have to handle and store session cookies manually
                CookieHelper.getInstance().checkSessionCookie(context, response.headers);
                return super.parseNetworkResponse(response);
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                outTime,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES
        ));

        ApiPool.getInstance(context).call(stringRequest);
    }
}
