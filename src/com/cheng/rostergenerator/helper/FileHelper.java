package com.cheng.rostergenerator.helper;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.cheng.rostergenerator.model.User;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FileHelper {

    private static final String MEMBER_LIST_FILE_PATH = "src/res/memberList.json";

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
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    User user = parseUser(jsonObject);
                    memberList.add(user);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return memberList;
    }

    private static User parseUser(JSONObject json) 
    {
        JSONObject userObject = (JSONObject) json.get("member");
        String name = (String) userObject.get("name");
        boolean experienced = (boolean) userObject.get("experienced");
        boolean assignSpeech = (boolean) userObject.get("assignSpeech");

        return new User(name, experienced, assignSpeech);
    }

}
