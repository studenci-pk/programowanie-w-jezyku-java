package pl.edu.pk.biblioteka.data;

import org.json.JSONObject;

public class Publisher {
    private int publisherId;
    private String name;
    private String address;
    private String country;

    public Publisher(int publisherId, String name, String address, String country) {
        this.publisherId = publisherId;
        this.name = name;
        this.address = address;
        this.country = country;
    }

    public static Publisher valueOf(JSONObject o) {
        return new Publisher(
                o.getInt("publisherId"),
                o.getString("name"),
                o.getString("address"),
                o.getString("country"));
    }

    public int getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(int publisherId) {
        this.publisherId = publisherId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        return publisherId == ((Publisher) o).publisherId;
    }
}
