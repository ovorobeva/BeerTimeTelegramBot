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
    private static BeerParser beerParser;


    public static HashMap<String, ItemDTO> getActualBeers() {
        HashMap<String, ItemDTO> itemsDTO = new HashMap<>();
        Document response = Document.createShell("BEERTIME");
        try {
            response = Jsoup.connect("https://www.beertime.pub/").get();
            //    System.out.println(response.getElementsByAttributeValue("class","polozkyTab"));

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
        for (Map.Entry<String, ItemDTO> oldBeer : oldBeersList.entrySet()) {
            if (currentBeers.containsKey(oldBeer.getKey())) {
                isAvailabilityChanged(currentBeers, changeList, oldBeer);
            } else {
                changeList.add("❌ Beer " + oldBeer.getValue().getInfo().getProvider() + " " + oldBeer.getValue().getInfo().getName() + " is not available anymore");
            }
        }
        for (Map.Entry<String, ItemDTO> currentBeer : currentBeers.entrySet()){
            if (oldBeersList.containsKey(currentBeer.getKey())){
                isAvailabilityChanged(oldBeersList, changeList, currentBeer);
            } else
                changeList.add("✅ New beer is available: " + currentBeer.getValue().getInfo().getProvider() + " "
                        + currentBeer.getValue().getInfo().getName() + " "
                + currentBeer.getValue().getSmallVolume() + " "
                + currentBeer.getValue().getLargeVolume());
        }
        return changeList;
    }

    private static void isAvailabilityChanged(HashMap<String, ItemDTO> oldBeersList, List<String> changeList, Map.Entry<String, ItemDTO> currentBeer) {
        if (!currentBeer.getValue().equals(oldBeersList.get(currentBeer.getKey())))
            if (currentBeer.getValue().getInfo().getName().equals(oldBeersList.get(currentBeer.getKey()).getInfo().getName()))
                if (currentBeer.getValue().getInfo().isAvailable() != oldBeersList.get(currentBeer.getKey()).getInfo().isAvailable()){
                    String availability;
                    if (currentBeers.get(currentBeer.getKey()).getInfo().isAvailable())
                        availability = " is now available ✅";
                    else availability = " is not available anymore ❌";
                    changeList.add("Beer " + currentBeer.getValue().getInfo().getProvider() + " " + currentBeer.getValue().getInfo().getName() + availability);
                }
    }


    private static BeerInfoDTO getBeerInfo(Element element) {
        String provider = element.getElementsByAttributeValue("class", "pivovar").text();
        String name = element.getElementsByAttributeValue("class", "nazev").text();
        String description = element.getElementsByAttributeValue("class", "popis").text();
        boolean isAvailable = element.getElementsByAttributeValue("class", "skrt").isEmpty();

        BeerInfoDTO beerInfo = new BeerInfoDTO(provider, name, description, isAvailable);
        System.out.println("beer info: " + beerInfo);
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
