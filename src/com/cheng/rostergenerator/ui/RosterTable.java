package com.cheng.rostergenerator.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import com.cheng.rostergenerator.domain.RosterProducer;
import com.cheng.rostergenerator.adapter.persistence.FileHelper;
import com.cheng.rostergenerator.ui.helper.ResourceHelper;
import com.cheng.rostergenerator.domain.model.RosterException;
import com.cheng.rostergenerator.ui.model.RosterTableModel;
import com.cheng.rostergenerator.util.UIUtil;


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
                try {
                    final var data = RosterProducer.generateRosterTableData();
                    dataModel.setData(data);
                    dataModel.fireTableDataChanged();
                } catch (RosterException exception) {
                    UIUtil.showSimpleDialog(RosterTable.this, exception.getLocalizedMessage());
                    var frame = UIUtil.getParentFrame(RosterTable.this);
                    frame.setVisible(false);
                }
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

        try {
            final var data = RosterProducer.generateRosterTableData();
            dataModel = new RosterTableModel();
            dataModel.setData(data);

            table = new JTable(dataModel);
            table.setPreferredScrollableViewportSize(new Dimension(1000, 400));
            table.setFillsViewportHeight(true);
    
            var renderer = new RosterCellRenderer(1);
            table.getTableHeader().setDefaultRenderer(renderer);
            table.setGridColor(Color.LIGHT_GRAY);
            table.setDefaultRenderer(Object.class, renderer);
    
            createSidePanel();
        } catch (RosterException e) {
            UIUtil.showSimpleDialog(this, e.getLocalizedMessage());
            var frame = UIUtil.getParentFrame(this);
            frame.setVisible(false);
        }
    }

    private void createSidePanel() {
        final var tableWithButtons = new JPanel();
        final var layout = new GridBagLayout();
        final var title = RosterProducer.generateRosterTableInstructionTitle();
        final var border = UiConstants.bigPaddingBorder();
        tableWithButtons.setBorder(border);
        tableWithButtons.setLayout(layout);
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 0;
        var textArea = new JTextArea(title);
        textArea.setEditable(false);
        textArea.setOpaque(false);
        tableWithButtons.add(textArea, c);

        c.insets = UiConstants.smallInsets();
        c.fill = GridBagConstraints.VERTICAL;
        c.gridx = 0;
        c.gridy = 1;
        c.gridheight = 2;
        var scrollPane = new JScrollPane(table);
        tableWithButtons.add(scrollPane, c);

        var restoreIcon = ResourceHelper.imageIcon("/drawable/ic_refresh.png");
        var restoreBtn = new JButton(restoreIcon);
        restoreBtn.setActionCommand("refresh");
        restoreBtn.addActionListener(buttonActionListener);
        c.gridx = 1;
        c.gridy = 1;
        c.gridheight = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.NONE;
        tableWithButtons.add(restoreBtn, c);

        var saveIcon = ResourceHelper.imageIcon("/drawable/ic_download.png");
        var saveBtn = new JButton(saveIcon);
        saveBtn.setActionCommand("export");
        saveBtn.addActionListener(buttonActionListener);
        c.gridx = 1;
        c.gridy = 2;
        tableWithButtons.add(saveBtn, c);

        add(tableWithButtons);
    }

}
