package com.cheng.rostergenerator.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.cheng.rostergenerator.adapter.persistence.FileHelper;
import com.cheng.rostergenerator.helper.MeetingRoleHelper;
import com.cheng.rostergenerator.helper.PreferenceHelper;
import com.cheng.rostergenerator.helper.ResBundleHelper;
import com.cheng.rostergenerator.domain.model.Member;
import com.cheng.rostergenerator.domain.model.RosterException;
import com.cheng.rostergenerator.ui.TextConstants;

public class RosterProducer {

    private static int sNumOfAllSpeakers = 0;
    private static int sNumOfRolesPerMeeting = 0;
    private static int sNumOfMeetings = 0;
    private static int sNumOfCopiesOfClubMembers = 0;
    private static List<Member> clubMembers = null;
    private static Map<String, ArrayList<String>> roleToNames = new HashMap<>();

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

    public static String generateRosterTableInstructionTitle() {
        final var titleFormat = ResBundleHelper.getString("rosterTable.title");
        final var numOfSpeeches = RosterProducer.numOfSpeechesPerMeetingString();

        return String.format(titleFormat,
            sNumOfAllSpeakers, numOfSpeeches, sNumOfMeetings,
            sNumOfRolesPerMeeting, sNumOfMeetings * sNumOfRolesPerMeeting, clubMembers.size(),
            sNumOfCopiesOfClubMembers
        );
    }

    // public only for unit test
    public static int numOfSpeechesPerMeeting() {
        var uiNum = numOfSpeechesPerMeetingString();
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
        clubMembers = FileHelper.readMemberList();
        sNumOfRolesPerMeeting = MeetingRoleHelper.rolesPerMeeting().size();
        var speakerNum = (int) clubMembers.stream().filter(m -> m.assignSpeech).count();
        sNumOfMeetings = numOfMeeting(speakerNum);
        sNumOfCopiesOfClubMembers = numOfAllMemberCopies(sNumOfMeetings, clubMembers.size());
        if (clubMembers.size() < sNumOfRolesPerMeeting) {
            return "errorMessage.notEnoughMembers";
        }
        var experiencedNum = clubMembers.stream().filter(m -> m.isExperienced).count();
        if (experiencedNum < sNumOfMeetings) {
            // At least the chairperson role should NOT be duplicate
            return "errorMessage.notEnoughExperienced";
        }

        return null;
    }

