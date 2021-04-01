package test.com.cheng.rostergenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import com.cheng.rostergenerator.RosterProducer;
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

}
