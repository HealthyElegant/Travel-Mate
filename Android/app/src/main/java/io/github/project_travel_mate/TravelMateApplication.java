package io.github.project_travel_mate;

import android.app.Application;

import com.blongho.country_data.World;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import utils.Constants;
import utils.LocaleHelper;

public class TravelMateApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize the World library
        World.init(this);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String lang = pref.getString(Constants.APP_LANGUAGE, "en");
        LocaleHelper.setLocale(this, lang);
    }
}
