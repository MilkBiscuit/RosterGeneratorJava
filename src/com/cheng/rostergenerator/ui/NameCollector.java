package com.cheng.rostergenerator.ui;

import com.cheng.rostergenerator.adapter.persistence.FileHelper;
import com.cheng.rostergenerator.domain.model.Member;
import com.cheng.rostergenerator.util.ResBundleUtil;
import com.cheng.rostergenerator.util.UiUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.stream.Collectors;

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
                toNameTable(NameCollector.this);
                break;
            case "done":
                var inputText = textArea.getText();
                var lines = Arrays.asList(inputText.split("\n"));
                var members = lines.stream()
                .filter(line -> !line.isBlank())
                .map(name -> new Member(name, true, true))
                .collect(Collectors.toList());

                FileHelper.writeMemberList(members);

                toNameTable(NameCollector.this);
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

        var label = new JLabel(ResBundleUtil.getString("pleaseInputNames"));
        var c = new GridBagConstraints();
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = UiConstants.smallInsets();
        add(label, c);

        textArea = new JTextArea(TEXT_AREA_ROW, TEXT_AREA_COL);
        var scrollPane = new JScrollPane(textArea);
        scrollPane.setMinimumSize(new Dimension(618, 1000));
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        add(scrollPane, c);

        var doneBtn = new JButton(ResBundleUtil.getString("common.done"));
        doneBtn.setActionCommand("done");
        doneBtn.addActionListener(actionListener);
        c.fill = GridBagConstraints.NONE;
        c.gridy = 2;
        add(doneBtn, c);

        var trialBtn = new JButton(ResBundleUtil.getString("trialWithSampleData"));
        trialBtn.setActionCommand("trial");
        trialBtn.addActionListener(actionListener);
        c.fill = GridBagConstraints.NONE;
        c.gridy = 3;
        add(trialBtn, c);

    }

    private void toNameTable(Component c) {
        var frame = UiUtil.getParentFrame(c);
        var nameTable = new NameTable();
        frame.getContentPane().removeAll();
        frame.setContentPane(nameTable);
        frame.pack();
        frame.setVisible(true);
    }

}
