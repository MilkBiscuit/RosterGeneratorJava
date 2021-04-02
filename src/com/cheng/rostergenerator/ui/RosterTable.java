package com.cheng.rostergenerator.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;

import com.cheng.rostergenerator.RosterProducer;
import com.cheng.rostergenerator.helper.FileHelper;
import com.cheng.rostergenerator.helper.ResBundleHelper;
import com.cheng.rostergenerator.model.RosterTableModel;
import com.cheng.rostergenerator.model.constant.UiConstants;


public class RosterTable extends JPanel {
    /**
     *
     */
    private static final long serialVersionUID = 7410794045255870578L;

    private JTable table = null;
    private RosterTableModel dataModel = null;

    private ActionListener buttonActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            var command = e.getActionCommand();
            switch (command) {
            case "refresh":
                final var data = RosterProducer.generateRosterTableData();
                dataModel.setData(data);
                dataModel.fireTableDataChanged();
                break;
            case "export":
                var fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                int returnVal = fileChooser.showSaveDialog(RosterTable.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    var fileToBeSaved = fileChooser.getSelectedFile();
                    if (!fileToBeSaved.getAbsolutePath().endsWith(".csv")) {
                        fileToBeSaved = new File(fileChooser.getSelectedFile() + ".csv");
                    }
                    FileHelper.exportToCSV(dataModel, fileToBeSaved.getAbsolutePath());
                }
                break;
            }
        }
    };

    public RosterTable() {
        super(new GridLayout(1,0));

        final var data = RosterProducer.generateRosterTableData();
        dataModel = new RosterTableModel();
        dataModel.setData(data);
        table = new JTable(dataModel);
        table.setPreferredScrollableViewportSize(new Dimension(1000, 300));
        table.setFillsViewportHeight(true);

        var renderer = new RosterCellRenderer(1);
        table.getTableHeader().setDefaultRenderer(renderer);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setDefaultRenderer(Object.class, renderer);

        createSidePanel();
    }

    private void createSidePanel() {
        final var tableWithButtons = new JPanel();
        final var layout = new GridBagLayout();

        final var titleFormat = ResBundleHelper.getString("rosterTable.title");
        final var numOfMeetings = dataModel.getColumnCount() - 1;
        final var numOfAllSpeakers = RosterProducer.allSpeakers.size();
        final var numOfSpeeches = RosterProducer.numOfSpeechesPerMeeting();
        final var title = String.format(titleFormat, numOfAllSpeakers, numOfSpeeches, numOfMeetings);
        var border = new CompoundBorder(
            new CompoundBorder(
                UiConstants.bigPaddingBorder(),
                new TitledBorder(title)
            ),
            UiConstants.smallPaddingBorder()
        );
        tableWithButtons.setBorder(border);
        tableWithButtons.setLayout(layout);
        GridBagConstraints c = new GridBagConstraints();

        c.insets = UiConstants.smallInsets();
        c.fill = GridBagConstraints.VERTICAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 2;

        var scrollPane = new JScrollPane(table);
        tableWithButtons.add(scrollPane, c);
        var restoreIcon = new ImageIcon("res/drawable/ic_refresh.png");
        var restoreBtn = new JButton(restoreIcon);
        restoreBtn.setActionCommand("refresh");
        restoreBtn.addActionListener(buttonActionListener);
        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.NONE;
        tableWithButtons.add(restoreBtn, c);

        var saveIcon = new ImageIcon("res/drawable/ic_download.png");
        var saveBtn = new JButton(saveIcon);
        saveBtn.setActionCommand("export");
        saveBtn.addActionListener(buttonActionListener);
        c.gridx = 1;
        c.gridy = 1;
        tableWithButtons.add(saveBtn, c);

        add(tableWithButtons);
    }

}
