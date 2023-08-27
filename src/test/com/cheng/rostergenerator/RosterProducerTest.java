package test.com.cheng.rostergenerator;

import com.cheng.rostergenerator.adapter.persistence.FileHelper;
import com.cheng.rostergenerator.adapter.persistence.PreferenceConstants;
import com.cheng.rostergenerator.adapter.persistence.PreferenceHelper;
import com.cheng.rostergenerator.domain.RosterProducer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RosterProducerTest {

    @Test
    void testNumOfMeeting() {
        var result = RosterProducer.numOfMeeting(0);
        assertEquals(0, result);

        PreferenceHelper.save(PreferenceConstants.KEY_FOUR_SPEECHES, true);
        PreferenceHelper.save(PreferenceConstants.KEY_RESERVE_FOR_NEW, true);
        // 18 speakers rostered like this: 4, 3, 4, 3, 4
        result = RosterProducer.numOfMeeting(18);
        assertEquals(5, result);
        result = RosterProducer.numOfMeeting(20);
        assertEquals(6, result);
        result = RosterProducer.numOfMeeting(35);
        assertEquals(10, result);

        PreferenceHelper.save(PreferenceConstants.KEY_FOUR_SPEECHES, true);
        PreferenceHelper.save(PreferenceConstants.KEY_RESERVE_FOR_NEW, false);
        result = RosterProducer.numOfMeeting(18);
        assertEquals(5, result);
        result = RosterProducer.numOfMeeting(20);
        assertEquals(5, result);
        result = RosterProducer.numOfMeeting(35);
        assertEquals(9, result);

        PreferenceHelper.save(PreferenceConstants.KEY_FOUR_SPEECHES, false);
        PreferenceHelper.save(PreferenceConstants.KEY_RESERVE_FOR_NEW, true);
        result = RosterProducer.numOfMeeting(18);
        assertEquals(4, result);
        result = RosterProducer.numOfMeeting(20);
        assertEquals(5, result);
        result = RosterProducer.numOfMeeting(35);
        assertEquals(8, result);

        PreferenceHelper.save(PreferenceConstants.KEY_FOUR_SPEECHES, false);
        PreferenceHelper.save(PreferenceConstants.KEY_RESERVE_FOR_NEW, false);
        result = RosterProducer.numOfMeeting(18);
        assertEquals(4, result);
        result = RosterProducer.numOfMeeting(20);
        assertEquals(4, result);
        result = RosterProducer.numOfMeeting(35);
        assertEquals(7, result);
    }

    @Test
    void testNumOfCopies() {
        PreferenceHelper.save(PreferenceConstants.KEY_FOUR_SPEECHES, true);
        PreferenceHelper.save(PreferenceConstants.KEY_TWO_TT_EVALUATORS, true);
        PreferenceHelper.save(PreferenceConstants.KEY_RESERVE_FOR_NEW, true);
        PreferenceHelper.save(PreferenceConstants.KEY_GUEST_HOSPITALITY, true);
        PreferenceHelper.save(PreferenceConstants.KEY_UM_AH_COUNTER, true);
        PreferenceHelper.save(PreferenceConstants.KEY_LISTENING_POST, true);
        var result = RosterProducer.numOfAllMemberCopies(6, 20);
        assertEquals(6, result);
        result = RosterProducer.numOfAllMemberCopies(10, 35);
        assertEquals(5, result);

        PreferenceHelper.save(PreferenceConstants.KEY_FOUR_SPEECHES, false);
        PreferenceHelper.save(PreferenceConstants.KEY_TWO_TT_EVALUATORS, false);
        PreferenceHelper.save(PreferenceConstants.KEY_RESERVE_FOR_NEW, false);
        PreferenceHelper.save(PreferenceConstants.KEY_GUEST_HOSPITALITY, false);
        PreferenceHelper.save(PreferenceConstants.KEY_UM_AH_COUNTER, false);
        PreferenceHelper.save(PreferenceConstants.KEY_LISTENING_POST, false);
        result = RosterProducer.numOfAllMemberCopies(6, 20);
        assertEquals(5, result);
        result = RosterProducer.numOfAllMemberCopies(10, 35);
        assertEquals(5, result);
    }

    @Test
    void testNumOfSpeechesPerMeeting() {
        PreferenceHelper.save(PreferenceConstants.KEY_FOUR_SPEECHES, true);
        PreferenceHelper.save(PreferenceConstants.KEY_RESERVE_FOR_NEW, true);
        var result = RosterProducer.numOfSpeechesPerMeeting();
        assertEquals(4, result);
        PreferenceHelper.save(PreferenceConstants.KEY_RESERVE_FOR_NEW, false);
        result = RosterProducer.numOfSpeechesPerMeeting();
        assertEquals(4, result);

        PreferenceHelper.save(PreferenceConstants.KEY_FOUR_SPEECHES, false);
        PreferenceHelper.save(PreferenceConstants.KEY_RESERVE_FOR_NEW, false);
        result = RosterProducer.numOfSpeechesPerMeeting();
        assertEquals(5, result);
        PreferenceHelper.save(PreferenceConstants.KEY_RESERVE_FOR_NEW, true);
        result = RosterProducer.numOfSpeechesPerMeeting();
        assertEquals(5, result);
    }

    @Test
    void testGenerateOneMeeting() {
        for (int i = 0; i < 100; i++) {
            var members = FileHelper.readMemberList();
            var allMembers = new ArrayList<>(members);
            allMembers.addAll(members);
            allMembers.addAll(members);
            var speakers = members.stream().filter(m -> m.name.startsWith("A")).collect(Collectors.toList());
            RosterProducer.setupRoleToNames();
            var result = RosterProducer.generateOneMeeting(speakers, allMembers);
            var nameCollection = result.values();
            var nameSet = new HashSet<>(nameCollection);
            // Should NEVER have one name appear twice or even more
            assertEquals(nameCollection.size(), nameSet.size());
        }
    }

}
