/**
 * 
 */
package com.cheng.rostergenerator.ui;

import javax.swing.JFrame;

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

        NameList newContentPane = new NameList();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        frame.pack();
        frame.setVisible(true);
    }

}
