package pl.edu.pk.biblioteka.controllers.model;

import pl.edu.pk.biblioteka.data.Account;

public class LibrarianDto extends UserDto {
    private int librarianId;

    public LibrarianDto(int librarianId, String name, String surname, String pesel, Account account) {
        super(name, surname, pesel, account);
        this.librarianId = librarianId;
    }

    public int getLibrarianId() {
        return librarianId;
    }

    public void setLibrarianId(int librarianId) {
        this.librarianId = librarianId;
    }
}
