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

        NameTable newContentPane = new NameTable();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        frame.pack();
        frame.setVisible(true);
    }



}
