package com.cheng.rostergenerator.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

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
import com.cheng.rostergenerator.model.Member;
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
            var members = tableModel.getMembers();
            switch (command) {
            case "save":
                table.getCellEditor().stopCellEditing();
                FileHelper.writeMemberList(members);
                break;
            case "add":
                var newMember = new Member("", false, true);
                members.add(newMember);
                tableModel.refreshTable(members);
                break;
            case "remove":
                int viewRow = table.getSelectedRow();
                if (viewRow >= 0) {
                    int modelRow = table.convertRowIndexToModel(viewRow);
                    members.remove(modelRow);
                    tableModel.refreshTable(members);
                }
                break;
            case "restore":
                var restoredMembers = FileHelper.readMemberList();
                tableModel.refreshTable(restoredMembers);
                break;
            }
        }
    };

    public NameTable() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        createTablePanel();

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
        c.gridheight = 4;
        JScrollPane scrollPane = new JScrollPane(table);
        tableWithButtons.add(scrollPane, c);

        var addIcon = new ImageIcon("res/drawable/ic_add.png");
        var addBtn = new JButton(addIcon);
        addBtn.setActionCommand("add");
        addBtn.addActionListener(actionListener);
        c.gridheight = 1;
        c.weighty = 1;
        c.gridx = 1;
        c.gridy = 0;
        c.fill = GridBagConstraints.NONE;
        tableWithButtons.add(addBtn, c);

        var removeIcon = new ImageIcon("res/drawable/ic_remove.png");
        removeBtn = new JButton(removeIcon);
        removeBtn.setEnabled(false);
        removeBtn.setActionCommand("remove");
        removeBtn.addActionListener(actionListener);
        c.gridy = 1;
        tableWithButtons.add(removeBtn, c);

        var restoreIcon = new ImageIcon("res/drawable/ic_restore.png");
        var restoreBtn = new JButton(restoreIcon);
        restoreBtn.setActionCommand("restore");
        restoreBtn.addActionListener(actionListener);
        c.gridy = 2;
        tableWithButtons.add(restoreBtn, c);

        var saveIcon = new ImageIcon("res/drawable/ic_save.png");
        var saveBtn = new JButton(saveIcon);
        saveBtn.setActionCommand("save");
        saveBtn.addActionListener(actionListener);
        c.gridy = 3;
        tableWithButtons.add(saveBtn, c);

        add(tableWithButtons);
    }

}
