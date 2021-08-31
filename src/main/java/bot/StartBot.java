package bot;

import json.ChatsToNotifyJson;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class StartBot {
    private static final Map<String, String> getenv = System.getenv();
    public static List<Chat> chatList = new LinkedList<>();
    public static Bot bot;

    public static void main(String[] args) {

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            bot = new Bot(getenv.get("BOT_NAME"), getenv.get("BOT_TOKEN"));
            botsApi.registerBot(bot);
            chatList = ChatsToNotifyJson.readChatListFromJson();
            if (!chatList.isEmpty()) {
                bot.startCheckingChanges(chatList);
                System.out.println("Chats to notify are found: \n" + chatList);
            }


        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


}
