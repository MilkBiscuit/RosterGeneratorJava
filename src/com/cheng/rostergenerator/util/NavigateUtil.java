package com.cheng.rostergenerator.util;

import java.awt.Component;

import javax.swing.JFrame;

import com.cheng.rostergenerator.ui.NameTable;
import com.cheng.rostergenerator.ui.RosterTable;

public class NavigateUtil {

    public static void toNameTable(Component c) {
        var frame = UIUtil.getParentFrame(c);
        var nameTable = new NameTable();
        frame.getContentPane().removeAll();;
        frame.getContentPane().add(nameTable);
        frame.pack();
        frame.setVisible(true);
    }

    public static void toRosterTable() {
        var frame = new JFrame();
        var table = new RosterTable();
        frame.getContentPane().add(table);
        frame.pack();
        frame.setVisible(true);
    }
    
}
