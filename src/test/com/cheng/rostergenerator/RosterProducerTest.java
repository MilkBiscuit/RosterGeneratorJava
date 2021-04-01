package test.com.cheng.rostergenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

import com.cheng.rostergenerator.RosterProducer;
import com.cheng.rostergenerator.helper.FileHelper;
import com.cheng.rostergenerator.model.Member;

import org.junit.jupiter.api.Test;

class RosterProducerTest {

    @Test
    void testNumOfMeeting() {
        var members = new ArrayList<Member>(0);
        var result = RosterProducer.numOfMeeting(members);
        assertEquals(0, result);

        for (int i = 0; i < 20; i++) {
            members.add(null);
        }
        result = RosterProducer.numOfMeeting(members);
        assertEquals(6, result);
        // 35 members in the club in total
        for (int i = 0; i < 15; i++) {
            members.add(null);
        }
        result = RosterProducer.numOfMeeting(members);
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
