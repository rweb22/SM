package com.tripleseven.android;

import static android.content.pm.PackageManager.GET_META_DATA;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class language extends AppCompatActivity {

    private latobold kannada;
    private latobold gujrati;
    private latobold english;
    private latobold hindi;
    private latobold bangla;
    private latobold marathi;
    private latobold telugu;
    private latobold tamil;
    private LinearLayout block;
    private LinearLayout submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        initViews();


        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocaleManager.setNewLocale(language.this, LocaleManager.LANGUAGE_KEY_ENGLISH);

                String languageToLoad = LocaleManager.LANGUAGE_KEY_ENGLISH; // your language
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                try {
                    ActivityInfo info = getPackageManager().getActivityInfo(getComponentName(), GET_META_DATA);
                    if (info.labelRes != 0) {
                        setTitle(info.labelRes);
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                getSharedPreferences(constant.prefs, MODE_PRIVATE).edit().putString("lang", languageToLoad).apply();



                Intent in = new Intent(language.this, splash.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);

            }
        });

        hindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LocaleManager.setNewLocale(language.this, LocaleManager.LANGUAGE_KEY_HINDI);

                String languageToLoad = LocaleManager.LANGUAGE_KEY_HINDI; // your language
                Locale locale = new Locale(languageToLoad);
                if (isLanguageInList(getLanguages(), locale)) {
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                    try {
                        ActivityInfo info = getPackageManager().getActivityInfo(getComponentName(), GET_META_DATA);
                        if (info.labelRes != 0) {
                            setTitle(info.labelRes);
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }

                    getSharedPreferences(constant.prefs, MODE_PRIVATE).edit().putString("lang", languageToLoad).apply();
                    Intent in = new Intent(language.this, splash.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                } else {
                    final Dialog dialog = new Dialog(language.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.lang);

                    RelativeLayout submit = dialog.findViewById(R.id.submit);

                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent i = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        }
                    });

                    dialog.show();
                }
            }
        });

        marathi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LocaleManager.setNewLocale(language.this, LocaleManager.LANGUAGE_KEY_MARATHI);

                String languageToLoad = LocaleManager.LANGUAGE_KEY_MARATHI; // your language
                Locale locale = new Locale(languageToLoad);
                if (isLanguageInList(getLanguages(), locale)) {
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                    try {
                        ActivityInfo info = getPackageManager().getActivityInfo(getComponentName(), GET_META_DATA);
                        if (info.labelRes != 0) {
                            setTitle(info.labelRes);
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }

                    getSharedPreferences(constant.prefs, MODE_PRIVATE).edit().putString("lang", languageToLoad).apply();
                    Intent in = new Intent(language.this, splash.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                } else {
                    final Dialog dialog = new Dialog(language.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.lang);

                    RelativeLayout submit = dialog.findViewById(R.id.submit);

                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent i = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        }
                    });

                    dialog.show();
                }
            }
        });

        gujrati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LocaleManager.setNewLocale(language.this, LocaleManager.LANGUAGE_KEY_GUJRATI);

                String languageToLoad = LocaleManager.LANGUAGE_KEY_GUJRATI; // your language
                Locale locale = new Locale(languageToLoad);
                if (isLanguageInList(getLanguages(), locale)) {
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                    try {
                        ActivityInfo info = getPackageManager().getActivityInfo(getComponentName(), GET_META_DATA);
                        if (info.labelRes != 0) {
                            setTitle(info.labelRes);
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }

                    getSharedPreferences(constant.prefs, MODE_PRIVATE).edit().putString("lang", languageToLoad).apply();
                    Intent in = new Intent(language.this, splash.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                } else {
                    final Dialog dialog = new Dialog(language.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.lang);

                    RelativeLayout submit = dialog.findViewById(R.id.submit);

                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent i = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        }
                    });

                    dialog.show();
                }
            }
        });

        tamil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LocaleManager.setNewLocale(language.this, LocaleManager.LANGUAGE_KEY_TAMIL);

                String languageToLoad = LocaleManager.LANGUAGE_KEY_TAMIL; // your language
                Locale locale = new Locale(languageToLoad);
                if (isLanguageInList(getLanguages(), locale)) {
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                    try {
                        ActivityInfo info = getPackageManager().getActivityInfo(getComponentName(), GET_META_DATA);
                        if (info.labelRes != 0) {
                            setTitle(info.labelRes);
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }

                    getSharedPreferences(constant.prefs, MODE_PRIVATE).edit().putString("lang", languageToLoad).apply();
                    Intent in = new Intent(language.this, splash.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                } else {
                    final Dialog dialog = new Dialog(language.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.lang);

                    RelativeLayout submit = dialog.findViewById(R.id.submit);

                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent i = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        }
                    });

                    dialog.show();
                }
            }
        });

        bangla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LocaleManager.setNewLocale(language.this, LocaleManager.LANGUAGE_KEY_BANGLA);

                String languageToLoad = LocaleManager.LANGUAGE_KEY_BANGLA; // your language
                Locale locale = new Locale(languageToLoad);
                if (isLanguageInList(getLanguages(), locale)) {
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                    try {
                        ActivityInfo info = getPackageManager().getActivityInfo(getComponentName(), GET_META_DATA);
                        if (info.labelRes != 0) {
                            setTitle(info.labelRes);
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    
                    getSharedPreferences(constant.prefs, MODE_PRIVATE).edit().putString("lang", languageToLoad).apply();
                    Intent in = new Intent(language.this, splash.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                } else {
                    final Dialog dialog = new Dialog(language.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.lang);

                    RelativeLayout submit = dialog.findViewById(R.id.submit);

                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent i = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        }
                    });

                    dialog.show();
                }
            }
        });

        kannada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LocaleManager.setNewLocale(language.this, LocaleManager.LANGUAGE_KEY_KANNADA);

                String languageToLoad = LocaleManager.LANGUAGE_KEY_KANNADA; // your language
                Locale locale = new Locale(languageToLoad);
                if (isLanguageInList(getLanguages(), locale)) {
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                    try {
                        ActivityInfo info = getPackageManager().getActivityInfo(getComponentName(), GET_META_DATA);
                        if (info.labelRes != 0) {
                            setTitle(info.labelRes);
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }

                    getSharedPreferences(constant.prefs, MODE_PRIVATE).edit().putString("lang", languageToLoad).apply();
                    Intent in = new Intent(language.this, splash.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                } else {
                    final Dialog dialog = new Dialog(language.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.lang);

                    RelativeLayout submit = dialog.findViewById(R.id.submit);

                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent i = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        }
                    });

                    dialog.show();
                }
            }
        });

        telugu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LocaleManager.setNewLocale(language.this, LocaleManager.LANGUAGE_KEY_TELUGU);

                String languageToLoad = LocaleManager.LANGUAGE_KEY_TELUGU; // your language
                Locale locale = new Locale(languageToLoad);
                if (isLanguageInList(getLanguages(), locale)) {
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                    try {
                        ActivityInfo info = getPackageManager().getActivityInfo(getComponentName(), GET_META_DATA);
                        if (info.labelRes != 0) {
                            setTitle(info.labelRes);
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }

                    getSharedPreferences(constant.prefs, MODE_PRIVATE).edit().putString("lang", languageToLoad).apply();
                    Intent in = new Intent(language.this, splash.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                } else {
                    final Dialog dialog = new Dialog(language.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.lang);

                    RelativeLayout submit = dialog.findViewById(R.id.submit);

                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent i = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        }
                    });

                    dialog.show();
                }
            }
        });




    }


    public static List<String> getLanguages() {
        String[] locales = Resources.getSystem().getAssets().getLocales();
        List<String> list = new ArrayList<>();

        for (Locale locale : Locale.getAvailableLocales()) {
            if (locale.getLanguage().length() == 2) {
                if (!isLanguageInList(list, locale)) {
                    list.add(locale.getDisplayLanguage());
                }
            }
        }
        Collections.sort(list);
        return list;
    }

    private static boolean isLanguageInList(List<String> list, Locale locale) {
        if (list == null) {
            return false;
        }
        for (String item : list) {
            if (item.equalsIgnoreCase(locale.getDisplayLanguage())) {
                return true;
            }
        }
        return false;
    }


    private void initViews() {
        kannada = findViewById(R.id.kannada);
        gujrati = findViewById(R.id.gujrati);
        english = findViewById(R.id.english);
        hindi = findViewById(R.id.hindi);
        bangla = findViewById(R.id.bangla);
        marathi = findViewById(R.id.marathi);
        telugu = findViewById(R.id.telugu);
        tamil = findViewById(R.id.tamil);
        block = findViewById(R.id.block);
        submit = findViewById(R.id.submit);
    }
}