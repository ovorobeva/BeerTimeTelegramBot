package bot;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import parsing.BeerParser;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Команда "Старт"
 */
public class NotifyCommand extends ServiceCommand {

    public static boolean isStopped;

    public NotifyCommand(String identifier, String description) {
        super(identifier, description);
    }


    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        isStopped = false;
        //формируем имя пользователя - поскольку userName может быть не заполнено, для этого случая используем имя и фамилию пользователя
        String userName = (user.getUserName() != null) ? user.getUserName() :
                String.format("%s %s", user.getLastName(), user.getFirstName());
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                "Start checking changes");
        //обращаемся к методу суперкласса для отправки пользователю ответа
        startCheckingChanges(absSender, chat, this.getCommandIdentifier(), userName);
    }

    @SneakyThrows
    private void startCheckingChanges(AbsSender absSender, Chat chat, String commandIdentifier, String userName) {
            TimerTask task = new TimerTask() {
                public void run() {
                    startCheckingChanges(absSender, chat, commandIdentifier, userName);
                }
            };
        if (!isStopped) {
            List<String> changeList = BeerParser.checkChanges();

            for (String change : changeList)
                sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                        change);
            Timer timer = new Timer();
            timer.schedule(task, 300000);

        }
    }

}