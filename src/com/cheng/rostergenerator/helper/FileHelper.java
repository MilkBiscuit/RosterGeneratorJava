package com.cheng.rostergenerator.helper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.table.TableModel;

import com.cheng.rostergenerator.model.Member;
import com.cheng.rostergenerator.util.TestUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class FileHelper {

    private static final String MEMBER_LIST_FILE_PATH = "res/memberList.json";
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static List<Member> readMemberList() {
        var path = (TestUtil.isRunningUnitTest()) ? "src/test/testMemberList.json" : MEMBER_LIST_FILE_PATH;
        Path filePath = Path.of(path);
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

    public static boolean exportToCSV(TableModel model, String exportToPath) {
        try {
            var file = new File(exportToPath);
            if (!file.exists()) {
                file.createNewFile();
            }
            var csv = new FileWriter(new File(exportToPath));
            for (int i = 0; i < model.getColumnCount(); i++) {
                csv.write(model.getColumnName(i) + ",");
            }
            csv.write("\n");

            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    csv.write(model.getValueAt(i, j).toString() + ",");
                }
                csv.write("\n");
            }
            csv.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

}
