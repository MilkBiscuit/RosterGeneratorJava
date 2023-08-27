package test.com.cheng.rostergenerator.helper;

import com.cheng.rostergenerator.adapter.persistence.PreferenceConstants;
import com.cheng.rostergenerator.domain.MeetingRoleHelper;
import com.cheng.rostergenerator.adapter.persistence.PreferenceHelper;
import com.cheng.rostergenerator.ui.TextConstants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MeetingRoleHelperTest {

    @Test
    void testGetMeetingRoleForAnyOne() {
        var result = MeetingRoleHelper.getMeetingRoleForAnyOne();
        assertFalse(result.stream().filter(role -> role == TextConstants.CHAIRPERSON).findFirst().isPresent());
        assertFalse(result.stream().filter(role -> role == TextConstants.GENERAL_EVALUATOR).findFirst().isPresent());
        assertFalse(result.stream().filter(role -> role == TextConstants.SPEAKER_1).findFirst().isPresent());
        assertFalse(result.stream().filter(role -> role == TextConstants.SPEAKER_5).findFirst().isPresent());
    }

    @Test
    void testRolesPerMeeting() {
        PreferenceHelper.save(PreferenceConstants.KEY_FOUR_SPEECHES, true);
        PreferenceHelper.save(PreferenceConstants.KEY_TWO_TT_EVALUATORS, true);
        PreferenceHelper.save(PreferenceConstants.KEY_RESERVE_FOR_NEW, true);
        PreferenceHelper.save(PreferenceConstants.KEY_GUEST_HOSPITALITY, true);
        PreferenceHelper.save(PreferenceConstants.KEY_UM_AH_COUNTER, true);
        PreferenceHelper.save(PreferenceConstants.KEY_LISTENING_POST, true);
        var result = MeetingRoleHelper.rolesPerMeeting();
        assertFalse(result.contains("Speaker 5"));
        assertFalse(result.contains("Evaluator 5"));
        assertTrue(result.contains("Table Topic Evaluator 2"));
        assertTrue(result.contains("Guest Hospitality"));
        assertTrue(result.contains("Um-Ah Counter"));
        assertTrue(result.contains("Listening Post"));

        PreferenceHelper.save(PreferenceConstants.KEY_FOUR_SPEECHES, false);
        PreferenceHelper.save(PreferenceConstants.KEY_TWO_TT_EVALUATORS, false);
        PreferenceHelper.save(PreferenceConstants.KEY_RESERVE_FOR_NEW, false);
        PreferenceHelper.save(PreferenceConstants.KEY_GUEST_HOSPITALITY, false);
        PreferenceHelper.save(PreferenceConstants.KEY_UM_AH_COUNTER, false);
        PreferenceHelper.save(PreferenceConstants.KEY_LISTENING_POST, false);
        result = MeetingRoleHelper.rolesPerMeeting();
        assertTrue(result.contains("Speaker 5"));
        assertTrue(result.contains("Evaluator 5"));
        assertFalse(result.contains("Table Topic Evaluator 2"));
        assertFalse(result.contains("Guest Hospitality"));
        assertFalse(result.contains("Um-Ah Counter"));
        assertFalse(result.contains("Listening Post"));
    }

    @Test
    void testIsTTEvaluator() {
        var result = MeetingRoleHelper.isTTEvaluator(null);
        assertFalse(result);
        result = MeetingRoleHelper.isTTEvaluator("");
        assertFalse(result);
        result = MeetingRoleHelper.isTTEvaluator("apple");
        assertFalse(result);
        result = MeetingRoleHelper.isTTEvaluator("Chairperson");
        assertFalse(result);
        result = MeetingRoleHelper.isTTEvaluator("Guest Hospitality");
        assertFalse(result);
        result = MeetingRoleHelper.isTTEvaluator("Table Topic Evaluator 1");
        assertTrue(result);
        result = MeetingRoleHelper.isTTEvaluator("Table Topic Evaluator 2");
        assertTrue(result);
    }

    @Test
    void testIsSpeechEvaluator() {
        var result = MeetingRoleHelper.isSpeechEvaluator(null);
        assertFalse(result);
        result = MeetingRoleHelper.isSpeechEvaluator("");
        assertFalse(result);
        result = MeetingRoleHelper.isSpeechEvaluator("apple");
        assertFalse(result);
        result = MeetingRoleHelper.isSpeechEvaluator("Chairperson");
        assertFalse(result);
        result = MeetingRoleHelper.isSpeechEvaluator("Guest Hospitality");
        assertFalse(result);
        result = MeetingRoleHelper.isSpeechEvaluator("Table Topic Evaluator 1");
        assertFalse(result);
        result = MeetingRoleHelper.isSpeechEvaluator("Table Topic Evaluator 2");
        assertFalse(result);
        result = MeetingRoleHelper.isSpeechEvaluator("Evaluator 2");
        assertTrue(result);
        result = MeetingRoleHelper.isSpeechEvaluator("Evaluator 5");
        assertTrue(result);
    }

}
