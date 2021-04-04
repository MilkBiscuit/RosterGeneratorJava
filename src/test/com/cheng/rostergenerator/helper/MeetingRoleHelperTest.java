package test.com.cheng.rostergenerator.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.cheng.rostergenerator.helper.MeetingRoleHelper;
import com.cheng.rostergenerator.helper.PreferenceHelper;
import com.cheng.rostergenerator.model.Member;
import com.cheng.rostergenerator.model.constant.PrefConstants;
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

    @Test
    void testRolesPerMeeting() {
        PreferenceHelper.save(PrefConstants.KEY_FOUR_SPEECHES, true);
        PreferenceHelper.save(PrefConstants.KEY_TWO_TT_EVALUATORS, true);
        PreferenceHelper.save(PrefConstants.KEY_RESERVE_FOR_NEW, true);
        PreferenceHelper.save(PrefConstants.KEY_GUEST_HOSPITALITY, true);
        PreferenceHelper.save(PrefConstants.KEY_UM_AH_COUNTER, true);
        PreferenceHelper.save(PrefConstants.KEY_LISTENING_POST, true);
        var result = MeetingRoleHelper.rolesPerMeeting();
        assertFalse(result.contains("Speaker 5"));
        assertFalse(result.contains("Evaluator 5"));
        assertTrue(result.contains("Table Topic Evaluator 2"));
        assertTrue(result.contains("Guest Hospitality"));
        assertTrue(result.contains("Um-Ah Counter"));
        assertTrue(result.contains("Listening Post"));

        PreferenceHelper.save(PrefConstants.KEY_FOUR_SPEECHES, false);
        PreferenceHelper.save(PrefConstants.KEY_TWO_TT_EVALUATORS, false);
        PreferenceHelper.save(PrefConstants.KEY_RESERVE_FOR_NEW, false);
        PreferenceHelper.save(PrefConstants.KEY_GUEST_HOSPITALITY, false);
        PreferenceHelper.save(PrefConstants.KEY_UM_AH_COUNTER, false);
        PreferenceHelper.save(PrefConstants.KEY_LISTENING_POST, false);
        result = MeetingRoleHelper.rolesPerMeeting();
        assertTrue(result.contains("Speaker 5"));
        assertTrue(result.contains("Evaluator 5"));
        assertFalse(result.contains("Table Topic Evaluator 2"));
        assertFalse(result.contains("Guest Hospitality"));
        assertFalse(result.contains("Um-Ah Counter"));
        assertFalse(result.contains("Listening Post"));
    }

}
