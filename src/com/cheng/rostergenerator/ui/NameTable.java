package com.cheng.rostergenerator.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;

import com.cheng.rostergenerator.RosterProducer;
import com.cheng.rostergenerator.helper.FileHelper;
import com.cheng.rostergenerator.helper.ResBundleHelper;
import com.cheng.rostergenerator.model.Member;
import com.cheng.rostergenerator.model.NameTableModel;
import com.cheng.rostergenerator.model.constant.UiConstants;
import com.cheng.rostergenerator.util.NavigateUtil;
import com.cheng.rostergenerator.util.SpringUtilities;
import com.cheng.rostergenerator.util.UIUtil;

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
            case "add":
                var newMember = new Member("", false, true);
                members.add(newMember);
                tableModel.refreshTable(members);
                break;
            case "generate":
                var errorKey = RosterProducer.validateErrorMessage();
                if (errorKey == null) {
                    NavigateUtil.toRosterTable();
                } else {
                    var frame = UIUtil.getParentFrame(NameTable.this);
                    UIUtil.showSimpleDialog(frame, errorKey);
                }
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
            case "save":
                var cellEditor = table.getCellEditor();
                if (cellEditor != null) {
                    cellEditor.stopCellEditing();
                }
                FileHelper.writeMemberList(members);
                break;
            }
        }
    };

    public NameTable() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        createTablePanel();

        createSettingsPanel();

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

    private void createSettingsPanel() {
        var speaker4 = new JRadioButton("4");
        var speaker5 = new JRadioButton("5");
        var speakerLabel = new JLabel(ResBundleHelper.getString("speechNum"));
        var speakerNum = Box.createHorizontalBox();
        speakerNum.add(speakerLabel);
        speakerNum.add(speaker4);
        speakerNum.add(speaker5);

        var ttEvaluator1 = new JRadioButton("1");
        var ttEvaluator2 = new JRadioButton("2");
        var ttEvaluatorNum = new JLabel(ResBundleHelper.getString("ttEvaluatorNum"));
        Box ttEvaluator = Box.createHorizontalBox();
        ttEvaluator.add(ttEvaluatorNum);
        ttEvaluator.add(ttEvaluator1);
        ttEvaluator.add(ttEvaluator2);


        String[] labels = {
            ResBundleHelper.getString("speechNum"),
            ResBundleHelper.getString("ttEvaluatorNum"),
            ResBundleHelper.getString("meetingRole.guestHospitality"),
            ResBundleHelper.getString("meetingRole.umAhCounter"),
            ResBundleHelper.getString("meetingRole.listeningPost")
        };

        var settingsPanel = new JPanel(new SpringLayout());
        var speechNumLabel = new JLabel(labels[0], JLabel.TRAILING);
        var fourSpeechRadio = new JRadioButton("4");
        var fiveSpeechRadio = new JRadioButton("5");
        var speechNumGroup = new ButtonGroup();
        speechNumGroup.add(fourSpeechRadio);
        speechNumGroup.add(fiveSpeechRadio);
        settingsPanel.add(speechNumLabel);
        settingsPanel.add(fourSpeechRadio);
        settingsPanel.add(fiveSpeechRadio);
        fourSpeechRadio.setSelected(true);

        var ttNumLabel = new JLabel(labels[1], JLabel.TRAILING);
        var oneTTRadio = new JRadioButton("1");
        var twoTTRadio = new JRadioButton("2");
        var ttNumGroup = new ButtonGroup();
        ttNumGroup.add(oneTTRadio);
        ttNumGroup.add(twoTTRadio);
        settingsPanel.add(ttNumLabel);
        settingsPanel.add(oneTTRadio);
        settingsPanel.add(twoTTRadio);
        twoTTRadio.setSelected(true);

        var numPairs = labels.length;
        for (int i = 2; i < numPairs; i++) {
            var label = new JLabel(labels[i], JLabel.TRAILING);
            settingsPanel.add(label);
            var checkbox = new JCheckBox();
            settingsPanel.add(checkbox);
            checkbox.setSelected(true);
            var box = Box.createHorizontalStrut(100);
            settingsPanel.add(box);
        }

        SpringUtilities.makeCompactGrid(
            settingsPanel, labels.length, 3,
            6, 6, 6, 6
        );
        settingsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(settingsPanel);
    }

}
