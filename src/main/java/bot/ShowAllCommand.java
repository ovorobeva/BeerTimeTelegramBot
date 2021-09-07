package bot;

import DTO.ItemDTO;
import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import parsing.BeerParser;

import java.util.HashMap;
import java.util.Map;


public class ShowAllCommand extends ServiceCommand {

    public ShowAllCommand(String identifier, String description) {
        super(identifier, description);
    }


    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = (user.getUserName() != null) ? user.getUserName() :
                String.format("%s %s", user.getLastName(), user.getFirstName());
        HashMap<String, ItemDTO> beers = BeerParser.getActualBeers();
        StringBuilder beerList = new StringBuilder();
        for (Map.Entry<String, ItemDTO> beer : beers.entrySet()) {
            if (!beer.getValue().getInfo().isAvailable())
                beerList.append("❌");
            else beerList.append("✅");
            beerList.append(" id: ").append(beer.getValue().getId()).append(" ")
                    .append(beer.getValue().getInfo())
                    .append("\n").append(beer.getValue().getSmallVolume())
                    .append(" ").append(beer.getValue().getLargeVolume());
            if (!beer.getValue().getInfo().isAvailable())
                beerList.append(" BEER IS NOT AVAILABLE NOW");
                    beerList.append("\n\n");
        }
        String answer = EmojiParser.parseToUnicode(beerList.toString());
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                "Actual menu: \n" + answer);
    }
}
