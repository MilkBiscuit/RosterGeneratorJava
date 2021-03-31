package com.cheng.rostergenerator.model;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.cheng.rostergenerator.helper.FileHelper;
import com.cheng.rostergenerator.helper.ResBundleHelper;

public class NameTableModel extends AbstractTableModel {

    /**
     *
     */
    private static final long serialVersionUID = -1342387483628153701L;

    private static final boolean DEBUG = true;

    private String[] columnNames = {
        ResBundleHelper.getString("name"),
        ResBundleHelper.getString("experienced"),
        ResBundleHelper.getString("assignASpeech"),
    };
    private List<Member> members = FileHelper.readMemberList();
    private int memberNum = members.size();
    private Object[][] data = null;

    public NameTableModel() {
        data = new Object[memberNum][];
        for (int i = 0; i < memberNum; i++) {
            Member u = members.get(i);
            Object[] memberObject = {
                u.name, u.isExperienced, u.assignSpeech
            };
            data[i] = memberObject;
        }
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    /*
     * JTable uses this method to determine the default renderer/ editor for each
     * cell. If we didn't implement this method, then the last column would contain
     * text ("true"/"false"), rather than a check box.
     */
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public boolean isCellEditable(int row, int col) {
        return true;
    }

    public void setValueAt(Object value, int row, int col) {
        if (DEBUG) {
            System.out.println("Setting value at " + row + "," + col + " to " + value + " (an instance of "
                    + value.getClass() + ")");
        }

        data[row][col] = value;
        fireTableCellUpdated(row, col);

        var updatedMember = members.get(row);
        switch (col) {
        case 0:
            updatedMember.name = (String) value;
            break;
        case 1:
            updatedMember.isExperienced = (boolean) value;
            break;
        case 2:
            updatedMember.assignSpeech = (boolean) value;
            break;
        }

        // if (DEBUG) {
        //     System.out.println("New value of data:");
        //     printDebugData();
        // }
    }

    public List<Member> getMembers() {
        return members;
    }

}
