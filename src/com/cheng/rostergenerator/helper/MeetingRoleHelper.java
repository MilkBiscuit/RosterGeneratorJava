package com.cheng.rostergenerator.helper;

import java.util.Comparator;
import java.util.List;

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

    /**
     * @return those roles anyone can fill per meeting, does NOT need to be experienced.
     * Exclude chairperson and general evaluator, also exclude prepared speech roles,
     * so 12 meeting roles
     */
    public static List<String> getMeetingRoleForAnyOne() {
        return List.of(
            TextConstants.GUEST_HOSPITALITY,
            TextConstants.GRAMMARIAN,
            TextConstants.TIMER,
            TextConstants.UM_AH_COUNTER,
            TextConstants.EVALUATOR_1,
            TextConstants.EVALUATOR_2,
            TextConstants.EVALUATOR_3,
            TextConstants.EVALUATOR_4,
            TextConstants.TT_MASTER,
            TextConstants.TT_EVALUATOR_1,
            TextConstants.TT_EVALUATOR_2,
            TextConstants.LISTENING_POST
        );
    }

}
