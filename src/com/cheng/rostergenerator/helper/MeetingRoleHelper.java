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
            TextConstants.GUESTHOSPITALITY,
            TextConstants.GRAMMARIAN,
            TextConstants.TIMEKEEPER,
            TextConstants.UMAHCOUNTER,
            TextConstants.EVALUATOR_1,
            TextConstants.EVALUATOR_2,
            TextConstants.EVALUATOR_3,
            TextConstants.EVALUATOR_4,
            TextConstants.TABLETOPICMASTER,
            TextConstants.TABLETOPICEVALUATOR1,
            TextConstants.TABLETOPICEVALUATOR2,
            TextConstants.LISTENINGPOST
        );
    }

}
