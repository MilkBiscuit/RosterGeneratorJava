package com.cheng.rostergenerator.adapter.persistence;

import java.util.prefs.Preferences;

public class PreferenceHelper {

    private static final Preferences sPref = Preferences.userRoot();

    public static void save(String key, boolean value) {
        sPref.putBoolean(key, value);
    }

    public static boolean read(String key, boolean def) {
        return sPref.getBoolean(key, def);
    }

    public static boolean reserveForNewMember() {
        return read(PreferenceConstants.KEY_RESERVE_FOR_NEW, true);
    }

    public static boolean hasGuestHospitality() {
        return read(PreferenceConstants.KEY_GUEST_HOSPITALITY, true);
    }

    public static boolean hasUmAhCounter() {
        return read(PreferenceConstants.KEY_UM_AH_COUNTER, true);
    }

    public static boolean hasListeningPost() {
        return read(PreferenceConstants.KEY_LISTENING_POST, false);
    }

    public static boolean hasFourSpeeches() {
        return read(PreferenceConstants.KEY_FOUR_SPEECHES, true);
    }

    public static boolean hasTwoTTEvaluator() {
        return read(PreferenceConstants.KEY_TWO_TT_EVALUATORS, true);
    }

}
