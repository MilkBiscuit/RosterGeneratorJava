package com.cheng.rostergenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.cheng.rostergenerator.helper.FileHelper;
import com.cheng.rostergenerator.helper.MeetingRoleHelper;
import com.cheng.rostergenerator.helper.PreferenceHelper;
import com.cheng.rostergenerator.model.Member;
import com.cheng.rostergenerator.model.constant.PrefConstants;
import com.cheng.rostergenerator.model.constant.TextConstants;

public class RosterProducer {


    private static int sRolesPerMeeting = 18;
    private static int sNonSpeechRolesPerMeeting = 14;
    private static String[] ROLES_PER_MEETING = TextConstants.ROLES_PER_MEETING;

    /**
     * 
     * @param speakers
     * @return num of meetings we need to accommodate all speakers
     */
    public static int numOfMeeting(int numOfSpeakers) {
        var reserveForNew = PreferenceHelper.reserveForNewMember();
        if (reserveForNew) {
            if (numOfSpeakers % 7 == 4) {
                return (numOfSpeakers / 7) * 2 + 1;
            }
        }

        return (int) Math.ceil(numOfSpeakers / 4);
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
    public static String validateErrorMessage() {
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
        if (speakers == null || speakers.isEmpty() || speakers.size() > 5) {
            System.out.println("num of speakers is invalid");

            return map;
        }
        Collections.shuffle(totalMembers);

        speakers.sort(MeetingRoleHelper.inExperiencedFirst());
        // TODO: watch hardcode 4
        var alignIndex = 4 - speakers.size() + 1;
        for (int i = 0; i < speakers.size(); i++) {
            String speakerName = speakers.get(i).name;
            map.put("Speaker " + (i + alignIndex), speakerName);
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

    public static String[][] generateRosterTableData() {
        List<Member> members = FileHelper.readMemberList();
        List<Member> allSpeakers = members.stream().filter(m -> m.assignSpeech).collect(Collectors.toList());
        var numOfMeetings = numOfMeeting(allSpeakers.size());
        var numOfCopiesOfMember = numOfAllMembers(numOfMeetings, members.size());
        var allMembers = new ArrayList<Member>();
        for (int i = 0; i < numOfCopiesOfMember; i++) {
            allMembers.addAll(members);
        }

        String[][] data = new String[sRolesPerMeeting][numOfMeetings+1];
        for (int i = 0; i < ROLES_PER_MEETING.length; i++) {
            data[i][0] = ROLES_PER_MEETING[i];
        }
        for (int j = 1; j <= numOfMeetings; j++) {
            var reserveForNewMember = PreferenceHelper.read(PrefConstants.KEY_RESERVE_FOR_NEW, true);
            var numOfSpeaker = (j % 2 == 0 && reserveForNewMember) ? 3 : 4;
            numOfSpeaker = Math.min(numOfSpeaker, allSpeakers.size());
            var speakers = allSpeakers.subList(0, numOfSpeaker);
            Map<String, String> rosterMap = RosterProducer.generateOneMeeting(speakers, allMembers);
            for (int i = 0; i < ROLES_PER_MEETING.length; i++) {
                data[i][j] = rosterMap.get(ROLES_PER_MEETING[i]);
            }
            allSpeakers.removeAll(speakers);
        }

        return data;
    }
}
