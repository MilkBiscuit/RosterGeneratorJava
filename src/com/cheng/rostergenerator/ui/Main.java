/**
 * 
 */
package com.cheng.rostergenerator.ui;

import javax.swing.JFrame;
import javax.swing.JTextArea;

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
        frame.setSize(1024, 768);
        frame.setLayout(null);

        JTextArea field = new JTextArea("aaabcdadad");
        field.setBounds(0, 100, 800, 600);
        frame.add(field);

        frame.setVisible(true);
    }

}
