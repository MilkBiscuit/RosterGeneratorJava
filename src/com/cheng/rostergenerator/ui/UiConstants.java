package com.cheng.rostergenerator.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UiConstants {
    public static final int PADDING_SMALL = 8;
    public static final int PADDING_NORMAL = 16;
    public static final int PADDING_BIG = 24;

    public static Component horizontalBox() {
        return Box.createRigidArea(new Dimension(PADDING_NORMAL, 0));
    }

    public static EmptyBorder bigPaddingBorder() {
        return new EmptyBorder(PADDING_BIG, PADDING_BIG, PADDING_BIG, PADDING_BIG);
    }

    public static EmptyBorder smallPaddingBorder() {
        return new EmptyBorder(PADDING_SMALL, PADDING_SMALL, PADDING_SMALL, PADDING_SMALL);
    }

    public static Insets smallInsets() {
        return new Insets(PADDING_SMALL, PADDING_SMALL, PADDING_SMALL, PADDING_SMALL);
    }

}
