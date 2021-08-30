package bot;

import json.ChatsToNotifyJson;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import parsing.BeerParser;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static bot.NotifyCommand.chatList;

public class StartBot {
        private static final Map<String, String> getenv = System.getenv();

        public static void main(String[] args) {
            try {
                TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
                    botsApi.registerBot(new Bot(getenv.get("BOT_NAME"), getenv.get("BOT_TOKEN")));
                    chatList = ChatsToNotifyJson.readChatListFromJson();


            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
    }

    @SneakyThrows
    public static void startCheckingChanges(List<Chat> chatList) {
        TimerTask task = new TimerTask() {
            public void run() {
                startCheckingChanges(chatList);
            }
        };
        SendMessage sendMessage = new SendMessage();
        if (!chatList.isEmpty()) {
            System.out.println("Users to notify: " + chatList);
            List<String> changeList = BeerParser.checkChanges();
            System.out.println("Changelist is: " + changeList);

            if (!changeList.isEmpty()) {
                StringBuilder answer = new StringBuilder();
                for (String change : changeList)
                    answer.append(change).append("\n");
                for (Chat chat: chatList){
                    sendMessage.setChatId(chat.getId().toString());
                    sendMessage.setText(answer.toString());
                    AbsSender absSender = ChatsToNotifyJson.readAbsSenderFromJson();
                    absSender.execute(sendMessage);
                }
            }
            Timer timer = new Timer();
            timer.schedule(task, 300000);
        }

    }


}
