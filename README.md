# Preview
A tutorial on how to use volley and gson with android. Thanks to https://reqres.in/api/ (https://github.com/benhowdle89/reqres) for API demostration

### Pre-request
Create empty android project

## Preparation
1. Add volley library to gradle
```
implementation 'com.android.volley:volley:1.0.0'
```

2. Add gson library to gradle
```
implementation 'com.google.code.gson:gson:2.4'
```

3. Add internet permission to manifest.xml
```
<uses-permission android:name="android.permission.INTERNET" />
```

## Core Class
### ApiPool - Centralized pool for api request queue
### CookieHelper - For handling cookies in request header (not normally required)
### GeneralRequest - Mother class for all request object
### GeneralResponse - Mother class for all response object
### GenericApiHelper - A generic helper class to make api call
### SharedPreferenceHelper - For storing cookies in shared preference

## Sample API call with Core classes
At MainActivity, make API call to https://reqres.in/api/users/2 with response as below, then display returned first and last name
```
{
    "data": {
        "id": 2,
        "first_name": "Janet",
        "last_name": "Weaver",
        "avatar": "https://s3.amazonaws.com/uifaces/faces/twitter/josephstein/128.jpg"
    }
}
```
1. Create SampleApiHelper which extends GenericApiHelper
2. Overload call() method with pre-defined api domain address and api time out
3. Create interface for SampleApiCallbackListener which extends ApiResponseListener
4. Create Data and SampleResponse according to api response in step 1, you can use (http://www.jsonschema2pojo.org/) to generate pojo from json
5. At MainActivity, implements SampleApiCallbackListener and generates the override methods according
6. Use SampleApiHelper to make call to https://reqres.in/api/users/2, which a self defined response code
7. The response code is used to identify which API it responses to(as multiple api call can be made)
8. Handle the response at onApiResponse() and onApiError() 

## Understanding core classes
### GenericApiHelper
#### call()
```
/**
     * method to make REST API request
     * @param context
     * @param method: Request.Method.*
     * @param domain: API domain address
     * @param endpoint: API endpoint
     * @param header: request header
     * @param requestParams: parameters append to url
     * @param requestObject: GeneralRequest object which will be parse to json by gson
     * @param responseObjectClazz: GeneralReponse class which allow the api response to be parse back to java object
     * @param responseCode: User definded integer to identify which API it is responding to
     * @param outTime: API connection time out
     * @param apiResponseListener: API callback listener for listening to success and fail api call
     */
    protected void call(final Context context, int method, final String domain, final String endpoint, final Map<String,String> header, final Map<String,String> requestParams, final GeneralRequest requestObject, final Class<?> responseObjectClazz, final int responseCode, final int outTime, final ApiResponseListener apiResponseListener) {

        String url = decorateUrl(context,domain, endpoint,requestParams);

        Log.d(TAG, "url: " + url);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(method, url,
                new Response.Listener<String>() {
                    @Override
                    //On API success return
                    public void onResponse(String response) {
                        Log.d(TAG, endpoint + "response:" + response);
                        if(apiResponseListener != null) {

                            //try parsing json response to response object
                            try {
                                Object responseObj = gson.fromJson(response,(Class<?>) responseObjectClazz);
                                Log.d(TAG, "responseObj:" + responseObj.toString());

                                //cast response object to GeneralResponse and return to listener
                                if(responseObj instanceof GeneralResponse) {
                                    GeneralResponse generalResponse = (GeneralResponse) responseObj;
                                    apiResponseListener.onApiResponse(responseCode, generalResponse);
                                }
                                else {
                                    String errorMsg = "error parsing response to GeneralResponse";
                                    apiResponseListener.onApiError(responseCode, errorMsg);
                                }
                            }
                            catch(JsonSyntaxException e) {
                                apiResponseListener.onApiError(responseCode, e.toString());
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "error calling:[" + endpoint + "], error:" + error.toString());

                if(apiResponseListener != null) {
                    apiResponseListener.onApiError(responseCode, error.toString());
                }
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                if(requestObject != null) {
                    try {
                        //parse request object to json format and send as request body
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
```
call() method is the core function to make api call. To use this method, you should create a child class of it and exposes the call method.

#### decorateUrl()
```
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
 ```
 This will create the url path by concatenating the domain name endpoint and GET request params. 
 Request parameters should be in Map<Key,Value> format and will be appended at the end of url in key=value format

 
 
 