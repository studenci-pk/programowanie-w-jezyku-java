package pl.edu.pk.biblioteka.controllers.model;

import pl.edu.pk.biblioteka.data.Account;

public class UserDto {
    private String name;
    private String surname;
    private String pesel;
    private Account account;

    @Override
    public String toString() {
        return "" + account.getLogin() + "   " + name + " " + surname + "";
    }

    public UserDto() { }

    public UserDto(String name, String surname, String pesel, Account account) {
        this.name = name;
        this.surname = surname;
        this.pesel = pesel;
        this.account = account;
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

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}