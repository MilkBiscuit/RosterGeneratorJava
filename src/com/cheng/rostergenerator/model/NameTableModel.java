package com.cheng.rostergenerator.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.cheng.rostergenerator.helper.FileHelper;
import com.cheng.rostergenerator.helper.ResBundleHelper;

public class NameTableModel extends AbstractTableModel {

    private static final long serialVersionUID = -1342387483628153701L;

    private String[] columnNames = {
        ResBundleHelper.getString("name"),
        ResBundleHelper.getString("experienced"),
        ResBundleHelper.getString("assignASpeech"),
    };
    private List<Member> members = null;
    private int memberNum = 0;
    private Object[][] data = null;

    public NameTableModel() {
        var members = FileHelper.readMemberList();
        refreshTable(members);
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
    public Class<?> getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public boolean isCellEditable(int row, int col) {
        return true;
    }

    public void setValueAt(Object value, int row, int col) {
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
    }

    public ArrayList<Member> getMembers() {
        return new ArrayList<Member>(members);
    }

    public void refreshTable(List<Member> newMembers) {
        this.members = newMembers;
        this.memberNum = newMembers.size();
        
        data = new Object[memberNum][];
        for (int i = 0; i < memberNum; i++) {
            Member u = members.get(i);
            Object[] memberObject = {
                u.name, u.isExperienced, u.assignSpeech
            };
            data[i] = memberObject;
        }
        fireTableDataChanged();
    }

}
