package utils;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

public class LocaleHelper {
    public static void setLocale(Context context, String languageCode) {
        if (languageCode == null || languageCode.isEmpty()) {
            return;
        }
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = context.getResources().getConfiguration();
        config.setLocale(locale);
        context.getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());
    }
}
