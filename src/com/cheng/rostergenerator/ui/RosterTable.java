package com.cheng.rostergenerator.ui;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.cheng.rostergenerator.RosterProducer;
import com.cheng.rostergenerator.helper.ResBundleHelper;
import com.cheng.rostergenerator.model.RosterTableModel;
import com.cheng.rostergenerator.model.constant.UiConstants;

public class RosterTable extends JPanel {
    /**
     *
     */
    private static final long serialVersionUID = 7410794045255870578L;

    public RosterTable() {
        super(new GridLayout(1,0));

        final var dataModel = new RosterTableModel();
        final var data = RosterProducer.generateRosterTableData();
        dataModel.setData(data);
        final var table = new JTable(dataModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 140));
        table.setFillsViewportHeight(true);
        JScrollPane areaScrollPane = new JScrollPane(table);
        final var titleFormat = ResBundleHelper.getString("rosterTable.title");
        final var title = String.format(titleFormat, dataModel.getColumnCount() - 1);
        areaScrollPane.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(
                    UiConstants.paddingBorder(),
                    BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(title),
                        UiConstants.smallPaddingBorder()
                    )
                ),
                areaScrollPane.getBorder()
            )
        );
        add(areaScrollPane);
    }

}
