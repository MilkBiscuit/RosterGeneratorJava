package com.cheng.rostergenerator.util;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.cheng.rostergenerator.ui.NameTable;
import com.cheng.rostergenerator.ui.RosterTable;

public class NavigateUtil {

    public static void toNameTable(Component c) {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(c);
        NameTable nameTable = new NameTable();
        nameTable.setOpaque(true);
        frame.getContentPane().removeAll();;
        frame.getContentPane().add(nameTable);
        frame.pack();
        frame.setVisible(true);
    }

    public static void toRosterTable() {
        // var frame = (JFrame) SwingUtilities.getWindowAncestor(c);
        var frame = new JFrame();
        var table = new RosterTable();
        table.setOpaque(true);
        // frame.getContentPane().removeAll();;
        frame.getContentPane().add(table);
        frame.pack();
        frame.setVisible(true);
    }
    
}
