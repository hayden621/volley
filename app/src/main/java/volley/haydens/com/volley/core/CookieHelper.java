package volley.haydens.com.volley.core;

import android.content.Context;
import android.util.Log;

import java.util.Map;

/**
 * Class for handling cookies
 * Created by lipy on 9/28/2017.
 */

public class CookieHelper {

    private static final String SET_COOKIE_KEY = "Set-Cookie";
    private static final String COOKIE_KEY = "Cookie";

    private static final String TAG = CookieHelper.class.getName();

    private static CookieHelper instance;


    public static CookieHelper getInstance() {
        if (instance == null) {
            instance = new CookieHelper();
        }
        return instance;
    }
    /**
     * Checks the response headers for session cookie and saves it
     * if it finds it.
     * @param headers Response Headers.
     */
    public final void checkSessionCookie(Context context, Map<String, String> headers) {
        String rawCookies = headers.get(SET_COOKIE_KEY);
        if(rawCookies != null && !rawCookies.equals("")) {
            Log.i(TAG, "store cookie: " + rawCookies);
           SharedPreferenceHelper.putString(context.getApplicationContext(), COOKIE_KEY, rawCookies);
        }
        else {
            Log.i(TAG, "no cookie to store");
        }
    }

    /**
     * Get stored cookies
     *
     */
    public Map<String,String> addSessionCookie(Context context, Map<String,String> params) {
        String sessionId =SharedPreferenceHelper.getString(context.getApplicationContext(), COOKIE_KEY, "");
        if(sessionId != null && !sessionId.equals("")) {
            params.put(CookieHelper.COOKIE_KEY, sessionId);
        }
        else {
            Log.i(TAG, "no cookie to add to header");
        }
        return  params;
    }
}
