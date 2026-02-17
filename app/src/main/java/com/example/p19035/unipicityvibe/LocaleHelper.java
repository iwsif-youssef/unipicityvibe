package com.example.p19035.unipicityvibe;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import java.util.Locale;

//Class to help change the language
public class LocaleHelper {

    public static Context setLocale(Context context) {

        SharedPreferences prefs =
                context.getSharedPreferences("settings", Context.MODE_PRIVATE);

        String language = prefs.getString("language", "en");

        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration config = context.getResources().getConfiguration();
        config.setLocale(locale);

        return context.createConfigurationContext(config);
    }
}
