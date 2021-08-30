package parsing;

import DTO.BeerInfoDTO;
import DTO.ItemDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BeerParser {
    private static final String SMALL = "small";
    private static final String LARGE = "large";
    private static HashMap<String, ItemDTO> currentBeers = getActualBeers();


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
            BeerInfoDTO beerInfo = getBeerInfo(item);
            ItemDTO itemDTO = new ItemDTO(getId(item), beerInfo, getPriceInfo(item, beerInfo.isAvailable(), SMALL), getPriceInfo(item, beerInfo.isAvailable(), LARGE));
            itemsDTO.put(itemDTO.getId(), itemDTO);
        }
        return itemsDTO;
    }

    public static List<String> checkChanges() {
        HashMap<String, ItemDTO> oldBeersList = currentBeers;
        List<String> changeList = new LinkedList<>();
        currentBeers = getActualBeers();
        if (currentBeers.isEmpty()){
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

    private static void checkChangesInLists(HashMap<String, ItemDTO> beerListToCompareWith, HashMap<String, ItemDTO> beerListToRunCompare, List<String> changeList, boolean isCompairingByOldList) {

        for (Map.Entry<String, ItemDTO> beer : beerListToRunCompare.entrySet()) {
            if (beerListToCompareWith.containsKey(beer.getKey())) {
          //      System.out.println(beer.getValue().getInfo().getName() + " is found by ID. Start comparing info.");
                if (!beer.getValue().equals(beerListToCompareWith.get(beer.getKey()))) {
               //     System.out.println(beer.getValue() + " doesn't equals to " + beerListToCompareWith.get(beer.getKey()));
                    if (beer.getValue().getInfo().getName().equals(beerListToCompareWith.get(beer.getKey()).getInfo().getName()) &&
                            beer.getValue().getInfo().getProvider().equals(beerListToCompareWith.get(beer.getKey()).getInfo().getProvider())) {
                 //       System.out.println("Beer names are equal");
                        if (beer.getValue().getInfo().isAvailable() != beerListToCompareWith.get(beer.getKey()).getInfo().isAvailable()){
                            String availability;
                            if (beer.getValue().getInfo().isAvailable() && !isCompairingByOldList)
                                availability = " is available now ✅";
                            else availability = " is not available anymore ❌";
                            changeList.add("Beer " + beer.getValue().getInfo() + availability);
                        }
                    } else
                        if (isCompairingByOldList)
                           changeList.add("❌ Beer " + beer.getValue().getInfo() + " is not available anymore");
                        else
                        changeList.add("✅ New beer is available: " + beer.getValue().getInfo()
                                + beer.getValue().getSmallVolume() + " "
                                + beer.getValue().getLargeVolume());
                }
                //else System.out.println(beer.getValue() + " completely equals to " + beerListToCompareWith.get(beer.getKey()));
            } else{
    //            System.out.println(beer.getValue().getInfo() + " is not found by ID");
                if (isCompairingByOldList)
                    changeList.add("❌ Beer " + beer.getValue().getInfo() + " is not available anymore");
                else
                    changeList.add("✅ New beer is available: " + beer.getValue().getInfo()
                            + beer.getValue().getSmallVolume() + " "
                            + beer.getValue().getLargeVolume());}
        }


    }


    private static BeerInfoDTO getBeerInfo(Element element) {
        String provider = element.getElementsByAttributeValue("class", "pivovar").text();
        String name = element.getElementsByAttributeValue("class", "nazev").text();
        String description = element.getElementsByAttributeValue("class", "popis").text();
        boolean isAvailable = element.getElementsByAttributeValue("class", "skrt").isEmpty();

        BeerInfoDTO beerInfo = new BeerInfoDTO(provider, name, description, isAvailable);
        System.out.println("beer info: " + beerInfo + " isAvailable = " + isAvailable);
        return beerInfo;
    }

    private static String getId(Element element) {
        String id = element.getElementsByIndexEquals(0).select("td").first().text();
        System.out.println("id = " + id);
        return id;
    }

    private static String getPriceInfo(Element element, boolean isAvailable, String type) {
        String volumeAndPrice = "";
        if (!isAvailable) {
            volumeAndPrice = "-";
        } else {
            switch (type) {
                case SMALL:
                    volumeAndPrice = element.getElementsByTag("td").get(2).text();
                    break;
                case LARGE:
                    volumeAndPrice = element.getElementsByTag("td").get(3).text();
            }
        }

        System.out.println(type + " price is: " + volumeAndPrice);
        return volumeAndPrice;
    }
}
