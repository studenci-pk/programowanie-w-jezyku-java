package pl.edu.pk.biblioteka.dto;

public class EbookDto {
    private int ebookId;
    private BookDto book;

    public EbookDto(int ebookId, BookDto book) {
        this.ebookId = ebookId;
        this.book = book;
    }

    public int getEbookId() {
        return ebookId;
    }

    public void setEbookId(int ebookId) {
        this.ebookId = ebookId;
    }

    public BookDto getBook() {
        return book;
    }

    public void setBook(BookDto book) {
        this.book = book;
    }
}
