package pl.edu.pk.biblioteka.dto;

import pl.edu.pk.biblioteka.data.Account;

import java.util.Optional;

public class LibrarianDto {
    private int librarianId;
    private String pesel;
    private String name;
    private String surname;
    private Optional<String> nickname;
    private Account account;

    public LibrarianDto(int librarianId, String pesel, String name, String surname, Optional<String> nickname, Account account) {
        this.librarianId = librarianId;
        this.pesel = pesel;
        this.name = name;
        this.surname = surname;
        this.nickname = nickname;
        this.account = account;
    }

    public LibrarianDto(String pesel, String name, String surname, Optional<String> nickname, Account account) {
        this.pesel = pesel;
        this.name = name;
        this.surname = surname;
        this.nickname = nickname;
        this.account = account;
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
