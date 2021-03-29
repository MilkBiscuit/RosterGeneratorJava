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

        NameCollector nameCollector = new NameCollector();
        nameCollector.initLayout();
        frame.add(nameCollector);

        frame.pack();
        frame.setVisible(true);
    }

}
