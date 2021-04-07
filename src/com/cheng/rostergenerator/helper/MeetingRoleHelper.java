package com.cheng.rostergenerator.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.cheng.rostergenerator.model.Member;
import com.cheng.rostergenerator.model.constant.TextConstants;

public class MeetingRoleHelper {

    public static Comparator<Member> inExperiencedFirst() {
        Comparator<Member> comparator = (Member o1, Member o2) -> {
            if (o1.isExperienced == o2.isExperienced) {
                return 0;
            } else if (o1.isExperienced) {
                return 1;
            } else {
                return -1;
            }
        };
        return comparator;
    }

    public static List<String> rolesPerMeeting() {
        var rolesPerMeeting = Arrays.stream(TextConstants.ROLES_PER_MEETING)
        .collect(Collectors.toCollection(ArrayList::new));

        if (PreferenceHelper.hasFourSpeeches()) {
            rolesPerMeeting.remove(TextConstants.SPEAKER_5);
            rolesPerMeeting.remove(TextConstants.EVALUATOR_5);
        }
        if (!PreferenceHelper.hasGuestHospitality()) {
            rolesPerMeeting.remove(TextConstants.GUEST_HOSPITALITY);
        }
        if (!PreferenceHelper.hasUmAhCounter()) {
            rolesPerMeeting.remove(TextConstants.UM_AH_COUNTER);
        }
        if (!PreferenceHelper.hasTwoTTEvaluator()) {
            rolesPerMeeting.remove(TextConstants.TT_EVALUATOR_2);
        }
        if (!PreferenceHelper.hasListeningPost()) {
            rolesPerMeeting.remove(TextConstants.LISTENING_POST);
        }

        return rolesPerMeeting;
    }

    /**
     * @return those roles anyone can fill per meeting, does NOT need to be experienced.
     * Exclude chairperson and general evaluator, also exclude prepared speech roles,
     */
    public static List<String> getMeetingRoleForAnyOne() {
        var rolesPerMeeting = rolesPerMeeting();
        rolesPerMeeting.remove(TextConstants.SPEAKER_1);
        rolesPerMeeting.remove(TextConstants.SPEAKER_2);
        rolesPerMeeting.remove(TextConstants.SPEAKER_3);
        rolesPerMeeting.remove(TextConstants.SPEAKER_4);
        rolesPerMeeting.remove(TextConstants.SPEAKER_5);
        rolesPerMeeting.remove(TextConstants.GENERAL_EVALUATOR);
        rolesPerMeeting.remove(TextConstants.CHAIRPERSON);

        return rolesPerMeeting;
    }

}
