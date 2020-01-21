package pl.edu.pk.biblioteka.data;

import java.util.Optional;

public class Librarian {
    private int librarianId;
    private String pesel;
    private String name;
    private String surname;
    private Optional<String> nickname;
    private int accountId;

    public Librarian(int librarianId, String pesel, String name, String surname, Optional<String> nickname, int accountId) {
        this.librarianId = librarianId;
        this.pesel = pesel;
        this.name = name;
        this.surname = surname;
        this.nickname = nickname;
        this.accountId = accountId;
    }

    public Librarian(String pesel, String name, String surname, Optional<String> nickname, int accountId) {
        this.pesel = pesel;
        this.name = name;
        this.surname = surname;
        this.nickname = nickname;
        this.accountId = accountId;
    }


    public int getLibrarianId() {
        return librarianId;
    }

    public void setLibrarianId(int librarianId) {
        this.librarianId = librarianId;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
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

    public Optional<String> getNickname() {
        return nickname;
    }

    public void setNickname(Optional<String> nickname) {
        this.nickname = nickname;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
}
