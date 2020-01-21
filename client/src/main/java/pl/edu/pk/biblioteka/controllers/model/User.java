package pl.edu.pk.biblioteka.controllers.model;

public class User {
    private String login;
    private String name;
    private String surname;

    public User(String login, String name, String surname) {
        this.login = login;
        this.name = name;
        this.surname = surname;
    }

    public static User valueOf(Librarian librarian) {
        return new User(
                librarian.getLogin(),
                librarian.getLogin(),
                librarian.getLogin()
        );
    }

    @Override
    public String toString() {
        return "" + login + "   " + name + " " + surname + "";
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }
}