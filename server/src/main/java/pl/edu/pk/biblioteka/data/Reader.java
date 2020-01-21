package pl.edu.pk.biblioteka.data;

public class Reader {
    private int readerId;
    private String pesel;
    private String name;
    private String surname;
    private String faculty;
    private String subject;
    private int accountId;

    public Reader(int readerId, String pesel, String name, String surname, String faculty, String subject, int accountId) {
        this.readerId = readerId;
        this.pesel = pesel;
        this.name = name;
        this.surname = surname;
        this.faculty = faculty;
        this.subject = subject;
        this.accountId = accountId;
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

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
}
