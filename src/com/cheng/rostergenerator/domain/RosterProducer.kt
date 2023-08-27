package com.cheng.rostergenerator.domain

import com.cheng.rostergenerator.adapter.persistence.FileHelper
import com.cheng.rostergenerator.adapter.persistence.PreferenceHelper
import com.cheng.rostergenerator.domain.MeetingRoleHelper.rolesPerMeeting
import com.cheng.rostergenerator.domain.model.Member
import com.cheng.rostergenerator.domain.model.RosterException
import com.cheng.rostergenerator.ui.TextConstants
import com.cheng.rostergenerator.util.ResBundleUtil
import java.util.*
import java.util.stream.Collectors
import kotlin.math.ceil
import kotlin.math.min

object RosterProducer {

    private var sNumOfAllSpeakers = 0
    private var sNumOfRolesPerMeeting = 0
    private var sNumOfMeetings = 0
    private var sNumOfCopiesOfClubMembers = 0
    private var clubMembers: List<Member> = emptyList()
    private val roleToNames: MutableMap<String, ArrayList<String>> = HashMap()

    /**
     * Validate the member list from the user
     * @return error message key, if list is valid, return null
     */
    @JvmStatic
    fun initialValidate(): String? {
        clubMembers = FileHelper.readMemberList()
        sNumOfRolesPerMeeting = rolesPerMeeting().size
        val speakerNum = clubMembers.stream().filter { m: Member? -> m!!.assignSpeech }
            .count().toInt()
        sNumOfMeetings = numOfMeeting(speakerNum)
        sNumOfCopiesOfClubMembers = numOfAllMemberCopies(sNumOfMeetings, clubMembers.size)
        if (clubMembers.size < sNumOfRolesPerMeeting) {
            return "errorMessage.notEnoughMembers"
        }
        val experiencedNum = clubMembers.stream().filter { m: Member? -> m!!.isExperienced }
            .count()
        return if (experiencedNum < sNumOfMeetings) {
            // At least the chairperson role should NOT be duplicate
            "errorMessage.notEnoughExperienced"
        } else null
    }

    /**
     *
     * @param numOfSpeakers
     * @return num of meetings we need to accommodate all speakers
     */
    @JvmStatic
    fun numOfMeeting(numOfSpeakers: Int): Int {
        // TODO: Use numOfSpeechesPerMeetingFloat to simplify the logic
        if (numOfSpeakers == 0) {
            return 0
        }
        val reserveForNew = PreferenceHelper.reserveForNewMember()
        val fourSpeeches = PreferenceHelper.hasFourSpeeches()
        return if (reserveForNew) {
            if (fourSpeeches) {
                val remainder = numOfSpeakers % 7
                if (remainder == 0) {
                    numOfSpeakers / 7 * 2
                } else if (remainder <= 4) {
                    numOfSpeakers / 7 * 2 + 1
                } else {
                    numOfSpeakers / 7 * 2 + 2
                }
            } else {
                val remainder = numOfSpeakers % 9
                if (remainder == 0) {
                    numOfSpeakers / 9 * 2
                } else if (remainder <= 5) {
                    numOfSpeakers / 9 * 2 + 1
                } else {
                    numOfSpeakers / 9 * 2 + 2
                }
            }
        } else ceil((numOfSpeakers / if (fourSpeeches) 4.0f else 5.0f).toDouble()).toInt()
    }

    @JvmStatic
    fun generateRosterTableInstructionTitle(): String {
        val titleFormat = ResBundleUtil.getString("rosterTable.title")
        val numOfSpeeches = numOfSpeechesPerMeetingFloat()
        return String.format(
            titleFormat, sNumOfAllSpeakers, numOfSpeeches, sNumOfMeetings, sNumOfRolesPerMeeting,
            sNumOfMeetings * sNumOfRolesPerMeeting, clubMembers.size, sNumOfCopiesOfClubMembers
        )
    }

    // public only for unit test
    @JvmStatic
    fun numOfSpeechesPerMeeting(): Int {
        val uiNum = numOfSpeechesPerMeetingFloat()
        return ceil(uiNum.toDouble()).toInt()
    }

    /**
     *
     * @param numOfMeeting, num of meetings so that all speakers are granted a speaking slot
     * @param numOfClubMembers, how many members does the club have
     * @return how many times we need to copy all members into a huge duplicated list,
     * specifically add one more time to alleviate 'Not enough experienced member' error
     */
    @JvmStatic
    fun numOfAllMemberCopies(numOfMeeting: Int, numOfClubMembers: Int): Int {
        val numOfRolesPerMeeting = rolesPerMeeting().size
        val numOfNonSpeechRolesPerMeeting = numOfRolesPerMeeting - numOfSpeechesPerMeeting()
        val totalNumOfNonSpeechRoles = (numOfMeeting * numOfNonSpeechRolesPerMeeting).toDouble()
        val result = totalNumOfNonSpeechRoles / numOfClubMembers.toDouble()
        return ceil(result).toInt() + 1
    }

