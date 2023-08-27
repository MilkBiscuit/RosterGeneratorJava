package test.com.cheng.rostergenerator.domain

import com.cheng.rostergenerator.domain.RosterProducer
import com.cheng.rostergenerator.domain.RosterProducer.generateRosterTableData
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class GenerateRosterTableDataTest {
    @Test
    fun testChairRoleAndGeneralEvaluatorRole() {
        (0..100).forEach { _ ->
            RosterProducer.initialValidate();
            val data = generateRosterTableData()
            val rowNum = data.size
            val columnNum = data[0].size

            // No one takes chairperson role twice in a cycle
            val chairPersonSet = mutableSetOf<String?>()
            println("---Chair start---")
            for (i in 1 until columnNum) {
                val chairPersonName = data[0][i]
                println(chairPersonName)
                chairPersonSet.add(chairPersonName)
            }
            println("---Chair end---")
            Assertions.assertEquals(columnNum - 1, chairPersonSet.size)

            // No one takes GE role twice in a cycle
            val generalEvaluatorSet = mutableSetOf<String?>()
            println("---GE start---")
            for (i in 1 until columnNum) {
                val geName = data[rowNum - 1][i]
                println(geName)
                generalEvaluatorSet.add(geName)
            }
            println("---GE end---")
            Assertions.assertEquals(columnNum - 1, generalEvaluatorSet.size)
        }
    }

}
