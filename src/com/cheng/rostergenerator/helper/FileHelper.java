package com.cheng.rostergenerator.helper;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cheng.rostergenerator.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class FileHelper {

    private static final String MEMBER_LIST_FILE_PATH = "src/res/memberList.json";
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static List<User> readUserList() {
        Path filePath = Path.of(MEMBER_LIST_FILE_PATH);
        try {
            String content = Files.readString(filePath);
            User[] userArray = gson.fromJson(content, User[].class);
            return Arrays.asList(userArray);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<User>();
    }

    public static void writeUserList(List<User> users) {
        try (FileWriter writer = new FileWriter(MEMBER_LIST_FILE_PATH)) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
