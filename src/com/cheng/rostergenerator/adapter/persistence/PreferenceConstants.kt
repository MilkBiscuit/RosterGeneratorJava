package com.cheng.rostergenerator.adapter.persistence

object PreferenceConstants {
    const val KEY_FOUR_SPEECHES = "numOfSpeech"
    const val KEY_TWO_TT_EVALUATORS = "numOfTT"
    const val KEY_RESERVE_FOR_NEW = "reserveForNew"
    const val KEY_GUEST_HOSPITALITY = "guestHospitality"
    const val KEY_UM_AH_COUNTER = "umAhCounter"
    const val KEY_LISTENING_POST = "listening"
    // TODO: JVMStatic annotation?
    val SETTING_KEYS: Array<String> = arrayOf(
        KEY_FOUR_SPEECHES,
        KEY_TWO_TT_EVALUATORS,
        KEY_RESERVE_FOR_NEW,
        KEY_GUEST_HOSPITALITY,
        KEY_UM_AH_COUNTER,
        KEY_LISTENING_POST
    )
}
