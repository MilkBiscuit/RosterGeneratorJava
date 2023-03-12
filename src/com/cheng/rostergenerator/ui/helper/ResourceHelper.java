package com.cheng.rostergenerator.ui.helper;

import com.cheng.rostergenerator.adapter.persistence.FileHelper;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ResourceHelper {
    
    public static ImageIcon imageIcon(String imagePath) {
        try {
            var resource = ResourceHelper.class.getResource(imagePath);
            var image = ImageIO.read(resource);
            return new ImageIcon(image);
        } catch (Exception exception) {
            FileHelper.printException(exception);
        }

        return null;
    }

}
