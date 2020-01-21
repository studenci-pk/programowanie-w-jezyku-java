package pl.edu.pk.biblioteka.dto;

import pl.edu.pk.biblioteka.data.BookDto;

public class CopyDto {
    private int copyId;
    private BookDto book;
    private boolean withdrawn;

    public CopyDto(int copyId, BookDto book, boolean withdrawn) {
        this.copyId = copyId;
        this.book = book;
    }

    public int getCopyId() {
        return copyId;
    }

    public void setCopyId(int copyId) {
        this.copyId = copyId;
    }

    public BookDto getBook() {
        return book;
    }

    public void setBook(BookDto book) {
        this.book = book;
    }

    public boolean isWithdrawn() {
        return withdrawn;
    }

    public void setWithdrawn(boolean withdrawn) {
        this.withdrawn = withdrawn;
    }
}
