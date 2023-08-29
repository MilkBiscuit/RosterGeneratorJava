package com.cheng.rostergenerator.util

import com.cheng.rostergenerator.adapter.persistence.FileHelper
import java.io.File
import java.io.FileWriter
import javax.swing.table.TableModel

object CsvHelper {

    // TODO: Split into 2 functions and add unit test
    @JvmStatic
    fun exportToCSV(model: TableModel, exportToPath: String): Boolean {
        try {
            val file = File(exportToPath)
            if (!file.exists()) {
                file.createNewFile()
            }
            val csvFileWriter = FileWriter(exportToPath)
            for (i in 0 until model.columnCount) {
                csvFileWriter.write(model.getColumnName(i) + ",")
            }
            csvFileWriter.write("\n")
            for (i in 0 until model.rowCount) {
                for (j in 0 until model.columnCount) {
                    csvFileWriter.write(model.getValueAt(i, j).toString() + ",")
                }
                csvFileWriter.write("\n")
            }
            csvFileWriter.close()
            return true
        } catch (e: Exception) {
            FileHelper.printException(e)
        }
        return false
    }
}
