package bot;

import json.ChatsToNotifyJson;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static bot.StartBot.chatList;

/**
 * Команда "Старт"
 */
public class StopCommand extends ServiceCommand {

    public StopCommand(String identifier, String description) {
        super(identifier, description);
    }


    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = (user.getUserName() != null) ? user.getUserName() :
                String.format("%s %s", user.getLastName(), user.getFirstName());
        if (chatList.contains(chat)){
            chatList.remove(chat);
            ChatsToNotifyJson.saveChatsToJson(chatList);
            System.out.println("Chats to notify: " + chatList);
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                    "Checking changes in beer list has stopped. If you want to check again type /notify");
        } else sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                "You haven't started notifying");

    }

}