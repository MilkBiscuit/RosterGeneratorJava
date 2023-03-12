/**
 * 
 */
package com.cheng.rostergenerator.ui;

import javax.swing.JFrame;

import com.cheng.rostergenerator.adapter.persistence.FileHelper;
import com.cheng.rostergenerator.helper.ResBundleHelper;

/**
 * The entry of the App
 * @author Chandler Cheng
 *
 */
public class Main {

    public static void main(String[] args) {
        System.setProperty("apple.awt.application.name", ResBundleHelper.getString("aboutInfo"));

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
