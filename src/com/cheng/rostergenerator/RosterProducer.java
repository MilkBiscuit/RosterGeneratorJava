package com.cheng.rostergenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cheng.rostergenerator.helper.MeetingRoleHelper;
import com.cheng.rostergenerator.model.Member;
import com.cheng.rostergenerator.model.constant.TextConstants;

public class RosterProducer {


    private static double sSpeechesPerMeeting = 3.5;
    private static int sRolesPerMeeting = 18;
    private static int sNonSpeechRolesPerMeeting = 14;

    public static int numOfMeeting(List<Member> speakers) {
        double numOfSpeakers = (double) speakers.size();

        return (int) Math.ceil(numOfSpeakers / sSpeechesPerMeeting);
    }

    /** 
     * Return how many times we need to copy all members into a list so that
     * roster can be generated.
     */
    public static int numOfAllMembers(int numOfMeeting, int numOfClubMembers) {
        double totalNumOfRoles = numOfMeeting * sNonSpeechRolesPerMeeting;
        double result = totalNumOfRoles / numOfClubMembers;

        return (int) Math.ceil(result);
    }

    /**
     * 
     * @param speakers, prepared speech speakers this meeting, it should NOT have duplicate values
     * @param totalMembers, n * (whole club member list)
     * @return
     */
    public static Map<String, String> generateOneMeeting(List<Member> speakers, List<Member> totalMembers) {
        var map = new HashMap<String, String>();
        var namesOfMeeting = new ArrayList<String>(sRolesPerMeeting);
        if (speakers == null || speakers.isEmpty() || speakers.size() > sSpeechesPerMeeting + 1) {
            System.out.println("num of speakers is invalid");

            return map;
        }
        Collections.shuffle(totalMembers);

        speakers.sort(MeetingRoleHelper.inExperiencedFirst());
        for (int i = 0; i < speakers.size(); i++) {
            String speakerName = speakers.get(i).name;
            map.put("Speaker " + (i + 1), speakerName);
            namesOfMeeting.add(speakerName);
        }

        var optChairperson = totalMembers.stream().filter(
            m -> (namesOfMeeting.indexOf(m.name) == -1) && m.isExperienced
        ).findFirst();
        if (optChairperson.isPresent()) {
            var chair = optChairperson.get();
            map.put(TextConstants.CHAIRPERSON, chair.name);
            namesOfMeeting.add(chair.name);
            totalMembers.remove(chair);
        } else {
            throw new RuntimeException("Impossible, no chairperson!");
        }

        var optGeneral = totalMembers.stream().filter(
            m -> (namesOfMeeting.indexOf(m.name) == -1) && m.isExperienced
        ).findFirst();
        if (optGeneral.isPresent()) {
            var general = optGeneral.get();
            map.put(TextConstants.GENERAL_EVALUATOR, general.name);
            namesOfMeeting.add(general.name);
            totalMembers.remove(general);
        } else {
            throw new RuntimeException("Impossible, no general evaluator!");
        }

        var meetingRoles = MeetingRoleHelper.getMeetingRoleForAnyOne();
        for (String role : meetingRoles) {
            var optAnyOne = totalMembers.stream().filter(
                m -> (namesOfMeeting.indexOf(m.name) == -1)
            ).findFirst();
            if (optAnyOne.isPresent()) {
                var anyone = optAnyOne.get();
                map.put(role, anyone.name);
                namesOfMeeting.add(anyone.name);
                totalMembers.remove(anyone);
            }
        }

        return map;
    }
}
