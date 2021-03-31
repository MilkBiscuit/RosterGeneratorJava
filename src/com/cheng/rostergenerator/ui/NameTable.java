/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

package com.cheng.rostergenerator.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
 * NameList.java requires SpringUtilities.java
 */
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;

import com.cheng.rostergenerator.helper.FileHelper;
import com.cheng.rostergenerator.helper.ResBundleHelper;
import com.cheng.rostergenerator.model.NameTableModel;
import com.cheng.rostergenerator.model.UiConstants;

public class NameTable extends JPanel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private JTable table;
    private JButton removeBtn;
    private TableRowSorter<NameTableModel> sorter;
    private NameTableModel tableModel = new NameTableModel();
    private ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            var command = e.getActionCommand();
            switch (command) {
            case "save":
                var memberList = tableModel.getMembers();
                FileHelper.writeMemberList(memberList);

                break;
            case "generate":
                int viewRow = table.getSelectedRow();
                if (viewRow >= 0) {
                    int modelRow = table.convertRowIndexToModel(viewRow);
                    String selectedText = String.format(
                        "Selected Row in view: %d. " + "Selected Row in model: %d.",
                        viewRow, modelRow);
                    System.out.println(selectedText);
                }
                break;
            }
        }
    };

    public NameTable() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        createTablePanel();

        JButton saveChangeBtn = new JButton(ResBundleHelper.getString("saveChanges"));
        saveChangeBtn.setActionCommand("save");
        saveChangeBtn.addActionListener(actionListener);
        saveChangeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(saveChangeBtn);

        JButton generateBtn = new JButton(ResBundleHelper.getString("generateRoster"));
        generateBtn.setActionCommand("generate");
        generateBtn.addActionListener(actionListener);
        generateBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(generateBtn);
    }

    private void createTablePanel() {
        sorter = new TableRowSorter<NameTableModel>(tableModel);
        table = new JTable(tableModel);
        table.setRowSorter(sorter);
        table.setPreferredScrollableViewportSize(new Dimension(500, 300));
        table.setFillsViewportHeight(true);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // When selection changes, provide user with row numbers for both view and model.
        table.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent event) {
                        removeBtn.setEnabled(true);
                    }
                }
        );

        var tableWithButtons = new JPanel();
        var layout = new GridBagLayout();
        var titledBorder = new TitledBorder(ResBundleHelper.getString("memberList"));
        var outsidePaddingBorder = UiConstants.bigPaddingBorder();
        var insidePaddingBorder = UiConstants.smallPaddingBorder();
        var outsideBorder = new CompoundBorder(outsidePaddingBorder, titledBorder);
        var border = new CompoundBorder(outsideBorder, insidePaddingBorder);
        tableWithButtons.setBorder(border);
        tableWithButtons.setLayout(layout);
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.VERTICAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 2;
        JScrollPane scrollPane = new JScrollPane(table);
        tableWithButtons.add(scrollPane, c);

        var addIcon = new ImageIcon("res/drawable/ic_add.png");
        var addBtn = new JButton(addIcon);
        c.gridx = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.NONE;
        tableWithButtons.add(addBtn, c);

        var removeIcon = new ImageIcon("res/drawable/ic_remove.png");
        removeBtn = new JButton(removeIcon);
        c.gridx = 1;
        c.gridy = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.NONE;
        tableWithButtons.add(removeBtn, c);
        removeBtn.setEnabled(false);

        add(tableWithButtons);
    }

}
