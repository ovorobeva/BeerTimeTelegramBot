package bot;

import DTO.ItemDTO;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import parsing.BeerParser;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class ShowAllCommand extends ServiceCommand {

    public ShowAllCommand(String identifier, String description) {
        super(identifier, description);
    }


    @SneakyThrows
    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        //формируем имя пользователя - поскольку userName может быть не заполнено, для этого случая используем имя и фамилию пользователя
        String userName = (user.getUserName() != null) ? user.getUserName() :
                String.format("%s %s", user.getLastName(), user.getFirstName());
        //обращаемся к методу суперкласса для отправки пользователю ответа
        HashMap<String, ItemDTO> beers = BeerParser.getActualBeers();
        StringBuilder beerList = new StringBuilder();
        for (Map.Entry<String, ItemDTO> beer : beers.entrySet()) {
            if (!beer.getValue().getInfo().isAvailable())
                beerList.append("~");
            beerList.append("id: ").append(beer.getValue().getId()).append(" ")
                    .append(beer.getValue().getInfo())
                    .append("\n").append(beer.getValue().getSmallVolume())
                    .append(" ").append(beer.getValue().getLargeVolume());
            if (!beer.getValue().getInfo().isAvailable())
                beerList.append("~");
               // beerList.append(" BEER IS NOT AVAILABLE NOW");
                    beerList.append("\n\n");
        }
        String answer = URLEncoder.encode(beerList.toString(), "UTF-8");
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                "Actual menu: \n" + answer);
    }
}
