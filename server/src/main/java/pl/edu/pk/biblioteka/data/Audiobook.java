package pl.edu.pk.biblioteka.data;

public class Audiobook {
    private int audiobookId;
    private int bookId;

    public Audiobook(int audiobookId, int bookId) {
        this.audiobookId = audiobookId;
        this.bookId = bookId;
    }

    public int getAudiobookId() {
        return audiobookId;
    }

    public void setAudiobookId(int audiobookId) {
        this.audiobookId = audiobookId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
}
