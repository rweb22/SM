package com.samratmatka.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.Locale;

public class LocaleManager {

    public static final String LANGUAGE_KEY_ENGLISH = "en";
    public static final String LANGUAGE_KEY_HINDI = "hi";
    public static final String LANGUAGE_KEY_MARATHI = "mr";
    public static final String LANGUAGE_KEY_GUJRATI = "gu";
    public static final String LANGUAGE_KEY_PUNJABI = "pa";
    public static final String LANGUAGE_KEY_TAMIL = "ta";
    public static final String LANGUAGE_KEY_BANGLA = "bn";
    public static final String LANGUAGE_KEY_KANNADA = "kn";
    public static final String LANGUAGE_KEY_TELUGU = "te";
    public static final String LANGUAGE_KEY_MALAYALAM = "ml";


    private static final String LANGUAGE_KEY = "lang";

    /**
     * set current pref locale
     * @param mContext
     * @return
     */
    public static Context setLocale(Context mContext) {
        return updateResources(mContext, getLanguagePref(mContext));
    }

    /**
     * Set new Locale with context
     * @param mContext
     * @param mLocaleKey
     * @return
     */
    public static Context setNewLocale(Context mContext, String mLocaleKey) {
        setLanguagePref(mContext, mLocaleKey);
        return updateResources(mContext, mLocaleKey);
    }

    /**
     * Get saved Locale from SharedPreferences
     * @param mContext current context
     * @return current locale key by default return english locale
     */
    public static String getLanguagePref(Context mContext) {
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return mPreferences.getString(LANGUAGE_KEY, LANGUAGE_KEY_ENGLISH);
    }

    public static String getLanguageName(Context mContext) {
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String lang = mPreferences.getString(LANGUAGE_KEY, LANGUAGE_KEY_ENGLISH);
        switch (lang) {
            case LANGUAGE_KEY_ENGLISH:
                return "English";
            case LANGUAGE_KEY_HINDI:
                return "Hindi";
            case LANGUAGE_KEY_MARATHI:
                return "Marathi";
            case LANGUAGE_KEY_GUJRATI:
                return "Gujarati";
            case LANGUAGE_KEY_PUNJABI:
                return "Punjabi";
            case LANGUAGE_KEY_TAMIL:
                return "Tamil";
            case LANGUAGE_KEY_BANGLA:
                return "Bengali";
            case LANGUAGE_KEY_KANNADA:
                return "Kannada";
            case LANGUAGE_KEY_TELUGU:
                return "Telugu";
            case LANGUAGE_KEY_MALAYALAM:
                return "Malayalam";
            default:
                return "English";
        }
    }

    /**
     *  set pref key
     * @param mContext
     * @param localeKey
     */
    private static void setLanguagePref(Context mContext, String localeKey) {
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mPreferences.edit().putString(LANGUAGE_KEY, localeKey).commit();
        updateResources(mContext, localeKey);
    }

    /**
     * update resource
     * @param context
     * @param language
     * @return
     */
    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        return context;
    }

    /**
     * get current locale
     * @param res
     * @return
     */
    public static Locale getLocale(Resources res) {
        Configuration config = res.getConfiguration();
        return Build.VERSION.SDK_INT >= 24 ? config.getLocales().get(0) : config.locale;
    }
}
