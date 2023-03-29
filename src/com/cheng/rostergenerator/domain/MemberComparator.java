package com.cheng.rostergenerator.domain;

import com.cheng.rostergenerator.domain.model.Member;

import java.util.Comparator;

public class MemberComparator {

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

}
