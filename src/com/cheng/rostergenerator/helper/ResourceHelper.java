package com.cheng.rostergenerator.helper;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ResourceHelper {
    
    public static ImageIcon imageIcon(String imagePath) {
        try {
            var resource = ResourceHelper.class.getResource(imagePath);
            var image = ImageIO.read(resource);
            return new ImageIcon(image);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return null;
    }

}