    /**
     * set to public only for unit test
     * 
     * @param speakers, prepared speech speakers this meeting, it should NOT have duplicate values
     * @param totalMembers, n * (whole club member list)
     * @return map of 'meeting role' to 'name'
     */
    public static Map<String, String> generateOneMeeting(List<Member> speakers, List<Member> totalMembers) {
        var map = new HashMap<String, String>();
        var namesOfMeeting = new ArrayList<String>(sNumOfRolesPerMeeting);
        if (speakers == null || speakers.isEmpty() || speakers.size() > 5) {
            System.out.println("num of speakers is invalid");

            return map;
        }

        speakers.sort(MeetingRoleHelper.inExperiencedFirst());

        // Speaker number starts from '1' instead of '0'
        var alignIndex = numOfSpeechesPerMeeting() - speakers.size() + 1;
        for (int i = 0; i < speakers.size(); i++) {
            String speakerName = speakers.get(i).name;
            map.put("Speaker " + (i + alignIndex), speakerName);
            namesOfMeeting.add(speakerName);
        }

        var chairPersonNames = roleToNames.get(TextConstants.CHAIRPERSON);
        var optChairperson = totalMembers.stream().filter(m -> {
            var name = m.name;
            return (namesOfMeeting.indexOf(name) == -1)
            && (chairPersonNames.indexOf(name) == - 1)
            && m.isExperienced;
        }).findFirst();
        if (optChairperson.isPresent()) {
            var chair = optChairperson.get();
            map.put(TextConstants.CHAIRPERSON, chair.name);
            namesOfMeeting.add(chair.name);
            totalMembers.remove(chair);
        } else {
            throw new RosterException("errorMessage.notEnoughExperienced.runtime");
        }

        var generalEvaluatorNames = roleToNames.get(TextConstants.GENERAL_EVALUATOR);
        var optGeneral = totalMembers.stream().filter(m -> {
            var name = m.name;
            return (namesOfMeeting.indexOf(name) == -1)
            && (generalEvaluatorNames.indexOf(name) == -1)
            && m.isExperienced;
        }).findFirst();
        if (optGeneral.isPresent()) {
            var general = optGeneral.get();
            map.put(TextConstants.GENERAL_EVALUATOR, general.name);
            namesOfMeeting.add(general.name);
            totalMembers.remove(general);
        } else {
            throw new RosterException("errorMessage.notEnoughExperienced.runtime");
        }

        var meetingRoles = MeetingRoleHelper.getMeetingRoleForAnyOne();
        for (String role : meetingRoles) {
            List<String> namesForRole = roleToNames.get(role);
            Optional<Member> optAnyOne;
            if (MeetingRoleHelper.isTTEvaluator(role)) {
                var ttEvaluatorNames = new ArrayList<String>();
                var ttEvaluator1 = roleToNames.get(TextConstants.TT_EVALUATOR_1);
                if (!ttEvaluator1.isEmpty()) {
                    ttEvaluatorNames.add(ttEvaluator1.get(ttEvaluator1.size() - 1));
                }
                var ttEvaluator2 = roleToNames.get(TextConstants.TT_EVALUATOR_2);
                if (ttEvaluator2 != null && !ttEvaluator2.isEmpty()) {
                    ttEvaluatorNames.add(ttEvaluator2.get(ttEvaluator2.size() - 1));
                }
                optAnyOne = totalMembers.stream().filter(
                    m -> (namesOfMeeting.indexOf(m.name) == -1) && (ttEvaluatorNames.indexOf(m.name) == -1)
                ).findFirst();
            } else {
                var excludeNameForRole = namesForRole.isEmpty() ? "" : namesForRole.get(namesForRole.size() - 1);
                optAnyOne = totalMembers.stream().filter(
                    m -> (namesOfMeeting.indexOf(m.name) == -1) && (m.name != excludeNameForRole)
                ).findFirst();
            }
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
        var rolesPerMeeting = MeetingRoleHelper.rolesPerMeeting();
        sNumOfRolesPerMeeting = rolesPerMeeting.size();
        // sNumOfMeetings, sNumOfCopiesOfClubMembers were already calculated in validateMessage()
        clubMembers = FileHelper.readMemberList();
        var allSpeakers = clubMembers.stream().filter(m -> m.assignSpeech).collect(Collectors.toList());
        var allMembers = new ArrayList<Member>();
        sNumOfAllSpeakers = allSpeakers.size();
        for (int i = 0; i < sNumOfCopiesOfClubMembers; i++) {
            allMembers.addAll(clubMembers);
        }

        // Randomise the order
        Collections.shuffle(allSpeakers);
        String[][] data = new String[sNumOfRolesPerMeeting][sNumOfMeetings+1];
        // Fill the row head, name of various meeting roles
        for (int i = 0; i < sNumOfRolesPerMeeting; i++) {
            data[i][0] = rolesPerMeeting.get(i);
        }

        setupRoleToNames();
        // Fill the rest of the table, value of meeting roles
        for (int j = 1; j <= sNumOfMeetings; j++) {
            // Randomise the order
            Collections.shuffle(allMembers);

            var reserveForNewMember = PreferenceHelper.reserveForNewMember();
            var fourSpeeches = PreferenceHelper.hasFourSpeeches();
            var usualSpeeches = fourSpeeches ? 4 : 5;
            var numOfSpeaker = (j % 2 == 0 && reserveForNewMember) ? usualSpeeches - 1 : usualSpeeches;
            numOfSpeaker = Math.min(numOfSpeaker, allSpeakers.size());
            var speakers = allSpeakers.subList(0, numOfSpeaker);
            Map<String, String> rosterMap = RosterProducer.generateOneMeeting(speakers, allMembers);
            for (var roleEntry : rosterMap.entrySet()) {
                var role = roleEntry.getKey();
                var name = roleEntry.getValue();
                roleToNames.get(role).add(name);
            }
            for (int i = 0; i < sNumOfRolesPerMeeting; i++) {
                var role = rolesPerMeeting.get(i);
                data[i][j] = rosterMap.get(role);
            }
            allSpeakers.removeAll(speakers);
        }

        return data;
    }

    public static void setupRoleToNames() {
        var rolesPerMeeting = MeetingRoleHelper.rolesPerMeeting();
        roleToNames.clear();
        for (String role : rolesPerMeeting) {
            roleToNames.put(role, new ArrayList<String>());
        }
    }

    private static float numOfSpeechesPerMeetingString() {
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
}
