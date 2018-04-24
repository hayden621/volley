package volley.haydens.com.volley.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreferenceHelper {

    private static final String TAG = SharedPreferenceHelper.class.getSimpleName();

    public static boolean getBoolean(Context context, String key,
                                     boolean defaultValue) {
        SharedPreferences sp = context.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultValue);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        Log.d(TAG, "putBoolean: [" + key + ":" + value + "]");
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public static String getString(Context context, String key,
                                   String defaultValue) {
        SharedPreferences sp = context.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }

    public static void putString(Context context, String key, String value) {
        Log.d(TAG, "putString: [" + key + ":" + value + "]");
        SharedPreferences sp = context.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences sp = context.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        return sp.getInt(key, defaultValue);
    }

    public static void putInt(Context context, String key, int value) {
        Log.d(TAG, "putInt: [" + key + ":" + value + "]");
        SharedPreferences sp = context.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public static void clear(Context context){
        SharedPreferences sp = context.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        Log.d(TAG, "clear all data");
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    public static void clearData(String key, Context context) {
        SharedPreferences sp = context.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        Log.d(TAG, "clear data: [" + key + "]");
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }
}