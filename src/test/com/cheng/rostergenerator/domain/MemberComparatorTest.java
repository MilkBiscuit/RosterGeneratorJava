package test.com.cheng.rostergenerator.domain;

import com.cheng.rostergenerator.domain.MemberComparator;
import com.cheng.rostergenerator.domain.model.Member;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MemberComparatorTest {

    @Test
    void testInExperiencedFirst() {
        var experiencedMember = new Member("AAA", true, true);
        var inexperiencedMember = new Member("BBB", false, false);
        var result = MemberComparator.inExperiencedFirst().compare(experiencedMember, inexperiencedMember);
        assertTrue(result > 0);

        result = MemberComparator.inExperiencedFirst().compare(inexperiencedMember, experiencedMember);
        assertTrue(result < 0);

        inexperiencedMember.isExperienced = true;
        result = MemberComparator.inExperiencedFirst().compare(inexperiencedMember, experiencedMember);
        assertEquals(0, result);
    }

}
