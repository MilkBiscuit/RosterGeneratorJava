package com.cheng.rostergenerator.helper;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.cheng.rostergenerator.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FileHelper {

    private static final String MEMBER_LIST_FILE_PATH = "src/res/memberList.json";
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static List<User> readUserList() {
        JSONParser jsonParser = new JSONParser();
        ArrayList<User> memberList = new ArrayList<User>();
        try (FileReader reader = new FileReader(MEMBER_LIST_FILE_PATH))
        {
            Object obj = jsonParser.parse(reader);
            JSONArray jsonArray = (JSONArray) obj;
            if (jsonArray != null) {
                int len = jsonArray.size();
                for (int i = 0; i < len; i++) {
                    var jsonString = (String) jsonArray.get(i);
                    User user = gson.fromJson(jsonString, User.class);
                    memberList.add(user);
                }
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return memberList;
    }

    public static void writeUserList(List<User> users) {
        JSONArray employeeList = new JSONArray();
        for (var user : users) {
            var jsonObject = gson.toJson(user);
            employeeList.add(jsonObject);
        }

        var formattedArray = gson.toJson(employeeList);
        formattedArray = formattedArray.concat("\n");
        try (FileWriter file = new FileWriter(MEMBER_LIST_FILE_PATH)) {
            file.write(formattedArray);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
