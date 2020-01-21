package pl.edu.pk.biblioteka.dto;

import pl.edu.pk.biblioteka.data.Account;

public class ReaderDto {
    private int readerId;
    private String pesel;
    private String name;
    private String surname;
    private String faculty;
    private String subject;
    private Account account;

    public ReaderDto(int readerId, String pesel, String name, String surname, String faculty, String subject, Account account) {
        this.readerId = readerId;
        this.pesel = pesel;
        this.name = name;
        this.surname = surname;
        this.faculty = faculty;
        this.subject = subject;
        this.account = account;
    }

    public int getReaderId() {
        return readerId;
    }

    public void setReaderId(int readerId) {
        this.readerId = readerId;
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

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "ReaderDto{" +
                "readerId=" + readerId +
                ", pesel='" + pesel + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", faculty='" + faculty + '\'' +
                ", subject='" + subject + '\'' +
                ", account=" + account +
                '}';
    }
}
