package test.com.cheng.rostergenerator.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.cheng.rostergenerator.helper.MeetingRoleHelper;
import com.cheng.rostergenerator.model.Member;
import com.cheng.rostergenerator.model.constant.TextConstants;

import org.junit.jupiter.api.Test;

class MeetingRoleHelperTest {

    @Test
    void testGetMeetingRoleForAnyOne() {
        var result = MeetingRoleHelper.getMeetingRoleForAnyOne();
        assertFalse(result.stream().filter(role -> role == TextConstants.CHAIRPERSON).findFirst().isPresent());
        assertFalse(result.stream().filter(role -> role == TextConstants.GENERAL_EVALUATOR).findFirst().isPresent());
        assertFalse(result.stream().filter(role -> role == TextConstants.SPEAKER_1).findFirst().isPresent());
        assertEquals(12, result.size());
    }

    @Test
    void testInExperiencedFirst() {
        var experiencedMember = new Member("AAA", true, true);
        var inexperiencedMember = new Member("BBB", false, false);
        var result = MeetingRoleHelper.inExperiencedFirst().compare(experiencedMember, inexperiencedMember);
        assertTrue(result > 0);

        result = MeetingRoleHelper.inExperiencedFirst().compare(inexperiencedMember, experiencedMember);
        assertTrue(result < 0);

        inexperiencedMember.isExperienced = true;
        result = MeetingRoleHelper.inExperiencedFirst().compare(inexperiencedMember, experiencedMember);
        assertTrue(result == 0);
    }

}
