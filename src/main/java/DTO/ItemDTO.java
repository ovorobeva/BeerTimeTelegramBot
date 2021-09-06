package DTO;


import org.jsoup.nodes.Element;

import java.util.Objects;

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

    public ItemDTO(String id, BeerInfoDTO info, String smallVolume, String largeVolume) {
        this.id = id;
        this.info = info;
        this.smallVolume = smallVolume;
        this.largeVolume = largeVolume;
    }

    public ItemDTO(String id, BeerInfoDTO info, String smallVolume) {
        this.id = id;
        this.info = info;
        this.smallVolume = smallVolume;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

        System.out.println(type + " price is: " + volumeAndPrice);
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

    public BeerInfoDTO getInfo() {
        return info;
    }

    public void setInfo(BeerInfoDTO info) {
        this.info = info;
    }

    public String getSmallVolume() {
        return smallVolume;
    }

    public void setSmallVolume(String smallVolume) {
        this.smallVolume = smallVolume;
    }

    public String getLargeVolume() {
        return largeVolume;
    }

    public void setLargeVolume(String largeVolume) {
        this.largeVolume = largeVolume;
    }
}
