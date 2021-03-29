package com.cheng.rostergenerator.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import com.cheng.rostergenerator.helper.ResBundleHelper;
import com.cheng.rostergenerator.model.UiConstants;

public class NameCollector extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int TEXT_AREA_ROW = 30;
    private static final int TEXT_AREA_COL = 10;

    public void initLayout() {
        setBorder(new EmptyBorder(UiConstants.PADDING_BIG, UiConstants.PADDING_BIG,
            UiConstants.PADDING_BIG, UiConstants.PADDING_BIG));
        setLayout(new GridBagLayout());

        JLabel label = new JLabel(ResBundleHelper.getString("please_input_names"));
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(label, c);

        add(UiConstants.verticalBox(), c);

        JTextArea textArea = new JTextArea(TEXT_AREA_ROW, TEXT_AREA_COL);
        JScrollPane scrollPane = new JScrollPane(textArea);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;

        add(scrollPane, c);
    }

}
