package com.cheng.rostergenerator.helper;

import java.util.prefs.Preferences;

import com.cheng.rostergenerator.adapter.persistence.PrefConstants;

public class PreferenceHelper {

    private static final Preferences sPref = Preferences.userRoot();

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

    public static boolean hasGuestHospitality() {
        return read(PrefConstants.KEY_GUEST_HOSPITALITY, true);
    }

    public static boolean hasUmAhCounter() {
        return read(PrefConstants.KEY_UM_AH_COUNTER, true);
    }

    public static boolean hasListeningPost() {
        return read(PrefConstants.KEY_LISTENING_POST, false);
    }

    public static boolean hasSeenTutorial() {
        return read(PrefConstants.KEY_HAS_SEEN_TUTORIAL, false);
    }

    public static boolean hasFourSpeeches() {
        return read(PrefConstants.KEY_FOUR_SPEECHES, true);
    }

    public static boolean hasTwoTTEvaluator() {
        return read(PrefConstants.KEY_TWO_TT_EVALUATORS, true);
    }

}
