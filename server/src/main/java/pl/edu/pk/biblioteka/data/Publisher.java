package pl.edu.pk.biblioteka.data;

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
}
