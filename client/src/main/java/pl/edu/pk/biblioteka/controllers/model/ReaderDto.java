package pl.edu.pk.biblioteka.controllers.model;

import pl.edu.pk.biblioteka.data.Account;

public class ReaderDto extends UserDto {
    private int readerId;

    public ReaderDto(int readerId, String name, String surname, String pesel, Account account) {
        super(name, surname, pesel, account);
        this.readerId = readerId;
    }

    public int getReaderId() {
        return readerId;
    }

    public void setReaderId(int readerId) {
        this.readerId = readerId;
    }
}
