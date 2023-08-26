package com.cheng.rostergenerator.adapter.persistence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.table.TableModel;

import com.cheng.rostergenerator.domain.model.Member;
import com.cheng.rostergenerator.util.TestUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class FileHelper {

    private static final String HOME_FOLDER = System.getProperty("user.home");
    private static final String MEMBER_LIST_FILE_PATH = HOME_FOLDER + "/rosterGenerator.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static List<Member> readMemberList() {
        var path = (TestUtil.isRunningUnitTest()) ? "src/test/testMemberList.json" : MEMBER_LIST_FILE_PATH;
        Path filePath = Path.of(path);
        try {
            String content = Files.readString(filePath);
            Member[] memberArray = gson.fromJson(content, Member[].class);
            return Arrays.asList(memberArray);
        } catch (Exception e) {
            printException(e);
        }

        return new ArrayList<Member>();
    }

    public static void writeMemberList(List<Member> members) {
        File file = new File(MEMBER_LIST_FILE_PATH);
        try {
            if (!memberListFileExists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            printException(e);
        }

        try (var writer = new FileWriter(file)) {
            gson.toJson(members, writer);
        } catch (Exception e) {
            printException(e);
        }
    }

    public static boolean memberListFileExists() {
        var file = new File(MEMBER_LIST_FILE_PATH);
        return file.exists();
    }

    public static void copySampleData() {
        try (var is = FileHelper.class.getResourceAsStream("/memberList.json")) {
            var desPath = Paths.get(MEMBER_LIST_FILE_PATH);
            Files.copy(is, desPath);
        } catch (IOException e) {
            printException(e);
        }
    }

    public static void deleteMemberList() {
        var file = new File(MEMBER_LIST_FILE_PATH);
        file.delete();
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
        } catch (Exception e) {
            printException(e);
        }

        return false;
    }

    public static void printException(Exception e) {
        final var file = new File(HOME_FOLDER + "/rosterGenerator_exception.log");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            var ps = new PrintStream(file);
            e.printStackTrace(ps);
            ps.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
