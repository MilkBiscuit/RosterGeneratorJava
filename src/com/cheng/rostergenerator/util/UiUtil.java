package com.cheng.rostergenerator.util;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class UiUtil {

    public static interface PositiveCB {
        void onClick();
    }

    public static void showSimpleDialog(Component c, String messageResKey) {
        var frame = getParentFrame(c);
        var message = ResBundleUtil.getString(messageResKey);
        JOptionPane.showMessageDialog(frame, message);
    }

    public static void showYesNoDialog(
            Component c,
            String messageResKey,
            String positiveKey,
            String negativeKey,
            PositiveCB cb
    ) {
        var frame = getParentFrame(c);
        var message = ResBundleUtil.getString(messageResKey);
        var positiveText = ResBundleUtil.getString(positiveKey);
        var negativeText = ResBundleUtil.getString(negativeKey);

        Object[] options = {positiveText, negativeText};
        int n = JOptionPane.showOptionDialog(
                frame, message, "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                options, null);
        if (JOptionPane.YES_OPTION == n) {
            cb.onClick();
        }
    }

    public static JFrame getParentFrame(Component c) {
        return (JFrame) SwingUtilities.getWindowAncestor(c);
    }
    
}
