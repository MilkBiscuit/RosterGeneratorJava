package com.cheng.rostergenerator.helper;

import java.util.Locale;
import java.util.ResourceBundle;

public class ResBundleHelper {

    private static ResourceBundle sResource = ResourceBundle.getBundle("string", Locale.US);

    public static String getString(String key) {
        return sResource.getString(key);
    }

    private ResBundleHelper() {
    }

}
