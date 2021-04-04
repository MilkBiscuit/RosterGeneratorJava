package com.cheng.rostergenerator.util;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.cheng.rostergenerator.helper.ResBundleHelper;

public class UIUtil {

    public static void showSimpleDialog(Component c, String messageResKey) {
        var frame = getParentFrame(c);
        var message = ResBundleHelper.getString(messageResKey);
        JOptionPane.showMessageDialog(frame, message);
    }

    public static JFrame getParentFrame(Component c) {
        return (JFrame) SwingUtilities.getWindowAncestor(c);
    }
    
}
