package json;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class ChatsToNotifyJson {
    private static final Type REVIEW_TYPE = new TypeToken<List<Chat>>() {
    }.getType();

    public static void saveChatsToJson(List<Chat> chatList, AbsSender absSender) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        JSONArray chatsArray = new JSONArray();
        for (Chat chat : chatList) {
            JSONObject chatToSave = null;
            try {
                chatToSave = new JSONObject(gson.toJson(chat));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            chatsArray.put(chatToSave);
        }
        chatsArray.put(gson.toJson(absSender));

        File targetFile = new File("target/chats_to_notify.json");
        try {
            targetFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (
                BufferedWriter writer = Files.newBufferedWriter(targetFile.toPath(), StandardCharsets.UTF_8)) {
            writer.write(chatsArray.toString());
            System.out.println("Chats saved: " + chatsArray);
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Chat> readChatListFromJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonReader reader = null;
        File file = new File("target/chats_to_notify.json");
        try {
            file.createNewFile();
            reader = new JsonReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Chat> chatList = gson.fromJson(reader, REVIEW_TYPE);
        System.out.println("Reading chats from file: " + chatList);
        return chatList;
    }

    public static AbsSender readAbsSenderFromJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        File file = new File("target/chats_to_notify.json");
        JsonReader reader = null;
        try {
            file.createNewFile();
            reader = new JsonReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AbsSender absSender = gson.fromJson(reader, REVIEW_TYPE);
        System.out.println("Getting sender from file: " + absSender);
        return absSender;
    }
}
