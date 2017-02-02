package ge.idevelopers.Lifti.app;

import android.app.Application;
import android.preference.PreferenceManager;

/**
 * Created by Gio on 11/15/2016.
 */

public class LiftiApplication extends Application {
    public static LiftiApplication instance;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

    }

    public static LiftiApplication getInstance() {
        return instance;
    }


    public String getSharedString(String key) {
        return PreferenceManager.getDefaultSharedPreferences(instance).getString(key, "");
    }
    public void setSharedString(String key, String value) {
        PreferenceManager.getDefaultSharedPreferences(instance).edit().putString(key, value).commit();
    }

}
