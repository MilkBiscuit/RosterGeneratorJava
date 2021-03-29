package com.cheng.rostergenerator.helper;

import java.util.Locale;
import java.util.ResourceBundle;

public class ResBundleHelper {

    private static ResBundleHelper sInstance = new ResBundleHelper();
    private static ResourceBundle sResource = ResourceBundle.getBundle("res/string", Locale.US);

    public static ResBundleHelper getInstance() {
        return sInstance;
    }

    public static String getString(String key) {
        return sResource.getString(key);
    }

    private ResBundleHelper() {
    }

}
