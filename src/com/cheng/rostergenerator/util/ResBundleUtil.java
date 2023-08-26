package com.cheng.rostergenerator.util;

import com.cheng.rostergenerator.adapter.persistence.FileHelper;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class ResBundleUtil {

    private static final ResourceBundle sResource = ResourceBundle.getBundle("string", Locale.US);

    public static String getString(String key) {
        return sResource.getString(key);
    }

    public static ImageIcon imageIcon(String imagePath) {
        try {
            var resource = ResBundleUtil.class.getResource(imagePath);
            var image = ImageIO.read(resource);
            return new ImageIcon(image);
        } catch (Exception exception) {
            FileHelper.printException(exception);
        }

        return null;
    }

    private ResBundleUtil() {
    }

}
