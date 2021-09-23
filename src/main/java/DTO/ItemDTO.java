package DTO;


import lombok.Data;
import org.jsoup.nodes.Element;

import java.util.Objects;
@Data
public class ItemDTO {
    String id;
    BeerInfoDTO info;
    String smallVolume;
    String largeVolume;
    private static final String SMALL = "small";
    private static final String LARGE = "large";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemDTO)) return false;
        ItemDTO itemDTO = (ItemDTO) o;
        return getId().equals(itemDTO.getId()) && getInfo().equals(itemDTO.getInfo()) && getSmallVolume().equals(itemDTO.getSmallVolume()) && getLargeVolume().equals(itemDTO.getLargeVolume());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getInfo(), getSmallVolume(), getLargeVolume());
    }

    public ItemDTO(Element element) {
        String id = element.getElementsByIndexEquals(0).select("td").first().text();

        this.id = id;
        this.info = new BeerInfoDTO(element);
        this.smallVolume = getPriceInfo(element, info.isAvailable(), SMALL);
        this.largeVolume = getPriceInfo(element, info.isAvailable(), LARGE);
    }

    private String getPriceInfo(Element element, boolean isAvailable, String type) {
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

  //      System.out.println(type + " price is: " + volumeAndPrice);
        return volumeAndPrice;
    }
    @Override
    public String toString() {
        return "ItemDTO{" +
                "id='" + id + '\'' +
                ", info=" + info +
                ", smallVolume=" + smallVolume +
                ", largeVolume=" + largeVolume +
                '}';
    }
}
