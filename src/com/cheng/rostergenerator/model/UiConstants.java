package com.cheng.rostergenerator.model;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.border.EmptyBorder;

public class UiConstants {

    public static final int APP_WIDTH = 1024;
    public static final int APP_HEIGHT = 768;
    public static final int PADDING_SMALL = 8;
    public static final int PADDING_NORMAL = 16;
    public static final int PADDING_BIG = 24;
    public static final int FRAME_WIDTH_WITH_PADDING_BIG = APP_WIDTH - 2 * PADDING_BIG;
    public static final int FRAME_HEIGHT_WITH_PADDING_BIG = APP_HEIGHT - 2 * PADDING_BIG;

    public static Component verticalBox() {
        return Box.createRigidArea(new Dimension(0, PADDING_NORMAL));
    }

    public static Component horizontalBox() {
        return Box.createRigidArea(new Dimension(PADDING_NORMAL, 0));
    }

    public static EmptyBorder bigPaddingBorder() {
        return new EmptyBorder(PADDING_BIG, PADDING_BIG, PADDING_BIG, PADDING_BIG);
    }

    public static EmptyBorder smallPaddingBorder() {
        return new EmptyBorder(PADDING_SMALL, PADDING_SMALL, PADDING_SMALL, PADDING_SMALL);
    }

}
