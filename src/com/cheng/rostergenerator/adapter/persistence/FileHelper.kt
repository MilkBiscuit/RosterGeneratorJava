package com.cheng.rostergenerator.adapter.persistence

import com.cheng.rostergenerator.domain.model.Member
import com.cheng.rostergenerator.util.TestUtil
import com.google.gson.GsonBuilder
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.PrintStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

object FileHelper {
    private val HOME_FOLDER = System.getProperty("user.home")
    private val MEMBER_LIST_FILE_PATH = "$HOME_FOLDER/rosterGenerator.json"
    private val gson = GsonBuilder().setPrettyPrinting().create()

    @JvmStatic
    fun readMemberList(): List<Member> {
        val path = if (TestUtil.isRunningUnitTest()) "src/test/testMemberList.json" else MEMBER_LIST_FILE_PATH
        val filePath = Path.of(path)
        try {
            val content = Files.readString(filePath)
            val memberArray = gson.fromJson(
                content,
                Array<Member>::class.java
            )
            return memberArray.toList()
        } catch (e: Exception) {
            printException(e)
        }
        return ArrayList()
    }

    @JvmStatic
    fun writeMemberList(members: List<Member>) {
        val file = File(MEMBER_LIST_FILE_PATH)
        try {
            if (!memberListFileExists()) {
                file.createNewFile()
            }
        } catch (e: Exception) {
            printException(e)
        }
        try {
            FileWriter(file).use { writer -> gson.toJson(members, writer) }
        } catch (e: Exception) {
            printException(e)
        }
    }

    @JvmStatic
    fun memberListFileExists(): Boolean {
        val file = File(MEMBER_LIST_FILE_PATH)
        return file.exists()
    }

    @JvmStatic
    fun copySampleData() {
        try {
            FileHelper::class.java.getResourceAsStream("/memberList.json").use { inputStream ->
                val desPath = Paths.get(MEMBER_LIST_FILE_PATH)
                Files.copy(inputStream, desPath)
            }
        } catch (e: IOException) {
            printException(e)
        }
    }

    @JvmStatic
    fun deleteMemberList() {
        val file = File(MEMBER_LIST_FILE_PATH)
        file.delete()
    }

    @JvmStatic
    fun printException(e: Exception) {
        val file = File("$HOME_FOLDER/rosterGenerator_exception.log")
        try {
            if (!file.exists()) {
                file.createNewFile()
            }
            val ps = PrintStream(file)
            e.printStackTrace(ps)
            ps.close()
        } catch (exception: IOException) {
            exception.printStackTrace()
        }
    }
}
