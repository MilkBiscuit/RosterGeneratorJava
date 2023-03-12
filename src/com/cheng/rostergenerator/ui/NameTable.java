package com.cheng.rostergenerator.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
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

import com.cheng.rostergenerator.domain.RosterProducer;
import com.cheng.rostergenerator.adapter.persistence.FileHelper;
import com.cheng.rostergenerator.helper.PreferenceHelper;
import com.cheng.rostergenerator.helper.ResBundleHelper;
import com.cheng.rostergenerator.ui.helper.ResourceHelper;
import com.cheng.rostergenerator.domain.model.Member;
import com.cheng.rostergenerator.ui.model.NameTableModel;
import com.cheng.rostergenerator.adapter.persistence.PrefConstants;
import com.cheng.rostergenerator.util.NavigationUtil;
import com.cheng.rostergenerator.util.SpringUtil;
import com.cheng.rostergenerator.util.UIUtil;

public class NameTable extends JPanel {
    private static final long serialVersionUID = 1L;

    private JTable table;
    private JButton removeBtn;
    private TableRowSorter<NameTableModel> sorter;
    private NameTableModel tableModel = new NameTableModel();

    private static String[] SETTING_LABELS = {
        ResBundleHelper.getString("speechNum"),
        ResBundleHelper.getString("ttEvaluatorNum"),
        ResBundleHelper.getString("settings.reserveForNewMember"),
        ResBundleHelper.getString("meetingRole.guestHospitality"),
        ResBundleHelper.getString("meetingRole.umAhCounter"),
        ResBundleHelper.getString("meetingRole.listeningPost")
    };
    private List<Object> settingObjects = new ArrayList<Object>(SETTING_LABELS.length);

