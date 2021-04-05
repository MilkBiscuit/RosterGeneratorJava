package com.cheng.rostergenerator.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.cheng.rostergenerator.helper.FileHelper;
import com.cheng.rostergenerator.helper.ResBundleHelper;
import com.cheng.rostergenerator.model.Member;
import com.cheng.rostergenerator.model.constant.UiConstants;
import com.cheng.rostergenerator.util.NavigateUtil;

public class NameCollector extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int TEXT_AREA_ROW = 30;
    private static final int TEXT_AREA_COL = 10;
    private JTextArea textArea;

    private ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            var command = e.getActionCommand();
            switch (command) {
            case "trial":
                FileHelper.copySampleData();
                NavigateUtil.toNameTable(NameCollector.this);
                break;
            case "done":
                var inputText = textArea.getText();
                var lines = Arrays.asList(inputText.split("\n"));
                var members = lines.stream()
                .filter(line -> !line.isBlank())
                .map(name -> new Member(name, true, true))
                .collect(Collectors.toList());

                FileHelper.writeMemberList(members);

                NavigateUtil.toNameTable(NameCollector.this);
                break;
            }
        }
    };

    public NameCollector() {
        initLayout();
    }

    private void initLayout() {
        var layout = new GridBagLayout();
        setLayout(layout);
        setBorder(UiConstants.bigPaddingBorder());

        var label = new JLabel(ResBundleHelper.getString("pleaseInputNames"));
        var c = new GridBagConstraints();
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = UiConstants.smallInsets();
        add(label, c);

        textArea = new JTextArea(TEXT_AREA_ROW, TEXT_AREA_COL);
        var scrollPane = new JScrollPane(textArea);
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        add(scrollPane, c);

        var doneBtn = new JButton(ResBundleHelper.getString("common.done"));
        doneBtn.setActionCommand("done");
        doneBtn.addActionListener(actionListener);
        c.fill = GridBagConstraints.NONE;
        c.gridy = 2;
        add(doneBtn, c);

        var trialBtn = new JButton(ResBundleHelper.getString("trialWithSampleData"));
        trialBtn.setActionCommand("trial");
        trialBtn.addActionListener(actionListener);
        c.fill = GridBagConstraints.NONE;
        c.gridy = 3;
        add(trialBtn, c);

    }

}
