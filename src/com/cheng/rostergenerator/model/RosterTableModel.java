package com.cheng.rostergenerator.model;

import javax.swing.table.AbstractTableModel;

import com.cheng.rostergenerator.helper.ResBundleHelper;

public class RosterTableModel extends AbstractTableModel {

    private static final long serialVersionUID = -2123497955712881177L;
    private static final String dateString = ResBundleHelper.getString("datePlacehoder");
    private static String[] COLUMN_NAMES = new String[] {
        ResBundleHelper.getString("meetingRoles"),
        dateString, dateString, dateString, dateString, dateString, dateString
    };
    private String[][] data = {};


    public boolean isCellEditable(int row, int col) {
        return false;
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        if (data == null || data.length == 0 || data[0] == null || data[0].length == 0) {
            return 0;
        }

        return data[0].length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    public void setData(String[][] data) {
        this.data = data;
    }

}
