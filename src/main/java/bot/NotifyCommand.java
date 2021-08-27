package bot;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import parsing.BeerParser;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Команда "Старт"
 */
public class NotifyCommand extends ServiceCommand {

    public static List<Chat> chatList = new LinkedList<>();

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
            startCheckingChanges(absSender);
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

        //обращаемся к методу суперкласса для отправки пользователю ответа
    }


    @SneakyThrows
    private void startCheckingChanges(AbsSender absSender) {
        TimerTask task = new TimerTask() {
            public void run() {
                startCheckingChanges(absSender);
            }
        };
        if (!chatList.isEmpty()) {
            System.out.println("Users to notify: " + chatList);
            List<String> changeList = BeerParser.checkChanges();
            System.out.println("Changelist is: " + changeList);

            if (!changeList.isEmpty()) {
                StringBuilder answer = new StringBuilder();
                for (String change : changeList)
                    answer.append(change).append("\n");
                for (Chat chat: chatList){
                    String userName = (chat.getUserName() != null) ? chat.getUserName() :
                            String.format("%s %s", chat.getLastName(), chat.getFirstName());
                    sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName, answer.toString());
                }
            }
            Timer timer = new Timer();
            timer.schedule(task, 300000);
        }
    }

}