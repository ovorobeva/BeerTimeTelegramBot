package DTO;

import java.util.Objects;

public class BeerInfoDTO {
    String provider;
    String name;
    String description;
    boolean isAvailable;

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

    public BeerInfoDTO(String provider, String name, String description, boolean isAvailable) {
        this.provider = provider;
        this.name = name;
        this.description = description;
        this.isAvailable = isAvailable;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        return "beer: '" + provider +
                " " + name + '\'' +
                ", description: " + description;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
