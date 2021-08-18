package bot;

import lombok.Getter;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

    public final class Bot extends TelegramLongPollingCommandBot {
        private final String BOT_NAME;
        private final String BOT_TOKEN;


        public Bot(String botName, String botToken) {
            super();
            this.BOT_NAME = botName;
            this.BOT_TOKEN = botToken;

            register(new ShowAllCommand("show", "Показать все"));
            register(new NotifyCommand("notify", "Сообщи, когда обновится"));
            register(new StopCommand("stop", "Больше не сообщай"));
        }

        @Override
        public String getBotToken() {
            return BOT_TOKEN;
        }

        @Override
        public String getBotUsername() {
            return BOT_NAME;
        }

        /**
         * Ответ на запрос, не являющийся командой
         */
        @Override
        public void processNonCommandUpdate(Update update) {
            Message msg = update.getMessage();
            Long chatId = msg.getChatId();
            String userName = getUserName(msg);
            String answer = "Type /show to show current beer list \nType /notify to notify you every time, when there are changes in beer list" +
                    "\nType /stop to stop notifying you";

            setAnswer(chatId, userName, answer);
        }

        /**
         * Формирование имени пользователя
         * @param msg сообщение
         */
        private String getUserName(Message msg) {
            User user = msg.getFrom();
            String userName = user.getUserName();
            return (userName != null) ? userName : String.format("%s %s", user.getLastName(), user.getFirstName());
        }

        /**
         * Отправка ответа
         * @param chatId id чата
         * @param userName имя пользователя
         * @param text текст ответа
         */
        private void setAnswer(Long chatId, String userName, String text) {
            SendMessage answer = new SendMessage();
            answer.setText(text);
            answer.setChatId(chatId.toString());
            try {
                execute(answer);
            } catch (TelegramApiException e) {
                //логируем сбой Telegram Bot API, используя userName
            }
        }
    }



