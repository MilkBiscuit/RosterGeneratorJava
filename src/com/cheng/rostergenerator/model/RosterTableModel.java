package com.cheng.rostergenerator.model;

import javax.swing.table.AbstractTableModel;

public class RosterTableModel extends AbstractTableModel {

    private static final long serialVersionUID = -2123497955712881177L;
    private String[][] data = {};
    // TODO: remove hardcode

    public boolean isCellEditable(int row, int col) {
        return false;
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        // TODO Auto-generated method stub
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    public void setData(String[][] data) {
        this.data = data;
    }

}
