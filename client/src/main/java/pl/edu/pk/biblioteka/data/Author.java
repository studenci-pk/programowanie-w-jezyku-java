package pl.edu.pk.biblioteka.data;

import org.json.JSONObject;

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

    public static Author valueOf(JSONObject o) {
        return new Author(
                o.getInt("authorId"),
                o.getString("name"),
                o.getString("surname"),
                o.getString("country"));
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

    @Override
    public String toString() {
        return String.format("%s %s", surname, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        return authorId == ((Author) o).authorId;
    }
}
