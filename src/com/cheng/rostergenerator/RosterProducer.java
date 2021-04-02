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
import com.cheng.rostergenerator.model.RosterException;
import com.cheng.rostergenerator.model.constant.TextConstants;

public class RosterProducer {

    public static int sNumOfAllSpeakers = 0;
    private static int sRolesPerMeeting = 0;
    public static List<Member> clubMembers = FileHelper.readMemberList();

    /**
     * 
     * @param speakers
     * @return num of meetings we need to accommodate all speakers
     */
    public static int numOfMeeting(int numOfSpeakers) {
        if (numOfSpeakers == 0) {
            return 0;
        }
        var reserveForNew = PreferenceHelper.reserveForNewMember();
        var fourSpeeches = PreferenceHelper.hasFourSpeeches();
        if (reserveForNew) {
            if (fourSpeeches) {
                var remainder = numOfSpeakers % 7;
                if (remainder == 0) {
                    return (numOfSpeakers / 7) * 2;
                } else if (remainder <= 4) {
                    return (numOfSpeakers / 7) * 2 + 1;
                } else {
                    return (numOfSpeakers / 7) * 2 + 2;
                }
            } else {
                var remainder = numOfSpeakers % 9;
                if (remainder == 0) {
                    return (numOfSpeakers / 9) * 2;
                } else if (remainder <= 5) {
                    return (numOfSpeakers / 9) * 2 + 1;
                } else {
                    return (numOfSpeakers / 9) * 2 + 2;
                }
            }
        }

        return (int) Math.ceil(numOfSpeakers / (fourSpeeches ? 4.0f : 5.0f));
    }

    public static float uiNumOfSpeechesPerMeeting() {
        var reserveForNew = PreferenceHelper.reserveForNewMember();
        var hasFourSpeeches = PreferenceHelper.hasFourSpeeches();
        float retValue;
        if (reserveForNew && hasFourSpeeches) {
            retValue = 3.5f;
        } else if (reserveForNew && !hasFourSpeeches) {
            retValue = 4.5f;
        } else if (!reserveForNew && hasFourSpeeches) {
            retValue = 4.0f;
        } else {
            retValue = 5.0f;
        }

        return retValue;
    }

    public static int numOfSpeechesPerMeeting() {
        var uiNum = uiNumOfSpeechesPerMeeting();
        var actualNum = (int) Math.ceil(uiNum);

        return actualNum;
    }

    /**
     * 
     * @param numOfMeeting, num of meetings so that all speakers are granted a speaking slot
     * @param numOfClubMembers, how many members does the club have
     * @return how many times we need to copy all members into a huge duplicated list,
     * specifically add one more time to alleviate 'Not enough experienced member' error
     */
    public static int numOfAllMemberCopies(int numOfMeeting, int numOfClubMembers) {
        var numOfRolesPerMeeting = MeetingRoleHelper.rolesPerMeeting().size();
        int numOfNonSpeechRolesPerMeeting = numOfRolesPerMeeting - numOfSpeechesPerMeeting();
        double totalNumOfRoles = numOfMeeting * numOfNonSpeechRolesPerMeeting;
        double result = totalNumOfRoles / (double) numOfClubMembers;

        return (int) Math.ceil(result) + 1;
    }

    /**
     * Validate the member list from the user
     * @return error message key, if list is valid, return null
     */
    public static String validateErrorMessage() {
        sRolesPerMeeting = MeetingRoleHelper.rolesPerMeeting().size();
        if (clubMembers.size() < sRolesPerMeeting) {
            return "errorMessage.notEnoghMembers";
        }
        var experienced = clubMembers.stream().filter(m -> m.isExperienced).toArray();
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

        speakers.sort(MeetingRoleHelper.inExperiencedFirst());

        var alignIndex = numOfSpeechesPerMeeting() - speakers.size();
        for (int i = 0; i < speakers.size(); i++) {
            String speakerName = speakers.get(i).name;
            // TODO, pay attention
            map.put("Speaker " + (i + alignIndex + 1), speakerName);
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
            throw new RosterException("errorMessage.notEnoghExperienced.chair");
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
            throw new RosterException("errorMessage.notEnoghExperienced.general");
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

    public static String[][] generateRosterTableData() throws RosterException {
        sRolesPerMeeting = MeetingRoleHelper.rolesPerMeeting().size();
        clubMembers = FileHelper.readMemberList();
        var allSpeakers = clubMembers.stream().filter(m -> m.assignSpeech).collect(Collectors.toList());
        var numOfMeetings = numOfMeeting(allSpeakers.size());
        var numOfCopiesOfMember = numOfAllMemberCopies(numOfMeetings, clubMembers.size());
        var allMembers = new ArrayList<Member>();
        sNumOfAllSpeakers = allSpeakers.size();
        for (int i = 0; i < numOfCopiesOfMember; i++) {
            allMembers.addAll(clubMembers);
        }

        // Randomise the order
        Collections.shuffle(allSpeakers);
        Collections.shuffle(allMembers);

        String[][] data = new String[sRolesPerMeeting][numOfMeetings+1];
        // Fill the row head, various meeting roles
        var rolesPerMeeting = MeetingRoleHelper.rolesPerMeeting();
        for (int i = 0; i < rolesPerMeeting.size(); i++) {
            data[i][0] = rolesPerMeeting.get(i);
        }
        // Fill the rest of the table
        for (int j = 1; j <= numOfMeetings; j++) {
            var reserveForNewMember = PreferenceHelper.reserveForNewMember();
            var fourSpeeches = PreferenceHelper.hasFourSpeeches();
            var usualSpeeches = fourSpeeches ? 4 : 5;
            var numOfSpeaker = (j % 2 == 0 && reserveForNewMember) ? usualSpeeches - 1 : usualSpeeches;
            numOfSpeaker = Math.min(numOfSpeaker, allSpeakers.size());
            var speakers = allSpeakers.subList(0, numOfSpeaker);
            Map<String, String> rosterMap = RosterProducer.generateOneMeeting(speakers, allMembers);
            for (int i = 0; i < rolesPerMeeting.size(); i++) {
                var role = rolesPerMeeting.get(i);
                data[i][j] = rosterMap.get(role);
            }
            allSpeakers.removeAll(speakers);
        }

        return data;
    }
}
