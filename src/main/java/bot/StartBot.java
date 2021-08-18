package bot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import parsing.BeerParser;

import java.util.Map;

public class StartBot {
        private static final Map<String, String> getenv = System.getenv();

        public static void main(String[] args) {
            try {
                TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            //    botsApi.registerBot(new Bot(getenv.get("BOT_NAME"), getenv.get("BOT_TOKEN")));
                botsApi.registerBot(new Bot("BeerTimeAndelBot", "1911489310:AAFYwfw0DIm4SSqywGihF1AUvK2qOgQ6y1c"));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
    }
}
