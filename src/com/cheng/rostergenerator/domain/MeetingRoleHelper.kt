package com.cheng.rostergenerator.domain

import com.cheng.rostergenerator.adapter.persistence.PreferenceHelper
import com.cheng.rostergenerator.ui.TextConstants
import java.util.*
import java.util.stream.Collectors

object MeetingRoleHelper {
    @JvmStatic
    fun rolesPerMeeting(): MutableList<String> {
        val rolesPerMeeting = Arrays.stream(TextConstants.ROLES_PER_MEETING)
            .collect(
                Collectors.toCollection { ArrayList() }
            )
        if (PreferenceHelper.hasFourSpeeches()) {
            rolesPerMeeting.remove(TextConstants.SPEAKER_5)
            rolesPerMeeting.remove(TextConstants.EVALUATOR_5)
        }
        if (!PreferenceHelper.hasGuestHospitality()) {
            rolesPerMeeting.remove(TextConstants.GUEST_HOSPITALITY)
        }
        if (!PreferenceHelper.hasUmAhCounter()) {
            rolesPerMeeting.remove(TextConstants.UM_AH_COUNTER)
        }
        if (!PreferenceHelper.hasTwoTTEvaluator()) {
            rolesPerMeeting.remove(TextConstants.TT_EVALUATOR_2)
        }
        if (!PreferenceHelper.hasListeningPost()) {
            rolesPerMeeting.remove(TextConstants.LISTENING_POST)
        }
        return rolesPerMeeting
    }

    /**
     * @return those roles anyone can fill per meeting, does NOT need to be experienced.
     * Exclude chairperson and general evaluator, also exclude prepared speech roles,
     */
    @JvmStatic
    fun getMeetingRoleForAnyOne(): List<String>? {
        val rolesPerMeeting = rolesPerMeeting()
        rolesPerMeeting.remove(TextConstants.SPEAKER_1)
        rolesPerMeeting.remove(TextConstants.SPEAKER_2)
        rolesPerMeeting.remove(TextConstants.SPEAKER_3)
        rolesPerMeeting.remove(TextConstants.SPEAKER_4)
        rolesPerMeeting.remove(TextConstants.SPEAKER_5)
        rolesPerMeeting.remove(TextConstants.GENERAL_EVALUATOR)
        rolesPerMeeting.remove(TextConstants.CHAIRPERSON)
        return rolesPerMeeting
    }

    @JvmStatic
    fun isTTEvaluator(role: String): Boolean {
        return TextConstants.TT_EVALUATOR_1 === role || TextConstants.TT_EVALUATOR_2 === role
    }

    @JvmStatic
    fun isSpeechEvaluator(role: String): Boolean {
        return TextConstants.EVALUATOR_1 == role || TextConstants.EVALUATOR_2 == role || TextConstants.EVALUATOR_3 == role || TextConstants.EVALUATOR_4 == role || TextConstants.EVALUATOR_5 == role
    }
}
