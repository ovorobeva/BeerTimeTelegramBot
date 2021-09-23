package parsing;

import DTO.ItemDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BeerParser {
    private static HashMap<String, ItemDTO> currentBeers = getActualBeers();

    public static void resetCurrentBeers() {
        currentBeers = null;
    }

    public static HashMap<String, ItemDTO> getActualBeers() {
        HashMap<String, ItemDTO> itemsDTO = new HashMap<>();
        Document response = Document.createShell("BEERTIME");
        try {
            response = Jsoup.connect("https://www.beertime.pub/").get();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements items = response.getElementsByAttributeValue("class", "polozka");

        for (Element item : items) {
            ItemDTO itemDTO = new ItemDTO(item);
            itemsDTO.put(itemDTO.getId(), itemDTO);
        }
        return itemsDTO;
    }

    public static Set<String> checkChanges() {
        HashMap<String, ItemDTO> oldBeersList = currentBeers;
        Set<String> changeList = new HashSet<>();
        currentBeers = getActualBeers();
        if (currentBeers.isEmpty()) {
            try {
                Thread.sleep(100);
                currentBeers = getActualBeers();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        checkChangesInLists(currentBeers, oldBeersList, changeList, true);
        checkChangesInLists(oldBeersList, currentBeers, changeList, false);
        return changeList;
    }

    private static void checkChangesInLists(HashMap<String, ItemDTO> beerListToCompareWith, HashMap<String, ItemDTO> beerListToRunCompare, Set<String> changeList, boolean isOldBeer) {

        for (Map.Entry<String, ItemDTO> beer : beerListToRunCompare.entrySet()) {
            if (beerListToCompareWith.containsKey(beer.getKey())) {
                //      System.out.println(beer.getValue().getInfo().getName() + " is found by ID. Start comparing info.");
                if (!beer.getValue().equals(beerListToCompareWith.get(beer.getKey()))) {
                    //     System.out.println(beer.getValue() + " doesn't equals to " + beerListToCompareWith.get(beer.getKey()));
                    if (beer.getValue().getInfo().getName().equals(beerListToCompareWith.get(beer.getKey()).getInfo().getName()) &&
                            beer.getValue().getInfo().getProvider().equals(beerListToCompareWith.get(beer.getKey()).getInfo().getProvider())) {
                        //       System.out.println("Beer names are equal");
                        if (beer.getValue().getInfo().isAvailable() != beerListToCompareWith.get(beer.getKey()).getInfo().isAvailable()) {
                            String availability;
                            if (beer.getValue().getInfo().isAvailable() && !isOldBeer)
                                availability = " is available now ✅";
                            else availability = " is not available anymore ❌";
                            {
                                changeList.add("Beer " + beer.getValue().getInfo() + availability);
                                System.out.println("adding a beer to the changelist: " + beer.getValue().getInfo() + " " +  availability + "Beer is found in the old list: " + isOldBeer);
                            }
                        }
                    } else if (beer.getValue().getInfo().isAvailable()) {
                        if (isOldBeer){
                            changeList.add("❌ Beer " + beer.getValue().getInfo() + " is not available anymore");
                            System.out.println("adding a beer to the changelist: " + beer.getValue().getInfo() + " " + beer.getValue().getInfo().isAvailable() + "Beer is found in the old list: " + isOldBeer);
                        }
                        else {
                            changeList.add("✅ New beer is available: " + beer.getValue().getInfo()
                                    + beer.getValue().getSmallVolume() + " "
                                    + beer.getValue().getLargeVolume());
                            System.out.println("adding a beer to the changelist: " + beer.getValue().getInfo() + " " + beer.getValue().getInfo().isAvailable() + "Beer is found in the old list: " + isOldBeer);
                        }
                    }
                }
                //else System.out.println(beer.getValue() + " completely equals to " + beerListToCompareWith.get(beer.getKey()));
            } else if (beer.getValue().getInfo().isAvailable()) {
                //            System.out.println(beer.getValue().getInfo() + " is not found by ID");
                if (isOldBeer) {
                    changeList.add("❌ Beer " + beer.getValue().getInfo() + " is not available anymore");
                    System.out.println("adding a beer to the changelist: " + beer.getValue().getInfo() + " " + beer.getValue().getInfo().isAvailable() + "Beer is found in the old list: " + isOldBeer);
                }
                else{
                    changeList.add("✅ New beer is available: " + beer.getValue().getInfo()
                            + beer.getValue().getSmallVolume() + " "
                            + beer.getValue().getLargeVolume());
                    System.out.println("adding a beer to the changelist: " + beer.getValue().getInfo() + " " + beer.getValue().getInfo().isAvailable() + "Beer is found in the old list: " + isOldBeer);
                }

            }


        }
    }
}
