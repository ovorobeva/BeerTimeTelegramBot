package bot;

import json.ChatsToNotifyJson;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import parsing.BeerParser;

import static bot.StartBot.chatList;

/**
 * Команда "Старт"
 */
public class NotifyCommand extends ServiceCommand {

    public NotifyCommand(String identifier, String description) {
        super(identifier, description);
    }


    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = (user.getUserName() != null) ? user.getUserName() :
                String.format("%s %s", user.getLastName(), user.getFirstName());
        if (chatList.isEmpty()) {
            chatList.add(chat);
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                    "Start checking changes");
            BeerParser.resetCurrentBeers();
            StartBot.bot.startCheckingChanges(chatList);
        } else {
            if (chatList.contains(chat))
                sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                        "You have already started notifying");
            else {
                chatList.add(chat);
                sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                        "Start checking changes");
            }
        }
        ChatsToNotifyJson.saveChatsToJson(chatList);
    }



}