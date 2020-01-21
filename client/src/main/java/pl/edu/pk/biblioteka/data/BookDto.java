package pl.edu.pk.biblioteka.data;

import org.apache.log4j.Logger;

public class BookDto {
    private static final Logger logger = Logger.getLogger(BookDto.class.getName());

    private int bookId;
    private String signature;
    private Author author;
    private Publisher publisher;
    private Department department;
    private String title;
    private String category;
    private String keywords;
    private boolean withdrawn;

    public BookDto() {

    }

    public BookDto(int bookId, String signature, Author author, Publisher publisher, Department department,
                   String title, String category, String keywords, boolean withdrawn) {
        this.bookId = bookId;
        this.signature = signature;
        this.author = author;
        this.publisher = publisher;
        this.department = department;
        this.title = title;
        this.category = category;
        this.keywords = keywords;
        this.withdrawn = withdrawn;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public static Logger getLogger() {
        return logger;
    }

    public boolean isWithdrawn() {
        return withdrawn;
    }

    public void setWithdrawn(boolean withdrawn) {
        this.withdrawn = withdrawn;
    }

    @Override
    public String toString() {
        return "BookDto{" +
                "bookId=" + bookId +
                ", signature='" + signature + '\'' +
                ", author=" + author +
                ", publisher=" + publisher +
                ", department=" + department +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", keywords='" + keywords + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        return bookId == ((BookDto) o).bookId;
    }
}
