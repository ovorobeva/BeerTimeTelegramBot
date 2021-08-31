package bot;

import json.ChatsToNotifyJson;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.groupadministration.SetChatPhoto;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.stickers.AddStickerToSet;
import org.telegram.telegrambots.meta.api.methods.stickers.CreateNewStickerSet;
import org.telegram.telegrambots.meta.api.methods.stickers.SetStickerSetThumb;
import org.telegram.telegrambots.meta.api.methods.stickers.UploadStickerFile;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.updateshandlers.SentCallback;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import parsing.BeerParser;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.CompletableFuture;


public class StartBot {
    private static final Map<String, String> getenv = System.getenv();
    public static List<Chat> chatList = new LinkedList<>();

    public static void main(String[] args) {

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new Bot(getenv.get("BOT_NAME"), getenv.get("BOT_TOKEN")));
            chatList = ChatsToNotifyJson.readChatListFromJson();
            if (!chatList.isEmpty()) {
                startCheckingChanges(chatList, null);
                System.out.println("Chats to notify are found: \n" + chatList);
            }

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public static void startCheckingChanges(List<Chat> chatList, AbsSender absSender) {
        TimerTask task = new TimerTask() {
            public void run() {
                startCheckingChanges(chatList, absSender);
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
                for (Chat chat : chatList) {
                    sendMessage.setChatId(chat.getId().toString());
                    sendMessage.setText(answer.toString());
                    if (absSender == null)
                        new AbsSender() {
                            @Override
                            public Message execute(SendDocument sendDocument) throws TelegramApiException {
                                return null;
                            }

                            @Override
                            public Message execute(SendPhoto sendPhoto) throws TelegramApiException {
                                return null;
                            }

                            @Override
                            public Message execute(SendVideo sendVideo) throws TelegramApiException {
                                return null;
                            }

                            @Override
                            public Message execute(SendVideoNote sendVideoNote) throws TelegramApiException {
                                return null;
                            }

                            @Override
                            public Message execute(SendSticker sendSticker) throws TelegramApiException {
                                return null;
                            }

                            @Override
                            public Message execute(SendAudio sendAudio) throws TelegramApiException {
                                return null;
                            }

                            @Override
                            public Message execute(SendVoice sendVoice) throws TelegramApiException {
                                return null;
                            }

                            @Override
                            public List<Message> execute(SendMediaGroup sendMediaGroup) throws TelegramApiException {
                                return null;
                            }

                            @Override
                            public Boolean execute(SetChatPhoto setChatPhoto) throws TelegramApiException {
                                return null;
                            }

                            @Override
                            public Boolean execute(AddStickerToSet addStickerToSet) throws TelegramApiException {
                                return null;
                            }

                            @Override
                            public Boolean execute(SetStickerSetThumb setStickerSetThumb) throws TelegramApiException {
                                return null;
                            }

                            @Override
                            public Boolean execute(CreateNewStickerSet createNewStickerSet) throws TelegramApiException {
                                return null;
                            }

                            @Override
                            public File execute(UploadStickerFile uploadStickerFile) throws TelegramApiException {
                                return null;
                            }

                            @Override
                            public Serializable execute(EditMessageMedia editMessageMedia) throws TelegramApiException {
                                return null;
                            }

                            @Override
                            public Message execute(SendAnimation sendAnimation) throws TelegramApiException {
                                return null;
                            }

                            @Override
                            public CompletableFuture<Message> executeAsync(SendDocument sendDocument) {
                                return null;
                            }

                            @Override
                            public CompletableFuture<Message> executeAsync(SendPhoto sendPhoto) {
                                return null;
                            }

                            @Override
                            public CompletableFuture<Message> executeAsync(SendVideo sendVideo) {
                                return null;
                            }

                            @Override
                            public CompletableFuture<Message> executeAsync(SendVideoNote sendVideoNote) {
                                return null;
                            }

                            @Override
                            public CompletableFuture<Message> executeAsync(SendSticker sendSticker) {
                                return null;
                            }

                            @Override
                            public CompletableFuture<Message> executeAsync(SendAudio sendAudio) {
                                return null;
                            }

                            @Override
                            public CompletableFuture<Message> executeAsync(SendVoice sendVoice) {
                                return null;
                            }

                            @Override
                            public CompletableFuture<List<Message>> executeAsync(SendMediaGroup sendMediaGroup) {
                                return null;
                            }

                            @Override
                            public CompletableFuture<Boolean> executeAsync(SetChatPhoto setChatPhoto) {
                                return null;
                            }

                            @Override
                            public CompletableFuture<Boolean> executeAsync(AddStickerToSet addStickerToSet) {
                                return null;
                            }

                            @Override
                            public CompletableFuture<Boolean> executeAsync(SetStickerSetThumb setStickerSetThumb) {
                                return null;
                            }

                            @Override
                            public CompletableFuture<Boolean> executeAsync(CreateNewStickerSet createNewStickerSet) {
                                return null;
                            }

                            @Override
                            public CompletableFuture<File> executeAsync(UploadStickerFile uploadStickerFile) {
                                return null;
                            }

                            @Override
                            public CompletableFuture<Serializable> executeAsync(EditMessageMedia editMessageMedia) {
                                return null;
                            }

                            @Override
                            public CompletableFuture<Message> executeAsync(SendAnimation sendAnimation) {
                                return null;
                            }

                            @Override
                            protected <T extends Serializable, Method extends BotApiMethod<T>, Callback extends SentCallback<T>> void sendApiMethodAsync(Method method, Callback callback) {

                            }

                            @Override
                            protected <T extends Serializable, Method extends BotApiMethod<T>> CompletableFuture<T> sendApiMethodAsync(Method method) {
                                return null;
                            }

                            @Override
                            protected <T extends Serializable, Method extends BotApiMethod<T>> T sendApiMethod(Method method) throws TelegramApiException {
                                return null;
                            }
                        }.execute(sendMessage);
                    else absSender.execute(sendMessage);
                }
            }
            Timer timer = new Timer();
            timer.schedule(task, 300000);
        }

    }


}
