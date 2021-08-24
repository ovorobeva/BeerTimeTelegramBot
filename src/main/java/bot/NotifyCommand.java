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
        if (userList.contains(user))
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                    "You have already started notifying");
        else {
            userList.add(user);
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                    "Start checking changes");
        }
        if (userList.isEmpty())
            startCheckingChanges(absSender, chat, user, userName);

        //обращаемся к методу суперкласса для отправки пользователю ответа
    }


    @SneakyThrows
    private void startCheckingChanges(AbsSender absSender, Chat chat, User user, String username) {
        TimerTask task = new TimerTask() {
            public void run() {
                startCheckingChanges(absSender, chat, user, username);
            }
        };
        if (!userList.isEmpty()) {
            System.out.println("Users to notify: " + userList);
            List<String> changeList = BeerParser.checkChanges();
            System.out.println("Changelist is: " + changeList);

            if (!changeList.isEmpty()) {
                StringBuilder answer = new StringBuilder();
                for (String change : changeList)
                    answer.append(change).append("\n");
                if (userList.contains(user)) {
                    System.out.println("User is found. Username is: " + username);
                    sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), username, answer.toString());
                }
            }
            Timer timer = new Timer();
            timer.schedule(task, 300000);
        }
    }

}