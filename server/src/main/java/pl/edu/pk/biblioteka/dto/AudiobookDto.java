package pl.edu.pk.biblioteka.dto;

public class AudiobookDto {
    private int audiobookId;
    private BookDto book;

    public AudiobookDto(int audiobookId, BookDto book) {
        this.audiobookId = audiobookId;
        this.book = book;
    }

    public int getAudiobookId() {
        return audiobookId;
    }

    public void setAudiobookId(int audiobookId) {
        this.audiobookId = audiobookId;
    }

    public BookDto getBook() {
        return book;
    }

    public void setBook(BookDto book) {
        this.book = book;
    }
}