    private ActionListener buttonActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            var command = e.getActionCommand();
            var members = tableModel.getMembers();
            switch (command) {
            case "1":
                PreferenceHelper.save(PrefConstants.KEY_TWO_TT_EVALUATORS, false);
                break;
            case "2":
                PreferenceHelper.save(PrefConstants.KEY_TWO_TT_EVALUATORS, true);
                break;
            case "4":
                PreferenceHelper.save(PrefConstants.KEY_FOUR_SPEECHES, true);
                break;
            case "5":
                PreferenceHelper.save(PrefConstants.KEY_FOUR_SPEECHES, false);
                break;
            case "add":
                var newMember = new Member("", false, true);
                members.add(newMember);
                tableModel.refreshTable(members);
                table.editCellAt(members.size() - 1, 0);
                table.getEditorComponent().requestFocus();
                var editingRect = table.getCellRect(members.size() - 1, 0, true);
                table.scrollRectToVisible(editingRect);

                break;
            case "generate":
                // Stop editing and trigger setValueAt to save data
                if (table.isEditing()) {
                    var cellEditor = table.getCellEditor();
                    if (cellEditor != null) {
                        cellEditor.stopCellEditing();
                    }
                }

                // Start generating
                var errorKey = RosterProducer.validateErrorMessage();
                if (errorKey == null) {
                    NavigationUtil.toRosterTable();
                } else {
                    UIUtil.showSimpleDialog(NameTable.this, errorKey);
                }
                break;
            case "remove":
                int viewRow = table.getSelectedRow();
                if (viewRow >= 0) {
                    int modelRow = table.convertRowIndexToModel(viewRow);
                    members.remove(modelRow);
                    tableModel.refreshTable(members);
                    FileHelper.writeMemberList(members);
                }
                break;
            case "exit":
                UIUtil.showYesNoDialog(NameTable.this, "dialog.removeAll", "common.yesImSure", "common.cancel", () -> {
                    FileHelper.deleteMemberList();
                    NavigationUtil.toNameCollector(NameTable.this);
                });
                break;
            }
        }
    };

    private ItemListener itemListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            var selected = e.getStateChange() == ItemEvent.SELECTED;
            var source = e.getItemSelectable();
            var index = settingObjects.indexOf(source);
            if (index > 1 && index < 6) {
                PreferenceHelper.save(PrefConstants.SETTING_KEYS[index], selected);
            }
        }
    };

    public NameTable() {
        super();
        BoxLayout box = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(box);
        setBorder(UiConstants.bigPaddingBorder());
        createTablePanel();
        createSidePanel();
        createSettingsPanel();

        JButton generateBtn = new JButton(ResBundleHelper.getString("generateRoster"));
        generateBtn.setActionCommand("generate");
        generateBtn.addActionListener(buttonActionListener);
        generateBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(generateBtn);
    }

    private void createTablePanel() {
        sorter = new TableRowSorter<NameTableModel>(tableModel);
        table = new JTable(tableModel);
        table.setRowSorter(sorter);
        table.setPreferredScrollableViewportSize(new Dimension(500, 300));
        table.setFillsViewportHeight(true);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // When selection changes, provide user with row numbers for both view and model.
        table.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent event) {
                        removeBtn.setEnabled(true);
                    }
                }
        );
    }

    private void createSidePanel() {
        var tableWithButtons = new JPanel();
        var layout = new GridBagLayout();
        var border = new CompoundBorder(
            new TitledBorder(ResBundleHelper.getString("memberList")),
            UiConstants.smallPaddingBorder()
        );
        tableWithButtons.setBorder(border);
        tableWithButtons.setLayout(layout);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = UiConstants.smallInsets();

        c.fill = GridBagConstraints.VERTICAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 4;
        var scrollPane = new JScrollPane(table);
        tableWithButtons.add(scrollPane, c);

        var addIcon = ResourceHelper.imageIcon("/drawable/ic_add.png");
        var addBtn = new JButton(addIcon);
        addBtn.setActionCommand("add");
        addBtn.addActionListener(buttonActionListener);
        c.gridheight = 1;
        c.weighty = 1;
        c.gridx = 1;
        c.gridy = 0;
        c.fill = GridBagConstraints.NONE;
        tableWithButtons.add(addBtn, c);

        var removeIcon = ResourceHelper.imageIcon("/drawable/ic_remove.png");
        removeBtn = new JButton(removeIcon);
        removeBtn.setEnabled(false);
        removeBtn.setActionCommand("remove");
        removeBtn.addActionListener(buttonActionListener);
        c.gridy = 1;
        tableWithButtons.add(removeBtn, c);

        var exitIcon = ResourceHelper.imageIcon("/drawable/ic_exit.png");
        var exitBtn = new JButton(exitIcon);
        exitBtn.setActionCommand("exit");
        exitBtn.addActionListener(buttonActionListener);
        c.gridy = 2;
        tableWithButtons.add(exitBtn, c);

        add(tableWithButtons);
    }

    private void createSettingsPanel() {
        var settingsPanel = new JPanel(new SpringLayout());
        var speechNumLabel = new JLabel(SETTING_LABELS[0], JLabel.TRAILING);
        var fourSpeechRadio = new JRadioButton("4");
        var fiveSpeechRadio = new JRadioButton("5");
        var speechNumGroup = new ButtonGroup();
        speechNumGroup.add(fourSpeechRadio);
        speechNumGroup.add(fiveSpeechRadio);
        settingsPanel.add(speechNumLabel);
        settingsPanel.add(fourSpeechRadio);
        settingsPanel.add(fiveSpeechRadio);
        settingObjects.add(speechNumGroup);
        fourSpeechRadio.addActionListener(buttonActionListener);
        fiveSpeechRadio.addActionListener(buttonActionListener);
        if (PreferenceHelper.hasFourSpeeches()) {
            fourSpeechRadio.setSelected(true);
        } else {
            fiveSpeechRadio.setSelected(true);
        }

        var ttNumLabel = new JLabel(SETTING_LABELS[1], JLabel.TRAILING);
        var oneTTRadio = new JRadioButton("1");
        var twoTTRadio = new JRadioButton("2");
        var ttNumGroup = new ButtonGroup();
        ttNumGroup.add(oneTTRadio);
        ttNumGroup.add(twoTTRadio);
        settingsPanel.add(ttNumLabel);
        settingsPanel.add(oneTTRadio);
        settingsPanel.add(twoTTRadio);
        twoTTRadio.setSelected(true);
        settingObjects.add(ttNumGroup);
        oneTTRadio.addActionListener(buttonActionListener);
        twoTTRadio.addActionListener(buttonActionListener);
        if (PreferenceHelper.hasTwoTTEvaluator()) {
            twoTTRadio.setSelected(true);
        } else {
            oneTTRadio.setSelected(true);
        }

        var numPairs = SETTING_LABELS.length;
        for (int i = 2; i < numPairs; i++) {
            var label = new JLabel(SETTING_LABELS[i], JLabel.TRAILING);
            settingsPanel.add(label);
            var box = UiConstants.horizontalBox();
            settingsPanel.add(box);

            var checkbox = new JCheckBox();
            var isChecked = PreferenceHelper.read(PrefConstants.SETTING_KEYS[i], true);
            checkbox.setSelected(isChecked);
            checkbox.addItemListener(itemListener);
            settingsPanel.add(checkbox);

            settingObjects.add(i, checkbox);
        }

        SpringUtil.makeCompactGrid(
            settingsPanel, SETTING_LABELS.length, 3,
            UiConstants.PADDING_SMALL, UiConstants.PADDING_SMALL,
            UiConstants.PADDING_SMALL, UiConstants.PADDING_SMALL
        );
        settingsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(settingsPanel);
    }

}
