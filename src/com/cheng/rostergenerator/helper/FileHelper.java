package com.cheng.rostergenerator.helper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cheng.rostergenerator.model.Member;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class FileHelper {

    private static final String MEMBER_LIST_FILE_PATH = "src/res/memberList.json";
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static List<Member> readMemberList() {
        Path filePath = Path.of(MEMBER_LIST_FILE_PATH);
        try {
            String content = Files.readString(filePath);
            Member[] memberArray = gson.fromJson(content, Member[].class);
            return Arrays.asList(memberArray);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<Member>();
    }

    public static void writeMemberList(List<Member> members) {
        try (FileWriter writer = new FileWriter(MEMBER_LIST_FILE_PATH)) {
            gson.toJson(members, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean memberListFileExists() {
        var file = new File(MEMBER_LIST_FILE_PATH);
        return file.exists();
    }

}
