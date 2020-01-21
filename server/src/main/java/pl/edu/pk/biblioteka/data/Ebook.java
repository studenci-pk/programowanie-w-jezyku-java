package pl.edu.pk.biblioteka.data;

public class Ebook {
    private int ebookId;
    private int bookId;

    public Ebook(int ebookId, int bookId) {
        this.ebookId = ebookId;
        this.bookId = bookId;
    }

    public int getEbookId() {
        return ebookId;
    }

    public void setEbookId(int ebookId) {
        this.ebookId = ebookId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
}
