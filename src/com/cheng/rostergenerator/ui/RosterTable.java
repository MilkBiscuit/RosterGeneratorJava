package com.cheng.rostergenerator.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class RosterTable extends JPanel {
    /**
     *
     */
    private static final long serialVersionUID = 7410794045255870578L;

    public RosterTable() {
        super(new GridLayout(1,0));

        // String[] columnNames = {"First Name",
        //                         "Last Name",
        //                         "Sport",
        //                         "# of Years",
        //                         "Vegetarian"};

        // Object[][] data = {
	    // {"Kathy", "Smith",
	    //  "Snowboarding", new Integer(5), new Boolean(false)},
	    // {"John", "Doe",
	    //  "Rowing", new Integer(3), new Boolean(true)},
	    // {"Sue", "Black",
	    //  "Knitting", new Integer(2), new Boolean(false)},
	    // {"Jane", "White",
	    //  "Speed reading", new Integer(20), new Boolean(true)},
	    // {"Joe", "Brown",
	    //  "Pool", new Integer(10), new Boolean(false)}
        // };

        // final JTable table = new JTable(data, columnNames);
        // table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        // table.setFillsViewportHeight(true);

        // //Create the scroll pane and add the table to it.
        // JScrollPane scrollPane = new JScrollPane(table);

        // //Add the scroll pane to this panel.
        // add(scrollPane);



        

        JTextArea textArea = new JTextArea(
                "This is an editable JTextArea. " +
                "A text area is a \"plain\" text component, " +
                "which means that although it can display text " +
                "in any font, all of the text is in the same font."
        );
        // textArea.setLineWrap(true);
        // textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        JScrollPane areaScrollPane = new JScrollPane(textArea);
        areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        areaScrollPane.setPreferredSize(new Dimension(250, 250));
        areaScrollPane.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder("Plain Text"),
                                BorderFactory.createEmptyBorder(5,5,5,5)),
                areaScrollPane.getBorder()
            )
        );

        add(areaScrollPane);
    }

}
