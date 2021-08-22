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

    public static List<User> userList = new LinkedList<>();

    public NotifyCommand(String identifier, String description) {
        super(identifier, description);
    }


    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = (user.getUserName() != null) ? user.getUserName() :
                String.format("%s %s", user.getLastName(), user.getFirstName());
        if (!userList.contains(user)){
        userList.add(user);
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                "Start checking changes");
        //обращаемся к методу суперкласса для отправки пользователю ответа
        startCheckingChanges(absSender, chat, userName, user);
    } else sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                    "You have already started notifying");
    }

    @SneakyThrows
    private void startCheckingChanges(AbsSender absSender, Chat chat, String userName, User user) {
            TimerTask task = new TimerTask() {
                public void run() {
                    startCheckingChanges(absSender, chat, userName, user);
                }
            };
            
        if (userList.contains(user)) {
            List<String> changeList = BeerParser.checkChanges();

            for (String change : changeList)
                sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                        change);
            Timer timer = new Timer();
            timer.schedule(task, 300000);

        }
    }

}