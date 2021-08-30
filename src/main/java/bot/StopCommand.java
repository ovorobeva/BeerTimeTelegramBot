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
        //обращаемся к методу суперкласса для отправки пользователю ответа
        if (chatList.contains(chat)){
            chatList.remove(chat);
            ChatsToNotifyJson.saveChatsToJson(chatList, absSender);
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                    "Checking changes in beer list has stopped. If you want to check again type /notify");
        } else sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                "You haven't started notifying");
        //формируем имя пользователя - поскольку userName может быть не заполнено, для этого случая используем имя и фамилию пользователя

    }

}