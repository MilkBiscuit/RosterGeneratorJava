/**
 * 
 */
package com.cheng.rostergenerator;

import javax.swing.JFrame;

import com.cheng.rostergenerator.adapter.persistence.FileHelper;
import com.cheng.rostergenerator.ui.NameCollector;
import com.cheng.rostergenerator.ui.NameTable;
import com.cheng.rostergenerator.util.ResBundleUtil;

public class Main {

    public static void main(String[] args) {
        System.setProperty("apple.awt.application.name", ResBundleUtil.getString("aboutInfo"));

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        if (FileHelper.memberListFileExists()) {
            NameTable nameTable = new NameTable();
            frame.setContentPane(nameTable);
        } else {
            var nameCollector = new NameCollector();
            frame.setContentPane(nameCollector);
        }

        frame.pack();
        frame.setVisible(true);
    }

}
