package com.cheng.rostergenerator;

import java.util.ArrayList;
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

        speakers.sort(MeetingRoleHelper.inExperiencedFirst());
        for (int i = 0; i < speakers.size(); i++) {
            String speakerName = speakers.get(i).name;
            map.put("Speaker_" + i + 1, speakerName);
            namesOfMeeting.add(speakerName);
        }

        var chairperson = totalMembers.stream().filter(
            m -> (namesOfMeeting.indexOf(m.name) == -1) && m.isExperienced
        ).findFirst();
        if (chairperson.isPresent()) {
            map.put(TextConstants.CHAIRPERSON, chairperson.get().name);
            namesOfMeeting.add(chairperson.get().name);
        } else {
            throw new RuntimeException("Impossible, no chairperson!");
        }

        var generalEvaluator = totalMembers.stream().filter(
            m -> (namesOfMeeting.indexOf(m.name) == -1) && m.isExperienced
        ).findFirst();
        if (generalEvaluator.isPresent()) {
            map.put(TextConstants.GENERAL_EVALUATOR, generalEvaluator.get().name);
            namesOfMeeting.add(generalEvaluator.get().name);
        } else {
            throw new RuntimeException("Impossible, no general evaluator!");
        }

        var meetingRoles = MeetingRoleHelper.getMeetingRoleForAnyOne();
        for (String role : meetingRoles) {
            var anyone = totalMembers.stream().filter(
                m -> (namesOfMeeting.indexOf(m.name) == -1)
            ).findFirst();
            if (anyone.isPresent()) {
                map.put(role, anyone.get().name);
                namesOfMeeting.add(anyone.get().name);
            }
        }

        return map;
    }
}
