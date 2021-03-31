/**
 * 
 */
package com.cheng.rostergenerator.ui;

import javax.swing.JFrame;

import com.cheng.rostergenerator.helper.FileHelper;

/**
 * @author Chandler Cheng
 *
 */
public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        if (FileHelper.memberListFileExists()) {
            NameTable nameTable = new NameTable();
            // nameTable.setOpaque(true);
            frame.setContentPane(nameTable);
        } else {
            var nameCollector = new NameCollector();
            frame.setContentPane(nameCollector);
        }

        frame.pack();
        frame.setVisible(true);
    }



}
