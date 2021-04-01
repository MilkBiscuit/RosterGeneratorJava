package com.cheng.rostergenerator.util;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.cheng.rostergenerator.helper.ResBundleHelper;

public class UIUtil {

    public static void showSimpleDialog(JFrame frame, String messageResKey) {
        var message = ResBundleHelper.getString(messageResKey);
        JOptionPane.showMessageDialog(frame, message);
    }
    
}