    /**
     * set to public only for unit test
     *
     * @param speakers, prepared speech speakers this meeting, it should NOT have duplicate values
     * @param totalMembers, n * (whole club member list)
     * @return map of 'meeting role' to 'name'
     */
    @JvmStatic
    fun generateOneMeeting(speakers: List<Member>, totalMembers: MutableList<Member>): Map<String, String> {
        val retMap = HashMap<String, String>()
        val namesOfMeeting = ArrayList<String>(sNumOfRolesPerMeeting)
        if (speakers.isEmpty() || speakers.size > 5) {
            println("num of speakers is invalid")
            return retMap
        }

        Collections.sort(speakers, MemberComparator.inExperiencedFirst())
        // Speaker number starts from '1' instead of '0',
        // If a speaking slot is reserved for a NEW MEMBER, then Speaker number starts from '2' instead of '0'.
        val firstSpeakerNo = numOfSpeechesPerMeeting() - speakers.size + 1
        for (i in speakers.indices) {
            val speakerName = speakers[i].name
            retMap["Speaker " + (i + firstSpeakerNo)] = speakerName
            namesOfMeeting.add(speakerName)
        }
        totalMembers.shuffle()
        val chairPersonNames = roleToNames[TextConstants.CHAIRPERSON]!!
        val optChairperson = totalMembers.stream().filter { m: Member -> !namesOfMeeting.contains(m.name)
                && !chairPersonNames.contains(m.name)
                && m.isExperienced
        }.findFirst()
        if (optChairperson.isPresent) {
            val chair = optChairperson.get()
            retMap[TextConstants.CHAIRPERSON] = chair.name
            namesOfMeeting.add(chair.name)
            totalMembers.remove(chair)
        } else {
            throw RosterException("errorMessage.notEnoughExperienced.runtime")
        }
        val generalEvaluatorNames = roleToNames[TextConstants.GENERAL_EVALUATOR]!!
        val optGeneral = totalMembers.stream().filter { m: Member -> !namesOfMeeting.contains(m.name)
                && !generalEvaluatorNames.contains(m.name)
                && m.isExperienced
        }.findFirst()
        if (optGeneral.isPresent) {
            val general = optGeneral.get()
            retMap[TextConstants.GENERAL_EVALUATOR] = general.name
            namesOfMeeting.add(general.name)
            totalMembers.remove(general)
        } else {
            throw RosterException("errorMessage.notEnoughExperienced.runtime")
        }
        for (role in MeetingRoleHelper.getMeetingRoleForAnyOne()) {
            val optAnyOne: Optional<Member> = totalMembers.stream().filter { m: Member ->
                !namesOfMeeting.contains(m.name)
            }.findFirst()
            if (optAnyOne.isPresent) {
                val anyone = optAnyOne.get()
                retMap[role] = anyone.name
                namesOfMeeting.add(anyone.name)
                totalMembers.remove(anyone)
            }
        }

        return retMap
    }

    // TODO: Add unit test
    @Throws(RosterException::class)
    @JvmStatic
    fun generateRosterTableData(): Array<Array<String?>> {
        val rolesPerMeeting: List<String> = rolesPerMeeting()
        sNumOfRolesPerMeeting = rolesPerMeeting.size
        // sNumOfMeetings, sNumOfCopiesOfClubMembers were already calculated in initialValidate()
        clubMembers = FileHelper.readMemberList()
        val allSpeakers = clubMembers.stream().filter { m: Member? -> m!!.assignSpeech }
            .collect(Collectors.toList())
        sNumOfAllSpeakers = allSpeakers.size
        val allMembers = ArrayList<Member>()
        for (i in 0 until sNumOfCopiesOfClubMembers) {
            allMembers.addAll(clubMembers)
        }

        // Randomise the speakers order
        allSpeakers.shuffle()
        val retData: Array<Array<String?>> = Array(sNumOfRolesPerMeeting) {
            arrayOfNulls<String>(
                sNumOfMeetings + 1
            )
        }
        // Fill the row head with the title of various meeting roles
        for (i in 0 until sNumOfRolesPerMeeting) {
            retData[i][0] = rolesPerMeeting[i]
        }
        setupRoleToNames()
        // Fill the rest of the table with meeting role values
        for (j in 1..sNumOfMeetings) {
            // Randomise the order
            allMembers.shuffle()
            val reserveForNewMember = PreferenceHelper.reserveForNewMember()
            val fourSpeeches = PreferenceHelper.hasFourSpeeches()
            val usualSpeeches = if (fourSpeeches) 4 else 5
            var numOfSpeaker = if (j % 2 == 0 && reserveForNewMember) usualSpeeches - 1 else usualSpeeches
            numOfSpeaker = min(numOfSpeaker, allSpeakers.size)
            val speakers: List<Member> = allSpeakers.subList(0, numOfSpeaker)
            val rosterMap = generateOneMeeting(speakers, allMembers)
            for ((role, name) in rosterMap) {
                roleToNames[role]!!.add(name)
            }
            for (i in 0 until sNumOfRolesPerMeeting) {
                val role = rolesPerMeeting[i]
                retData[i][j] = rosterMap[role]
            }
            allSpeakers.removeAll(speakers)
        }
        return retData
    }

    @JvmStatic
    fun setupRoleToNames() {
        val rolesPerMeeting: List<String> = rolesPerMeeting()
        roleToNames.clear()
        for (role in rolesPerMeeting) {
            roleToNames[role] = ArrayList()
        }
    }

    private fun numOfSpeechesPerMeetingFloat(): Float {
        val reserveForNew = PreferenceHelper.reserveForNewMember()
        val hasFourSpeeches = PreferenceHelper.hasFourSpeeches()
        return if (reserveForNew && hasFourSpeeches) {
            3.5f
        } else if (reserveForNew && !hasFourSpeeches) {
            4.5f
        } else if (!reserveForNew && hasFourSpeeches) {
            4.0f
        } else {
            5.0f
        }
    }
}
