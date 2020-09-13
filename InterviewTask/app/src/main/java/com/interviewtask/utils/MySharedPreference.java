package com.interviewtask.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreference {

    public static void setPreference(Context context, String prefName, String prefKey, Object prefVal) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (prefVal instanceof String)
            editor.putString(prefKey, String.valueOf(prefVal));
        else if (prefVal instanceof Integer)
            editor.putInt(prefKey, (Integer) prefVal);
        else if (prefVal instanceof Float)
            editor.putFloat(prefKey, (Float) prefVal);
        else if (prefVal instanceof Long)
            editor.putLong(prefKey, (Long) prefVal);
        else if (prefVal instanceof Boolean)
            editor.putBoolean(prefKey, (Boolean) prefVal);
        editor.apply();
    }

    public static Object getPreference(Context context, String prefName, String prefKey, Object defaultVal) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        if (defaultVal instanceof String)
            return sharedPreferences.getString(prefKey, String.valueOf(defaultVal));
        else if (defaultVal instanceof Integer)
            return sharedPreferences.getInt(prefKey, (Integer) defaultVal);
        else if (defaultVal instanceof Long)
            return sharedPreferences.getLong(prefKey, (Long) defaultVal);
        else if (defaultVal instanceof Float)
            return sharedPreferences.getFloat(prefKey, (Float) defaultVal);
        else
            return sharedPreferences.getBoolean(prefKey, (Boolean) defaultVal);
    }

    public static void clearPreference(Context context, String prefName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }

//    public static void deletePreference(Context context, String _preferenceName) {
//        context.getSharedPreferences(_preferenceName, 0).edit().clear().apply();
//        File xmlFile = new File(Utility.getPreferencePrefix(context) + _preferenceName + ".xml");
//        if (xmlFile.exists()) xmlFile.delete();
//
//        File backFile = new File(Utility.getPreferencePrefix(context) + _preferenceName + ".bak");
//        if (backFile.exists()) backFile.delete();
//    }
}