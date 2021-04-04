package com.cheng.rostergenerator.util;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.cheng.rostergenerator.helper.ResBundleHelper;

public class UIUtil {

    public static interface PositiveCB {
        void onClick();
    }

    public static void showSimpleDialog(Component c, String messageResKey) {
        var frame = getParentFrame(c);
        var message = ResBundleHelper.getString(messageResKey);
        JOptionPane.showMessageDialog(frame, message);
    }

    public static void showYesNoDialog(Component c, String messageResKey, String positiveKey, String negativeKey, PositiveCB cb ) {
        var frame = getParentFrame(c);
        var message = ResBundleHelper.getString(messageResKey);
        var positiveText = ResBundleHelper.getString(positiveKey);
        var negativeText = ResBundleHelper.getString(negativeKey);

        Object[] options = {positiveText, negativeText};
        int n = JOptionPane.showOptionDialog(
            frame, message, "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
        if (JOptionPane.YES_OPTION == n) {
            cb.onClick();
        }
    }

    public static JFrame getParentFrame(Component c) {
        return (JFrame) SwingUtilities.getWindowAncestor(c);
    }
    
}
