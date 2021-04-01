package com.cheng.rostergenerator.ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.cheng.rostergenerator.RosterProducer;
import com.cheng.rostergenerator.helper.FileHelper;
import com.cheng.rostergenerator.model.Member;
import com.cheng.rostergenerator.model.RosterTableModel;
import com.cheng.rostergenerator.model.constant.TextConstants;

public class RosterTable extends JPanel {
    /**
     *
     */
    private static final long serialVersionUID = 7410794045255870578L;

    public RosterTable() {
        super(new GridLayout(1,0));

        var members = FileHelper.readMemberList();
        var allMembers = new ArrayList<Member>();
        var allSpeakers = members.stream().filter(m -> m.assignSpeech).collect(Collectors.toList());
        var speakers = allSpeakers.subList(0, 4);
        // TODO: Remove speakers from allSpeakers
        allMembers.addAll(members);
        allMembers.addAll(members);
        allMembers.addAll(members);
        allMembers.addAll(members);
        allMembers.addAll(members);

        Map<String, String> rosterMap = RosterProducer.generateOneMeeting(speakers, allMembers);
        String[][] data = {
            {TextConstants.CHAIRPERSON, rosterMap.get(TextConstants.CHAIRPERSON)},
            {TextConstants.GENERAL_EVALUATOR, rosterMap.get(TextConstants.GENERAL_EVALUATOR)},
            {TextConstants.SPEAKER_1, rosterMap.get(TextConstants.SPEAKER_1)},
            {TextConstants.SPEAKER_2, rosterMap.get(TextConstants.SPEAKER_2)},
            {TextConstants.SPEAKER_3, rosterMap.get(TextConstants.SPEAKER_3)},
            {TextConstants.SPEAKER_4, rosterMap.get(TextConstants.SPEAKER_4)}
        };
        final var dataModel = new RosterTableModel();
        dataModel.setData(data);
        final var table = new JTable(dataModel);
        // table.setModel(dataModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 140));
        table.setFillsViewportHeight(true);
        JScrollPane areaScrollPane = new JScrollPane(table);
        areaScrollPane.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(
                    // TODO: remove hardcode
                    BorderFactory.createTitledBorder("Roster for 2 meetings"),
                    BorderFactory.createEmptyBorder(5,5,5,5)
                ),
                areaScrollPane.getBorder()
            )
        );
        add(areaScrollPane);
    }

}
