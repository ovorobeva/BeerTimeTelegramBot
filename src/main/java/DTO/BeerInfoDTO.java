package DTO;

import lombok.Data;
import org.jsoup.nodes.Element;

import java.util.Objects;

@Data
public class BeerInfoDTO {
    String provider;
    String name;
    String description;
    boolean isAvailable;
    String flag;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BeerInfoDTO)) return false;
        BeerInfoDTO that = (BeerInfoDTO) o;
        return isAvailable() == that.isAvailable() && getProvider().equals(that.getProvider()) && getName().equals(that.getName()) && getDescription().equals(that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProvider(), getName(), getDescription(), isAvailable());
    }

    public BeerInfoDTO (Element element) {
        String provider = element.getElementsByAttributeValue("class", "pivovar").text();
        String name = element.getElementsByAttributeValue("class", "nazev").text();
        String description = element.getElementsByAttributeValue("class", "popis").text();
        boolean isAvailable = element.getElementsByAttributeValue("class", "skrt").isEmpty();
        String countryCode = element.getElementsByTag("img").attr("alt");
        String flag = countryCodeToEmoji(countryCode);

        this.provider = provider;
        this.name = name;
        this.description = description;
        this.isAvailable = isAvailable;
        this.flag = flag;
        System.out.println("beer info: " + this + " isAvailable = " + isAvailable);
    }



    private String countryCodeToEmoji(String code) {

        // offset between uppercase ascii and regional indicator symbols
        int OFFSET = 127397;

        // validate code
        if(code == null || code.length() != 2) {
            return "";
        }

        //fix for uk -> gb
        if (code.equalsIgnoreCase("uk")) {
            code = "gb";
        }

        // convert code to uppercase
        code = code.toUpperCase();

        StringBuilder emojiStr = new StringBuilder();

        //loop all characters
        for (int i = 0; i < code.length(); i++) {
            emojiStr.appendCodePoint(code.charAt(i) + OFFSET);
        }

        // return emoji
        return emojiStr.toString();
    }



    public boolean isAvailable() {
        return isAvailable;
    }


    @Override
    public String toString() {
        return "beer: '" + provider +
                " " + name + '\'' +
                ", description: " + description + flag;
    }

}
