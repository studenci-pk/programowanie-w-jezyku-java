package pl.edu.pk.biblioteka.data;

public class Author {
    private int authorId;
    private String name;
    private String surname;
    private String country;

    public Author(int authorId, String name, String surname, String country) {
        this.authorId = authorId;
        this.name = name;
        this.surname = surname;
        this.country = country;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
