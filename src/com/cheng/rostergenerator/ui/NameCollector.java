package com.cheng.rostergenerator.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import com.cheng.rostergenerator.helper.FileHelper;
import com.cheng.rostergenerator.helper.ResBundleHelper;
import com.cheng.rostergenerator.model.Member;
import com.cheng.rostergenerator.model.UiConstants;
import com.cheng.rostergenerator.util.NavigateUtil;

public class NameCollector extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int TEXT_AREA_ROW = 30;
    private static final int TEXT_AREA_COL = 10;
    private JTextArea textArea;

    private ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String inputText = textArea.getText();
            List<String> lines = Arrays.asList(inputText.split("\n"));
            List<Member> members = lines.stream()
            .filter(line -> !line.isBlank())
            .map(name -> new Member(name, true, true))
            .collect(Collectors.toList());

            FileHelper.writeMemberList(members);

            NavigateUtil.toNameTable(NameCollector.this);
        }
    };

    public NameCollector() {
        initLayout();
    }

    private void initLayout() {
        setBorder(new EmptyBorder(UiConstants.PADDING_BIG, UiConstants.PADDING_BIG,
            UiConstants.PADDING_BIG, UiConstants.PADDING_BIG));
        setLayout(new GridBagLayout());

        JLabel label = new JLabel(ResBundleHelper.getString("pleaseInputNames"));
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        add(label, c);

        add(UiConstants.verticalBox(), c);

        // Text area with scroll bars
        textArea = new JTextArea(TEXT_AREA_ROW, TEXT_AREA_COL);
        JScrollPane scrollPane = new JScrollPane(textArea);
        c.fill = GridBagConstraints.BOTH;
        add(scrollPane, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        add(UiConstants.verticalBox(), c);

        // Done button line
        c.gridwidth = 1;
        c.gridy = 4;
        c.weightx = 0.33;
        add(UiConstants.horizontalBox(), c);

        c.gridx = 1;
        JButton button = new JButton(ResBundleHelper.getString("common.done"));
        button.addActionListener(actionListener);
        add(button, c);

        c.gridx = 2;
        add(UiConstants.horizontalBox(), c);
    }

}
