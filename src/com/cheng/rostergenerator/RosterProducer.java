package com.cheng.rostergenerator;

import java.util.List;

import com.cheng.rostergenerator.model.Member;

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
    
}
