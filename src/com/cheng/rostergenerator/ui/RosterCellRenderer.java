package com.cheng.rostergenerator.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

public class RosterCellRenderer implements TableCellRenderer {
    private Border border;

    public RosterCellRenderer(int lineWidth) {
        this.border = BorderFactory.createMatteBorder(0, 0, lineWidth, 0, Color.LIGHT_GRAY);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
    int row, int column) {
        var newValue = (String) value;
        var label = new JLabel(newValue);
        label.setBorder(border);
        if (row == -1 || column == 0) {
            // MUST do this for background to show up
            label.setOpaque(true);
            label.setBackground(Color.LIGHT_GRAY);
        }

        return label;
    }

}
