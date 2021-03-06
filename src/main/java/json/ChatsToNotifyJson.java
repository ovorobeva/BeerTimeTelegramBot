package json;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.telegram.telegrambots.meta.api.objects.Chat;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

public class ChatsToNotifyJson {
    private static final Type REVIEW_TYPE = new TypeToken<List<Chat>>() {
    }.getType();
    private static File file = new File("resources/chats_to_notify.json");

    public static void saveChatsToJson(List<Chat> chatList) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        JSONArray chatsArray = new JSONArray();
        for (Chat chat : chatList) {
            JSONObject chatToSave = null;
            try {
                chatToSave = new JSONObject(gson.toJson(chat));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            chatsArray.add(chatToSave);
        }

        try {
            file.createNewFile();
            PrintWriter writerToDelete = new PrintWriter(file);
            writerToDelete.print("");
            writerToDelete.close();
            System.out.println("File is here: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (
                BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
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
        try {
            System.out.println("File is created: " + file.createNewFile() + " path is: " + file.getAbsolutePath());
            reader = new JsonReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Chat> chatList = gson.fromJson(reader, REVIEW_TYPE);
        System.out.println("Reading chats from file: " + chatList);
        if (chatList == null) chatList = new LinkedList<>();
        return chatList;
    }
}
