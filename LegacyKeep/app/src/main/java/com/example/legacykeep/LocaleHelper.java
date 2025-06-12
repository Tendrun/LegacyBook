package com.example.legacykeep;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

public class LocaleHelper {

    // Called from attachBaseContext to apply saved language
    public static Context setLocale(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("LegacyKeepPrefs", Context.MODE_PRIVATE);
        String language = prefs.getString("app_language", "en"); // Default to English
        return setLocale(context, language);
    }

    // Apply a specific language code manually (e.g. from the language picker)
    public static Context setLocale(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration config = new Configuration(resources.getConfiguration());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale);
            return context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            resources.updateConfiguration(config, resources.getDisplayMetrics());
            return context;
        }
    }
}
