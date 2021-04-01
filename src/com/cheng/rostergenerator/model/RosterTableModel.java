package com.cheng.rostergenerator.model;

import javax.swing.table.AbstractTableModel;

public class RosterTableModel extends AbstractTableModel {

    private static final long serialVersionUID = -2123497955712881177L;
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

    public void setData(String[][] data) {
        this.data = data;
    }

}
