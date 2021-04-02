package com.cheng.rostergenerator.helper;

import java.util.prefs.Preferences;

import com.cheng.rostergenerator.model.constant.PrefConstants;

public class PreferenceHelper {

    private static Preferences sPref = Preferences.userRoot();

    public static void save(String key, boolean value) {
        sPref.putBoolean(key, value);
    }

    public static void save(String key, int value) {
        sPref.putInt(key, value);
    }

    public static boolean read(String key, boolean def) {
        return sPref.getBoolean(key, def);
    }

    public static int read(String key, int def) {
        return sPref.getInt(key, def);
    }

    public static boolean reserveForNewMember() {
        return read(PrefConstants.KEY_RESERVE_FOR_NEW, true);
    }

}
