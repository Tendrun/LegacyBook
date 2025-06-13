package com.example.legacykeep;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

/**
 * Klasa pomocnicza do zarządzania ustawieniami języka aplikacji.
 * Pozwala na ustawienie i zastosowanie wybranego języka w całej aplikacji.
 */
public class LocaleHelper {

    /**
     * Ustawia język aplikacji na podstawie zapisanej preferencji użytkownika.
     * Wywoływana zazwyczaj w metodzie attachBaseContext w celu zastosowania języka przy uruchomieniu aplikacji.
     *
     * @param context kontekst aplikacji lub aktywności
     * @return nowy kontekst z zastosowanym językiem
     */
    public static Context setLocale(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("LegacyKeepPrefs", Context.MODE_PRIVATE);
        String language = prefs.getString("app_language", "en"); // Domyślnie angielski
        return setLocale(context, language);
    }

    /**
     * Ustawia język aplikacji na podany kod języka.
     * Może być wywoływana np. po wyborze języka przez użytkownika.
     *
     * @param context kontekst aplikacji lub aktywności
     * @param language kod języka (np. "en", "pl")
     * @return nowy kontekst z zastosowanym językiem
     */
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