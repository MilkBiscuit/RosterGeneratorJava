package test.com.cheng.rostergenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

import com.cheng.rostergenerator.RosterProducer;
import com.cheng.rostergenerator.helper.FileHelper;

import org.junit.jupiter.api.Test;

class RosterProducerTest {

    @Test
    void testNumOfMeeting() {
        var result = RosterProducer.numOfMeeting(0);
        assertEquals(0, result);

        // 18 speakers rostered like this: 4, 3, 4, 3, 4
        result = RosterProducer.numOfMeeting(18);
        assertEquals(5, result);

        result = RosterProducer.numOfMeeting(20);
        assertEquals(6, result);
        result = RosterProducer.numOfMeeting(35);
        assertEquals(10, result);
    }

    @Test
    void testNumOfAllMembers() {
        var result = RosterProducer.numOfAllMembers(6, 20);
        assertEquals(5, result);

        result = RosterProducer.numOfAllMembers(10, 35);
        assertEquals(4, result);
    }

    @Test
    void testGenerateOneMeeting() {
        var members = FileHelper.readMemberList();
        var allMembers = new ArrayList<>(members);
        allMembers.addAll(members);
        allMembers.addAll(members);
        var cMembers = members.stream().filter(m -> m.name.startsWith("A")).collect(Collectors.toList());

        var result = RosterProducer.generateOneMeeting(cMembers, allMembers);
        var nameCollection = result.values();
        var nameSet = new HashSet<>(nameCollection);
        // Should NEVER have one name appear twice or even more
        assertEquals(nameCollection.size(), nameSet.size());

        System.out.println(result);
    }

}
