package com.cheng.rostergenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cheng.rostergenerator.helper.FileHelper;
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
     * 
     * @param numOfMeeting, num of meetings so that all speakers are granted a speaking slot
     * @param numOfClubMembers, how many members does the club have
     * @return how many times we need to copy all members into a huge duplicated list
     */
    public static int numOfAllMembers(int numOfMeeting, int numOfClubMembers) {
        double totalNumOfRoles = numOfMeeting * sNonSpeechRolesPerMeeting;
        double result = totalNumOfRoles / numOfClubMembers;

        return (int) Math.ceil(result);
    }

    /**
     * Validate the member list from the user
     * @return error message key, if list is valid, return null
     */
    public static String validateNumOfMembers() {
        var members = FileHelper.readMemberList();
        if (members.size() < sRolesPerMeeting) {
            return "errorMessage.notEnoghMembers";
        }
        var experienced = members.stream().filter(m -> m.isExperienced).toArray();
        if (experienced.length < 2) {
            return "errorMessage.notEnoghExperienced";
        }

        return null;
    }

    /**
     * 
     * @param speakers, prepared speech speakers this meeting, it should NOT have duplicate values
     * @param totalMembers, n * (whole club member list)
     * @return map of 'meeting role' to 'name'
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
