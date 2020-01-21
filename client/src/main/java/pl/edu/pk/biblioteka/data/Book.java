package pl.edu.pk.biblioteka.data;

public class Book {
    private int bookId;
    private String signature;
    private String author;
    private int pubisherId;
    private int departmentId;
    private String title;
    private String category;
    private String keywords;
    private boolean withdrawn;

    public Book(int bookId, String signature, String author, int pubisherId, int departmentId,
                String title, String category, String keywords, boolean withdrawn) {
        this.bookId = bookId;
        this.signature = signature;
        this.author = author;
        this.pubisherId = pubisherId;
        this.departmentId = departmentId;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPubisherId() {
        return pubisherId;
    }

    public void setPubisherId(int pubisherId) {
        this.pubisherId = pubisherId;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
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

    public boolean isWithdrawn() {
        return withdrawn;
    }

    public void setWithdrawn(boolean withdrawn) {
        this.withdrawn = withdrawn;
    }
}
